package com.example.myapplication.dados.banco

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.modelo.Mensagem
import com.example.myapplication.modelo.Pessoa
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        CompraEntity::class,
        CarteiraEntity::class,
        LocalEntity::class,
        Pessoa::class,
        Conversa::class,
        Mensagem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lojaDao(): LojaDao
    abstract fun localDao(): LocalDao
    abstract fun pessoaDao(): PessoaDao
    abstract fun conversaDao(): ConversaDao
    abstract fun mensagemDao(): MensagemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(contexto: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder<AppDatabase>(
                    context = contexto.applicationContext,
                    name = "tinder_app.db"
                )
                    .setDriver(BundledSQLiteDriver())
                    .setQueryCoroutineContext(Dispatchers.IO)

                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}

object BancoDeDados {
    fun obter(contexto: Context): AppDatabase = AppDatabase.getInstance(contexto)
}
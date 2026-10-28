package com.example.myapplication.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.myapplication.data.local.CarteiraEntity
import com.example.myapplication.data.local.CompraEntity
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

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder<AppDatabase>(
                        context = context.applicationContext,
                        name = "tinder_app.db"
                    )
                        .setDriver(BundledSQLiteDriver())
                        .setQueryCoroutineContext(Dispatchers.IO)
                        .fallbackToDestructiveMigration(dropAllTables = true)
                        .build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}
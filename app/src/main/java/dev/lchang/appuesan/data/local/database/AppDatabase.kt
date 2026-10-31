package dev.lchang.appuesan.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.lchang.appuesan.data.local.dao.ApiCountryDao
import dev.lchang.appuesan.data.local.dao.CountryDao
import dev.lchang.appuesan.data.local.entity.ApiCountryEntity
import dev.lchang.appuesan.data.local.entity.CountryEntity

@Database(
    entities = [CountryEntity::class, ApiCountryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun apiCountryDao(): ApiCountryDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add savedAt column to favorite_countries
                db.execSQL("ALTER TABLE favorite_countries ADD COLUMN savedAt INTEGER NOT NULL DEFAULT 0")
                
                // Create api_countries table
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `api_countries` (`name` TEXT NOT NULL, `code` TEXT, `flag` TEXT, PRIMARY KEY(`name`))"
                )
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

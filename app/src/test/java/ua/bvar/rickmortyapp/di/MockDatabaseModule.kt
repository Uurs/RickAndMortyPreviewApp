package ua.bvar.rickmortyapp.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ua.bvar.data.di.RoomDbModule
import ua.bvar.data.localdb.RoomAppDatabase
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RoomDbModule::class]
)
class MockDatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(): RoomAppDatabase {
        return database
    }

    companion object {
        const val DB_SETUP_V1_WITH_FAVORITES = "local_db_v1_with_favorites.sql"
        const val DB_SETUP_V1_NO_FAVORITES = "local_db_v1_no_favorites.sql"

        val database by lazy {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    RoomAppDatabase::class.java,
                )
                .allowMainThreadQueries()
                .build()
        }

        fun setupFromAssets(assetsFileName: String) {
            this::class.java.classLoader!!.getResourceAsStream("database/$assetsFileName")
                .bufferedReader()
                .use {
                    it.lines().forEach { line ->
                        database.compileStatement(line).execute()
                    }
                }
        }

        fun cleanDB() {
            database.clearAllTables()
        }
    }
}
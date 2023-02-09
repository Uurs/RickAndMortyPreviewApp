package ua.bvar.data.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.room.rxjava3.RxRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.bvar.data.localdb.RoomAppDatabase
import javax.inject.Singleton

@VisibleForTesting
@Module
@InstallIn(SingletonComponent::class)
class RoomDbModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): RoomAppDatabase {
        return Room
            .databaseBuilder(context, RoomAppDatabase::class.java, RoomAppDatabase.DATABASE_NAME)
            .build()
    }
}
package ua.bvar.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.bvar.data.localdb.RoomAppDatabase
import ua.bvar.data.localdb.dao.CharactersDao

@Module
@InstallIn(SingletonComponent::class)
internal class RoomDaoModule {

    @Provides
    fun provideCharactersDao(db: RoomAppDatabase): CharactersDao {
        return db.createCharactersDao()
    }
}
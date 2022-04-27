package self.tuan.hocmaians.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import self.tuan.hocmaians.data.AppDatabase
import self.tuan.hocmaians.repositories.AppRepository
import self.tuan.hocmaians.repositories.IRepository
import self.tuan.hocmaians.util.Constants
import self.tuan.hocmaians.util.Constants.TEST_DB_PATH
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        Constants.DB_NAME
    )
        .createFromAsset(TEST_DB_PATH)
        .build()

    @Singleton
    @Provides
    fun provideAppRepository(
        db: AppDatabase
    ): IRepository = AppRepository(db.dao)
}
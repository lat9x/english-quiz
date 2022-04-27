package self.tuan.hocmaians.data

import androidx.room.Database
import androidx.room.RoomDatabase
import self.tuan.hocmaians.data.entities.*

@Database(
    entities = [
        Course::class,
        Topic::class,
        Question::class,
        UserAnswer::class,
        Score::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: AppDao
}
package self.tuan.hocmaians

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class QuizApplication : Application() {

    companion object {
        lateinit var resource: Resources
    }

    override fun onCreate() {
        super.onCreate()

        resource = resources
    }
}
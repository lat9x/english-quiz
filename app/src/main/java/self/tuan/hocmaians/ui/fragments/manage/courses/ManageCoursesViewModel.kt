package self.tuan.hocmaians.ui.fragments.manage.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import self.tuan.hocmaians.data.entities.Course
import self.tuan.hocmaians.repositories.IRepository
import javax.inject.Inject

@HiltViewModel
class ManageCoursesViewModel @Inject constructor(
    repository: IRepository
) : ViewModel() {

    // live data from db for manage courses related fragments to observe
    val courses: LiveData<List<Course>> = repository.getAllCourses()
}
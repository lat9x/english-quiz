package self.tuan.hocmaians.ui.fragments.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import self.tuan.hocmaians.data.entities.Course
import self.tuan.hocmaians.data.entities.Question
import self.tuan.hocmaians.data.entities.Topic
import self.tuan.hocmaians.repositories.IRepository
import self.tuan.hocmaians.util.Event
import self.tuan.hocmaians.util.Resource
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    // variable for Bookmarks fragment
    var isFilterSectionOpen = false
    var chosenCourse: Course? = null
    var chosenTopic: Topic? = null

    // live data from db for BookmarksFragment to observe
    val courses: LiveData<List<Course>> = repository.getAllCourses()
    lateinit var topicsByCourse: LiveData<List<Topic>>
    lateinit var bookmarks: LiveData<List<Question>>

    // start filtering state for BookmarksFragment to observe
    private var _filter = MutableLiveData<Event<Resource<String>>>()
    val filter: LiveData<Event<Resource<String>>> = _filter

    /**
     * Get all bookmarks from the database
     */
    fun getAllBookmarks() {
        bookmarks = repository.getAllBookmarks()
    }

    /**
     * When user choose a course from spinner. Update chosen course, then get all topics belong to
     * that course
     *
     * @param course chosen course
     */
    fun onChooseCourse(course: Course) {
        chosenCourse = course
        getTopicsBasedOnCourse(course.id)
    }

    /**
     * When user choose a topic from spinner. Update chosen topic, then count all questions in that
     * topic
     *
     * @param topic chosen topic
     */
    fun onChooseTopic(topic: Topic) {
        chosenTopic = topic
    }

    fun onFilterBookmarks() {
        if (chosenCourse == null) {
            _filter.postValue(
                Event(
                    Resource.error(
                        msg = "You have not chosen any course yet",
                        data = null
                    )
                )
            )
            return
        }
        if (chosenTopic == null) {
            _filter.postValue(
                Event(
                    Resource.error(
                        msg = "You have not chosen any topic yet",
                        data = null
                    )
                )
            )
            return
        }

        _filter.postValue(
            Event(
                Resource.success(
                    data = "You can filter bookmarks"
                )
            )
        )
        bookmarks = getBookmarksByTopic(topicId = chosenTopic!!.id)
    }

    /* ---------------------------------- DB related methods ---------------------------------- */
    private fun getTopicsBasedOnCourse(courseId: Int) {
        topicsByCourse = repository.getTopicsBasedOnCourse(courseId = courseId)
    }

    private fun getBookmarksByTopic(topicId: Int): LiveData<List<Question>> =
        repository.getBookmarksBasedOnTopicId(topicId = topicId)

    fun updateQuestionBookmark(questionId: Long, bookmark: Int) =
        viewModelScope.launch {
            repository.updateQuestionBookmark(questionId = questionId, bookmark = bookmark)
        }
}
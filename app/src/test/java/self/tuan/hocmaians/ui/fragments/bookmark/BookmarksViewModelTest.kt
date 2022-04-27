package self.tuan.hocmaians.ui.fragments.bookmark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import self.tuan.hocmaians.MainCoroutineRule
import self.tuan.hocmaians.data.entities.Course
import self.tuan.hocmaians.data.entities.Topic
import self.tuan.hocmaians.getOrAwaitValueTest
import self.tuan.hocmaians.repositories.FakeRepository
import self.tuan.hocmaians.util.Status

@ExperimentalCoroutinesApi
class BookmarksViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BookmarksViewModel

    @Before
    fun setup() {
        viewModel = BookmarksViewModel(FakeRepository())
    }

    @Test
    fun `filter bookmarks by topic with null course, returns error`() {
        viewModel.onFilterBookmarks()

        val value = viewModel.filter.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `filter bookmarks by topic with null topic, returns error`() {
        viewModel.chosenCourse = Course(id = 1, name = "Q", priority = 0)
        viewModel.chosenTopic = null

        viewModel.onFilterBookmarks()

        val value = viewModel.filter.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `filter bookmarks by topic with valid requirements, returns success`() {
        viewModel.chosenCourse = Course(id = 1, name = "Q", priority = 0)
        viewModel.chosenTopic = Topic(
            id = 4, name = "t", priority = 0, isUserAdded = 0, courseId = 1
        )

        viewModel.onFilterBookmarks()

        val value = viewModel.filter.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}
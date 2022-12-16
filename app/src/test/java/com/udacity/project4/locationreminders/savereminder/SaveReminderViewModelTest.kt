package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.source.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    //TODO: provide testing to the SaveReminderView and its live data objects
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    private lateinit var fakeDataSource: FakeDataSource

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    @Before
    fun setUpViewModel() {
        stopKoin()
        fakeDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(), fakeDataSource
        )
    }

    @After
    fun stopDown() {
        stopKoin()
    }

    @Test
    fun reminder_saved() {
        val reminder = ReminderDataItem(
            "Reminder", "Description",
            "Location", (-360..360).random().toDouble(),
            (-360..360).random().toDouble(), "id"
        )
        saveReminderViewModel.saveReminder(reminder)
        assertThat(saveReminderViewModel.showToast.getOrAwaitValue(), `is`("Reminder Saved !"))
    }

    @Test
    fun save_Reminder_expect_description() {
        val reminder2 = ReminderDataItem(
            "title", "",
            "Location", (-360..360).random().toDouble(),
            (-360..360).random().toDouble(), "id"
        )
        saveReminderViewModel.validateAndSaveReminder(reminder2)
        assertThat(saveReminderViewModel.showToast.getOrAwaitValue(), notNullValue())
    }

    @Test
    fun validate_loading_LiveData() = runBlocking {
        //given reminder
        val reminder = ReminderDataItem(
            "Reminder", "",
            "Location", (-360..360).random().toDouble(),
            (-360..360).random().toDouble(), "id"
        )
        mainCoroutineRule.pauseDispatcher()
        //when -> save reminder
        saveReminderViewModel.validateAndSaveReminder(reminder)

        //then ->  expected loading  is true  display data of reminder
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        //then ->  expected loading  is false  disappeared
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }
}
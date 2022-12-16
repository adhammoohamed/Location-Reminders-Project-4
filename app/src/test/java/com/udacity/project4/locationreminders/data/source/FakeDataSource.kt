package com.udacity.project4.locationreminders.data.source

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
@Suppress("UNREACHABLE_CODE")
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    private var _returnError = false
    fun returnError(result: Boolean){
        _returnError = result

    }
//    TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (_returnError) {
            return Result.Error("There is Exception Error!")
        }
        reminders.let { return Result.Success(ArrayList(it)) }
        return Result.Error("Reminders not found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {

        if(_returnError) {
            return Result.Error("There is Exception Error!")
        }
        reminders?.find { it.id == id }?.let { return Result.Success(it) }
        return Result.Error("Not Found The Reminder Have Id = $id")
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}
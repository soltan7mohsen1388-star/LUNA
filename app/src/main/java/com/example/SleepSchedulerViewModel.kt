package com.example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

enum class Language {
    PERSIAN, ENGLISH
}

data class SimpleTime(val hour: Int, val minute: Int) {
    fun minusMinutes(mins: Int): SimpleTime {
        val totalMinutes = hour * 60 + minute - mins
        val positiveMinutes = ((totalMinutes % 1440) + 1440) % 1440
        return SimpleTime(positiveMinutes / 60, positiveMinutes % 60)
    }

    fun plusMinutes(mins: Int): SimpleTime {
        val totalMinutes = hour * 60 + minute + mins
        val positiveMinutes = ((totalMinutes % 1440) + 1440) % 1440
        return SimpleTime(positiveMinutes / 60, positiveMinutes % 60)
    }

    fun format(): String {
        return String.format("%02d:%02d", hour, minute)
    }
}

data class SleepOption(
    val cycles: Int,
    val time: SimpleTime,
    val durationHours: Double,
    val isOptimal: Boolean
)

class SleepSchedulerViewModel : ViewModel() {

    private val _language = MutableStateFlow(Language.PERSIAN)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _isWakeUpMode = MutableStateFlow(true)
    val isWakeUpMode: StateFlow<Boolean> = _isWakeUpMode.asStateFlow()

    private val _hour = MutableStateFlow(7)
    val hour: StateFlow<Int> = _hour.asStateFlow()

    private val _minute = MutableStateFlow(0)
    val minute: StateFlow<Int> = _minute.asStateFlow()

    private val _fallAsleepMinutes = MutableStateFlow(14)
    val fallAsleepMinutes: StateFlow<Int> = _fallAsleepMinutes.asStateFlow()

    private val _sleepOptions = MutableStateFlow<List<SleepOption>>(emptyList())
    val sleepOptions: StateFlow<List<SleepOption>> = _sleepOptions.asStateFlow()

    init {
        recalculate()
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    fun setWakeUpMode(isWakeUp: Boolean) {
        _isWakeUpMode.value = isWakeUp
        recalculate()
    }

    fun updateHour(newHour: Int) {
        val bounded = ((newHour % 24) + 24) % 24
        _hour.value = bounded
        recalculate()
    }

    fun updateMinute(newMinute: Int) {
        val bounded = ((newMinute % 60) + 60) % 60
        _minute.value = bounded
        recalculate()
    }

    fun updateFallAsleepMinutes(minutes: Int) {
        _fallAsleepMinutes.value = minutes.coerceIn(0, 60)
        recalculate()
    }

    fun sleepNow() {
        val calendar = Calendar.getInstance()
        _isWakeUpMode.value = false
        _hour.value = calendar.get(Calendar.HOUR_OF_DAY)
        _minute.value = calendar.get(Calendar.MINUTE)
        recalculate()
    }

    fun recalculate() {
        val currentHour = _hour.value
        val currentMinute = _minute.value
        val currentFallAsleep = _fallAsleepMinutes.value

        _sleepOptions.value = if (_isWakeUpMode.value) {
            // Wake Up Mode: Target wake-up time is set. Find when to go to sleep.
            val wakeUpTime = SimpleTime(currentHour, currentMinute)
            (3..6).map { cycles ->
                val totalMinutesToSubtract = (cycles * 90) + currentFallAsleep
                val bedtime = wakeUpTime.minusMinutes(totalMinutesToSubtract)
                SleepOption(
                    cycles = cycles,
                    time = bedtime,
                    durationHours = cycles * 1.5,
                    isOptimal = cycles == 5 || cycles == 6
                )
            }.reversed()
        } else {
            // Sleep Mode: Target bedtime is set. Find when to wake up.
            val bedtime = SimpleTime(currentHour, currentMinute)
            (3..6).map { cycles ->
                val totalMinutesToAdd = (cycles * 90) + currentFallAsleep
                val wakeUpTime = bedtime.plusMinutes(totalMinutesToAdd)
                SleepOption(
                    cycles = cycles,
                    time = wakeUpTime,
                    durationHours = cycles * 1.5,
                    isOptimal = cycles == 5 || cycles == 6
                )
            }.reversed()
        }
    }
}

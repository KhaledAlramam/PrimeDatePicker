package com.aminography.primedatepicker.sample.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.nav_drawer_calendar.view.*
import org.jetbrains.anko.toast
import java.util.*

class CalendarViewActivity : AppCompatActivity(), PrimeCalendarView.OnDayClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        openDrawer()

        var calendarType = CalendarType.CIVIL
        calendarView.onDayClickListener = this
        calendarView.calendarType = calendarType

        navigationView.getHeaderView(0)?.apply {

            civilRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.CIVIL
                    calendarView.calendarType = calendarType

                    restoreDefaults(this, calendarType)
                }
            }
            persianRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.PERSIAN
                    calendarView.calendarType = calendarType

                    restoreDefaults(this, calendarType)
                }
            }
            hijriRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.HIJRI
                    calendarView.calendarType = calendarType

                    restoreDefaults(this, calendarType)
                }
            }
            //--------------------------------------------------------------------------------------
            singleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.SINGLE
                    pickedTextView.text = ""
                }
            }
            startRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.START_RANGE
                    pickedTextView.text = ""
                }
            }
            endRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.END_RANGE
                    pickedTextView.text = ""
                }
            }
            //--------------------------------------------------------------------------------------
            minDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.add(Calendar.MONTH, -5)
                        calendarView.minDateCalendar = calendar
                        updatePickedText()
                    } else {
                        calendarView.minDateCalendar = null
                    }
                }
            }
            maxDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.add(Calendar.MONTH, 5)
                        calendarView.maxDateCalendar = calendar
                        updatePickedText()
                    } else {
                        calendarView.maxDateCalendar = null
                    }
                }
            }
            //--------------------------------------------------------------------------------------
            gotoPastTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.add(Calendar.MONTH, -7)
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
            gotoNowTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
            gotoFutureTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.add(Calendar.MONTH, 7)
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDayClick(calendarView: PrimeCalendarView, pickType: PickType, day: BaseCalendar) {
        updatePickedText()
    }

    private fun updatePickedText() {
        when (calendarView.pickType) {
            PickType.SINGLE -> {
                calendarView.pickedSingleDayCalendar?.apply {
                    pickedTextView.visibility = View.VISIBLE
                    pickedTextView.text = "Single Day: $longDateString"
                }
            }
            PickType.START_RANGE, PickType.END_RANGE -> {
                calendarView.pickedStartRangeCalendar?.let { start ->
                    pickedTextView.visibility = View.VISIBLE
                    var text = "Start Range Day: ${start.longDateString}"
                    calendarView.pickedEndRangeCalendar?.let { end ->
                        text += "\n"
                        text += "End Range Day: ${end.longDateString}"
                    }
                    pickedTextView.text = text
                }
            }
            PickType.NOTHING -> {
                pickedTextView.visibility = View.INVISIBLE
            }
        }
    }

    private fun restoreDefaults(view: View, calendarType: CalendarType) {
        pickedTextView.visibility = View.INVISIBLE
        pickedTextView.text = ""
        with(view) {
            minDateCheckBox.isChecked = false
            maxDateCheckBox.isChecked = false
            singleRadioButton.isChecked = false
            startRangeRadioButton.isChecked = false
            endRangeRadioButton.isChecked = false
            val today = CalendarFactory.newInstance(calendarType)
            minDateCheckBox.text = "Min Date: ${today.monthName} 5"
            maxDateCheckBox.text = "Max Date: ${today.monthName} 25"

            calendarView.pickedSingleDayCalendar = null
            calendarView.pickedStartRangeCalendar = null
            calendarView.pickedEndRangeCalendar = null
            calendarView.pickType = PickType.NOTHING
            calendarView.minDateCalendar = null
            calendarView.maxDateCalendar = null
        }
    }

    private fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)

    private fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.START)

}

package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.ui.home.HomeViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener{

    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var addCourseViewModel: AddCourseViewModel

    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.day)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDay.adapter = arrayAdapter

        val factory = HomeViewModelFactory.createFactory(this)
        addCourseViewModel = ViewModelProvider( this, factory).get(AddCourseViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                if (binding.addCourseName.text.toString().isEmpty()) {
                    binding.addCourseName.error = "Please fill the Course Name"
                }else if (startTime.isEmpty()) {
                    binding.tvStartTime.error = "Please fill the Start Time"
                }else if (endTime.isEmpty()) {
                    binding.tvEndTime.error = "Please fill the End Time"
                }else if (binding.addLecturerTitle.text.toString().isEmpty()) {
                    binding.addLecturerTitle.error = "Please fill the Lecturer"
                }else if (binding.addNote.text.toString().isEmpty()) {
                    binding.addNote.error = "Please fill the Lecturer"
                }else {
                    addCourseViewModel.insertCourse(binding.addCourseName.text.toString(), binding.spinnerDay.selectedItemPosition, startTime, endTime, binding.addLecturerTitle.text.toString(), binding.addNote.text.toString())
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun startTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "startTimePicker")
    }

    fun endTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "endTimePicker")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        if (tag == "startTimePicker") {
            val min = String.format("%02d", minute)
            binding.tvStartTime.text = "Start Time\n${hour}:${min}"
            startTime = "${hour}:${min}"
        }
        if (tag == "endTimePicker") {
            val min = String.format("%02d", minute)
            binding.tvEndTime.text = "End Time\n${hour}:${min}"
            endTime = "${hour}:${min}"
        }
    }
}
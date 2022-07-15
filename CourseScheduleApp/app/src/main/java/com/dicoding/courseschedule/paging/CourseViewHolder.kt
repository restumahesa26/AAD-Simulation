package com.dicoding.courseschedule.paging

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.util.DayName

class CourseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private lateinit var course: Course

    // TODO 7 : Complete ViewHolder to show item
    fun bind(course: Course, clickListener: (Course) -> Unit) {
        this.course = course

        val tvCourse: TextView = itemView.findViewById(R.id.tv_course)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvLecture: TextView = itemView.findViewById(R.id.tv_lecturer)

        course.apply {
            tvCourse.text = courseName
            tvTime.text = "${DayName.getByNumber(day)}, $startTime - $endTime"
            tvLecture.text = lecturer
        }

        itemView.setOnClickListener {
            clickListener(course)
        }
    }

    fun getCourse(): Course = course
}
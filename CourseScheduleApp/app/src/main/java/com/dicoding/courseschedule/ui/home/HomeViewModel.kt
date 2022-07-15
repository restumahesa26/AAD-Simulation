package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(private val repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()

    private lateinit var nearestCourse: LiveData<Course?>

    init {
        _queryType.value = QueryType.NEXT_DAY
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    fun setNearestCourse() {
        nearestCourse = Transformations.switchMap(_queryType) {
            repository.getNearestSchedule(it)
        }
    }

    fun getNearestCourse(): LiveData<Course?> {
        return nearestCourse
    }
}

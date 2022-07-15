package com.dicoding.todoapp.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.databinding.ActivityTaskDetailBinding
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.Constants.Companion.TASK_ID
import com.dicoding.todoapp.utils.DateConverter

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskModel: DetailTaskViewModel
    private lateinit var binding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO 11 : Show detail task and implement delete action

        val factory = ViewModelFactory.getInstance(this)
        detailTaskModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        val taskId = intent.getIntExtra(TASK_ID, 0)
        detailTaskModel.setTaskId(taskId)
        detailTaskModel.task.observe(this) {
            if (it != null) {
                binding.detailEdTitle.setText(it.title)
                binding.detailEdDescription.setText(it.description)
                binding.detailEdDueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))
            }
        }
        binding.btnDeleteTask.setOnClickListener {
            detailTaskModel.deleteTask()
            val intent = Intent(this@DetailTaskActivity, TaskActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
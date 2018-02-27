package suwashizmu.org.roomsample.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.tasks_act.*
import suwashizmu.org.roomsample.R

class TaskDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_details_act)
        setSupportActionBar(toolbar)

        Logger.addLogAdapter(AndroidLogAdapter())

//        val db = Room.databaseBuilder(applicationContext,
//                AppDatabase::class.java, "task-room.db")
//                .build()

//        val repository = TasksLocalDataSource(db.taskDao(), db.getTreePathDao())
//        val tasksViewModel = TasksViewModel(repository)

        val fragment: TaskDetailsFragment = supportFragmentManager.findFragmentById(R.id.fragment) as TaskDetailsFragment
//        fragment.tasksViewModel = tasksViewModel

    }

}

package suwashizmu.org.roomsample.tasks

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.tasks_act.*
import suwashizmu.org.roomsample.R
import suwashizmu.org.roomsample.data.source.local.AppDatabase
import suwashizmu.org.roomsample.data.source.local.TasksLocalDataSource

class TasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)
        setSupportActionBar(toolbar)

        Logger.addLogAdapter(AndroidLogAdapter())

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "task-room.db")
                .build()

        val repository = TasksLocalDataSource(db.taskDao(), db.getTreePathDao())
        val tasksViewModel = TasksViewModel(repository)

        check(supportFragmentManager.findFragmentById(R.id.fragment) as? TasksFragment != null)

        val fragment: TasksFragment = supportFragmentManager.findFragmentById(R.id.fragment) as TasksFragment
        fragment.tasksViewModel = tasksViewModel

    }

}

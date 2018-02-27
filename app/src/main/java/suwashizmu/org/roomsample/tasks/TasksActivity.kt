package suwashizmu.org.roomsample.tasks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import kotlinx.android.synthetic.main.tasks_act.*
import suwashizmu.org.roomsample.Navigator
import suwashizmu.org.roomsample.R
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.source.TasksRepository
import suwashizmu.org.roomsample.details.TaskDetailsActivity

class TasksActivity : AppCompatActivity(), Navigator {

    companion object {
        private const val REQUEST_DETAILS = 1
    }

    private val kodein = LazyKodein(appKodein)

    //propertyにもたせたい場合
//    private val dataSource: TasksLocalDataSource by kodein.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)
        setSupportActionBar(toolbar)

        val repository: TasksRepository = kodein().instance()
        val tasksViewModel = TasksViewModel(repository, this)

        check(supportFragmentManager.findFragmentById(R.id.fragment) as? TasksFragment != null)

        val fragment: TasksFragment = supportFragmentManager.findFragmentById(R.id.fragment) as TasksFragment
        fragment.tasksViewModel = tasksViewModel

    }

    override fun gotoDetails(task: Task?) {
        val intent = Intent(this, TaskDetailsActivity::class.java)
        startActivityForResult(intent, REQUEST_DETAILS)
    }
}

package suwashizmu.org.roomsample.details

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

class TaskDetailsActivity : AppCompatActivity(), Navigator {

    private val kodein = LazyKodein(appKodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_details_act)
        setSupportActionBar(toolbar)

        val repository: TasksRepository = kodein().instance()

        val fragment: TaskDetailsFragment = supportFragmentManager.findFragmentById(R.id.fragment) as TaskDetailsFragment
        fragment.taskDetailsViewModel = TaskDetailsViewModel(repository, this)

    }

    override fun gotoDetails(task: Task?) {
        //コールされない
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

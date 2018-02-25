package suwashizmu.org.roomsample

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.main_act.*
import suwashizmu.org.roomsample.model.AppDatabase
import suwashizmu.org.roomsample.model.entity.Task
import java.util.*

class MainActActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            Thread({
                db.taskDao().insertAll(Task(0, Date().toString()), Task(0, Date().toString()))
            }).start()
        }

        db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "task-room.db")
                .build()


        Thread({
            val list = db.taskDao().getAll()
            list.forEach { Log.d("TAG", it.toString()) }
        }).start()

    }

}

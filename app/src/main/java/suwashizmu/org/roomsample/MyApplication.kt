package suwashizmu.org.roomsample

import android.app.Application
import android.arch.persistence.room.Room
import com.github.salomonbrys.kodein.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import suwashizmu.org.roomsample.data.source.TasksRepository
import suwashizmu.org.roomsample.data.source.local.AppDatabase
import suwashizmu.org.roomsample.data.source.local.TasksLocalDataSource

/**
 * Created by KEKE on 2018/02/27.
 */
class MyApplication : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
    }

    override val kodein by Kodein.lazy {
        /* bindings */

        bind<AppDatabase>() with eagerSingleton {
            val db = Room.databaseBuilder(this@MyApplication,
                    AppDatabase::class.java, "task-room.db")
                    .build()
            db
        }

        bind<TasksRepository>() with singleton {
            TasksLocalDataSource(instance())
        }
    }


}
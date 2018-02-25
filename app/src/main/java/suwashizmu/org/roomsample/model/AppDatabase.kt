package suwashizmu.org.roomsample.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import suwashizmu.org.roomsample.model.entity.Task

/**
 * Created by KEKE on 2018/02/24.
 */
@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
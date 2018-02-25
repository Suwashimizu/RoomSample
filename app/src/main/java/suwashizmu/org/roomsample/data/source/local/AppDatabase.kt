package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.TreePaths

/**
 * Created by KEKE on 2018/02/24.
 */
@Database(entities = [Task::class, TreePaths::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun getTreePathDao(): TreePathsDao
}
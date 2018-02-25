package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import suwashizmu.org.roomsample.data.Task

/**
 * Created by KEKE on 2018/02/25.
 */
@RunWith(AndroidJUnit4::class)
class TaskEntityReadWriteTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: AppDatabase

    private val firstSummary = "data 1"

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        taskDao = db.taskDao()
    }

    @After
    fun finish() {
        db.close()
    }

    @Test
    fun writeTaskAndReadInList() {
        val task = Task(summary = firstSummary)
        taskDao.insertAll(task)

        val list = taskDao.getAll().test()

        list.assertNoErrors()
        list.assertValue { it.size == 1 }
        list.assertValue { it.first().summary == firstSummary }

    }

    @Test
    fun writeTaskAndDelete() {
        val task = Task(summary = firstSummary)
        taskDao.insertAll(task)

        val _task = taskDao.getAll().test().values().first().first()

        taskDao.delete(_task)


        val list = taskDao.getAll().test()

        list.assertNoErrors()
        list.assertValue { it.isEmpty() }
    }
}
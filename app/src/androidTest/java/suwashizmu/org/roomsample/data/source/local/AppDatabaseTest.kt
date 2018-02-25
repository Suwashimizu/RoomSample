package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.TreePaths

/**
 * Created by KEKE on 2018/02/25.
 */
@RunWith(AndroidJUnit4::class)
class TaskEntityReadWriteTest {

    private lateinit var taskDao: TaskDao
    private lateinit var treeDao: TreePathsDao
    private lateinit var db: AppDatabase

    private val firstSummary = "data 1"

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        taskDao = db.taskDao()
        treeDao = db.getTreePathDao()
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

        val id = list.values().first().first().uid

        treeDao.insertAll(TreePaths(id, id))

        val tree = treeDao.getAll().test()

        tree.assertNoErrors()
        tree.assertValue { it.size == 1 }
        tree.assertValue { it.first().ancestor == 1L && it.first().descendant == 1L }
    }

    @Test
    fun writeTaskAndDelete() {
        val task = Task(summary = firstSummary)
        taskDao.insertAll(task)

        val _task = taskDao.getAll().test().values().first().first()
        val id = _task.uid

        treeDao.insertAll(TreePaths(id, id))

        taskDao.delete(_task)

        val list = taskDao.getAll().test()

        list.assertNoErrors()
        list.assertValue { it.isEmpty() }

        val tree = treeDao.getAll().test()

        //参照先が消えるとTreePathも消える
        tree.assertNoErrors()
        tree.assertValue { it.isEmpty() }

    }
}
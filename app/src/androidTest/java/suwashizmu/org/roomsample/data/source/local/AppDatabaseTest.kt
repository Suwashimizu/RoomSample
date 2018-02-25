package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import junit.framework.Assert.assertEquals
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

        Logger.addLogAdapter(AndroidLogAdapter())
    }

    @After
    fun finish() {
        db.close()
    }

    @Test
    fun writeTaskAndReadInList() {
        val task = Task(summary = firstSummary)
        val ids = taskDao.insertAll(task)

        val list = taskDao.getAll().test()

        list.assertNoErrors()
        list.assertValue { it.size == 1 }
        list.assertValue { it.first().summary == firstSummary }

        treeDao.insertAll(TreePaths(ids[0], ids[0]))

        val tree = treeDao.getAll().test()

        tree.assertNoErrors()
        tree.assertValue { it.size == 1 }
        tree.assertValue { it.first().ancestor == 1L && it.first().descendant == 1L }
    }

    @Test
    fun writeTaskAndDelete() {
        val task = Task(summary = firstSummary)
        val ids = taskDao.insertAll(task)

        val _task = taskDao.getAll().test().values().first().first()

        treeDao.insertAll(TreePaths(ids[0], ids[0]))

        taskDao.delete(_task)

        val list = taskDao.getAll().test()

        list.assertNoErrors()
        list.assertValue { it.isEmpty() }

        val tree = treeDao.getAll().test()

        //参照先が消えるとTreePathも消える
        tree.assertNoErrors()
        tree.assertValue { it.isEmpty() }

    }

    @Test
    fun writeTestAtMultiple() {
        val task = Task(summary = firstSummary)
        val id1 = taskDao.insert(task)
        val id2 = taskDao.insert(task)

        assertEquals(id1, 1)
        assertEquals(id2, 2)
    }

    @Test
    fun findAncestor() {

        val id1 = taskDao.insert(Task(summary = "task1"))
        val id2 = taskDao.insert(Task(summary = "task2"))
        val id3 = taskDao.insert(Task(summary = "task3"))

        treeDao.insertAll(
                TreePaths(id1, id1),

                TreePaths(id2, id2),
                TreePaths(id1, id2),

                TreePaths(id3, id3),
                TreePaths(id1, id3))

        val ancestorOfTask1Tree = treeDao.findAncestorById(id1).test()

        ancestorOfTask1Tree.assertNoErrors()
        ancestorOfTask1Tree.assertValue { it.size == 3 }


        val ancestorOfTask2Tree = treeDao.findAncestorById(id2).test()

        ancestorOfTask2Tree.assertNoErrors()
        ancestorOfTask2Tree.assertValue { it.size == 1 }

        val descendantOfTask2Tree = treeDao.findDescendantById(id2).test()

        descendantOfTask2Tree.assertNoErrors()
        descendantOfTask2Tree.assertValue {
            Logger.d(it)
            it.size == 2
        }
        descendantOfTask2Tree.assertValue { it[0].summary == "task2" && it[1].summary == "task1" }
    }
}
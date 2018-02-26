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

        treeDao.insertAll(TreePaths(ids[0], ids[0], 0))

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

        treeDao.insertAll(TreePaths(ids[0], ids[0], 0))

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
                TreePaths(id1, id1, 0),

                TreePaths(id2, id2, 0),
                TreePaths(id1, id2, 1),

                TreePaths(id3, id3, 0),
                TreePaths(id1, id3, 2))

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


    @Test
    fun getRootItemIsTwo() {

/*
task1
├── task2
│   └── task4
└── task3
    └── task5
task6
*/

        val id1 = taskDao.insert(Task(summary = "task1"))
        val id2 = taskDao.insert(Task(summary = "task2"))
        val id3 = taskDao.insert(Task(summary = "task3"))
        val id4 = taskDao.insert(Task(summary = "task4"))
        val id5 = taskDao.insert(Task(summary = "task5"))

        treeDao.insertAll(TreePaths(id1, id1, 0))

        //insert to task2
        treeDao.insertAll(TreePaths(id2, id2, 0))
        treeDao.insertAll(TreePaths(id1, id2, 1))

        //insert to task4
        treeDao.insertAll(TreePaths(id4, id4, 0), TreePaths(id1, id4, 2), TreePaths(id2, id4, 1))

        //insert to task3
        treeDao.insertAll(TreePaths(id3, id3, 0), TreePaths(id1, id3, 1))

        //insert to task5
        treeDao.insertAll(TreePaths(id5, id5, 0), TreePaths(id3, id5, 1), TreePaths(id1, id5, 2))

        val id6 = taskDao.insert(Task(summary = "task6"))
        treeDao.insertAll(TreePaths(id6, id6, 0))

        val root = treeDao.getRoots().test()

        root.assertValue { it.size == 2 }
        root.assertValue { it[0].summary == "task1" }
        root.assertValue { it[1].summary == "task6" }

    }

    @Test
    fun getCount() {

/*
task1
├── task2
│   └── task4
└── task3
    └── task5
*/

        val id1 = taskDao.insert(Task(summary = "task1"))
        val id2 = taskDao.insert(Task(summary = "task2"))
        val id3 = taskDao.insert(Task(summary = "task3"))
        val id4 = taskDao.insert(Task(summary = "task4"))
        val id5 = taskDao.insert(Task(summary = "task5"))


        treeDao.insertAll(TreePaths(id1, id1, 0))

        //insert to task2
        treeDao.insertAll(TreePaths(id2, id2, 0))
        treeDao.insertAll(TreePaths(id1, id2, 1))

        //insert to task4
        treeDao.insertAll(TreePaths(id4, id4, 0), TreePaths(id1, id4, 2), TreePaths(id2, id4, 1))

        //insert to task3
        treeDao.insertAll(TreePaths(id3, id3, 0), TreePaths(id1, id3, 1))

        //insert to task5
        treeDao.insertAll(TreePaths(id5, id5, 0), TreePaths(id3, id5, 1), TreePaths(id1, id5, 2))

        val treePaths = treeDao.getCount().test()

        treePaths.assertValue { it == 11 }
    }

    @Test
    fun insertAtBetweenTask1Task2() {

/*
task1
├── task2
│   └── task4
└── task3
    └── task5
*/

        val id1 = taskDao.insert(Task(summary = "task1"))
        val id2 = taskDao.insert(Task(summary = "task2"))
        val id3 = taskDao.insert(Task(summary = "task3"))
        val id4 = taskDao.insert(Task(summary = "task4"))
        val id5 = taskDao.insert(Task(summary = "task5"))


        treeDao.insertAll(TreePaths(id1, id1, 0))

        //insert to task2
        treeDao.insertAll(TreePaths(id2, id2, 0))
        treeDao.insertAll(TreePaths(id1, id2, 1))

        //insert to task4
        treeDao.insertAll(TreePaths(id4, id4, 0), TreePaths(id1, id4, 2), TreePaths(id2, id4, 1))

        //insert to task3
        treeDao.insertAll(TreePaths(id3, id3, 0), TreePaths(id1, id3, 1))

        //insert to task5
        treeDao.insertAll(TreePaths(id5, id5, 0), TreePaths(id3, id5, 1), TreePaths(id1, id5, 2))

        val tasks = treeDao.findAncestorById(id1).test()

        tasks.assertValue { it.size == 5 }

        val treePaths = treeDao.findTreePathsById(id1).test()

        treePaths.assertValue { it.size == 5 }

//
//        val id2 = taskDao.insert(Task(summary = "task2"))
//        treeDao.insertAll(
//                TreePaths(id2, id2),
//
//                TreePaths(id1, id2))
//
//        val id3 = taskDao.insert(Task(summary = "task3"))
//
//        treeDao.insert(id1, id2, )

    }
}
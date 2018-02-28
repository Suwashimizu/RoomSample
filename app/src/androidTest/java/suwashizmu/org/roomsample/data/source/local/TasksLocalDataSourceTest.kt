package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.error.ValueEmptyException

/**
 * Created by KEKE on 2018/02/28.
 */
@RunWith(AndroidJUnit4::class)
class TasksLocalDataSourceTest {

    private lateinit var repository: TasksLocalDataSource
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        repository = TasksLocalDataSource(db)
    }

    @After
    fun finish() {
        db.close()
    }


    @Test
    fun write_task_and_read_in_list() {
        val task = Task(summary = "data 1")
        val ids = repository.insertRoot(task).test()

        val list = repository.getAll().test()

        list.assertNoErrors()
        list.assertValue { it.size == 1 }
        list.assertValue { it.first().summary == "data 1" }

        val id = ids.values().first()

        val loadedTask = repository.loadAllByIds(id).test()
        loadedTask.assertNoErrors()
        loadedTask.assertValue { it.size == 1 }
        loadedTask.assertValue { it.first().summary == "data 1" }


        val tree = repository.loadAllByIds(id)
//
//        tree.assertNoErrors()
//        tree.assertValue { it.size == 1 }
//        tree.assertValue { it.first().ancestor == 1L && it.first().descendant == 1L }
    }

    @Test
    fun should_be_summary_is_empty_to_exception() {

        val task = Task(summary = "")
        val ids = repository.insertRoot(task).test()

        ids.assertError(ValueEmptyException::class.java)
    }

    @Test
    fun write_children_and_read() {

/*
task1
└── task2
*/

        val task = Task(summary = "data 1")
        var ids = repository.insertRoot(task).test()

        val id = ids.values().first()

        val task2 = Task(summary = "data 2")

        ids = repository.insert(task2, id).test()
        ids.assertNoErrors()
        val id2 = ids.values().first()

        val loadedTask = repository.loadAllByIds(id2).test()
        loadedTask.assertValue { it.first().summary == "data 2" }

        val treePaths = repository.getTree().test()
        treePaths.assertNoErrors()
        treePaths.assertValue { it.size == 3 }

    }

    @Test
    fun write_grandchild_and_read() {

        val task = Task(summary = "data 1")
        var ids = repository.insertRoot(task).test()

        val id1 = ids.values().first()

        val task2 = Task(summary = "data 2")

        ids = repository.insert(task2, id1).test()
        val id2 = ids.values().first()

        val task3 = Task(summary = "data 3")
        ids = repository.insert(task3, id2).test()
        val id3 = ids.values().first()

/*ここまでの状態
task1
└── task2
    └── task3
*/

        val loadedTask = repository.loadAllByIds(id3).test()
        loadedTask.assertValue { it.first().summary == "data 3" }

        //自己参照も含めてノードは6
        var treePaths = repository.getTree().test()
        treePaths.assertNoErrors()
        treePaths.assertValue { it.size == 6 }

        val task4 = Task(summary = "data 4")
        repository.insert(task4, id3).test()

/*
task1
└── task2
    └── task3
        └── task4
*/
        //自己参照も含めてノードは10
        treePaths = repository.getTree().test()
        treePaths.assertNoErrors()
        treePaths.assertValue { it.size == 10 }


/*
task1
├── task2
│    └── task3
│        └── task4
└-- task5
*/
        val task5 = Task(summary = "data 5")
        repository.insert(task5, id1).test()

        treePaths = repository.getTree().test()
        treePaths.assertNoErrors()
        treePaths.assertValue { it.size == 12 }


    }
}
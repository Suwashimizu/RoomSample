package suwashizmu.org.roomsample.data.source.local

import io.reactivex.Observable
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.source.TasksRepository

/**
 * Created by KEKE on 2018/02/25.
 */
class TasksLocalDataSource(private val taskDao: TaskDao) : TasksRepository {

    override fun getAll(): Observable<List<Task>> {

        return taskDao.getAll().toObservable()
    }


    override fun loadAllByIds(ids: List<Int>) = taskDao.loadAllByIds(ids)

    override fun findBySummary(summary: String) = taskDao.findBySummary(summary)

    override fun insertAll(vararg tasks: Task) {
        taskDao.insertAll(*tasks)
    }

    override fun delete(task: Task) = taskDao.delete(task)
}
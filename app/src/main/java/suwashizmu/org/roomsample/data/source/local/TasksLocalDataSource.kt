package suwashizmu.org.roomsample.data.source.local

import io.reactivex.Observable
import io.reactivex.Single
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.TreePaths
import suwashizmu.org.roomsample.data.source.TasksRepository
import suwashizmu.org.roomsample.error.ValueEmptyException

/**
 * Created by KEKE on 2018/02/25.
 */
class TasksLocalDataSource(private val taskDao: TaskDao, private val treePathsDao: TreePathsDao) : TasksRepository {

    override fun getAll(): Observable<List<Task>> {

        return taskDao.getAll().toObservable()
    }

    override fun loadAllByIds(vararg ids: Long): Single<List<Task>> = taskDao.loadAllByIds(*ids)

    override fun findBySummary(summary: String) = taskDao.findBySummary(summary)

    override fun insert(task: Task, ancestor: Long?, descendant: Long?): Single<Long> =
            Single.create<Long> {

                try {
                    //空ならError
                    if (task.summary.isBlank()) throw ValueEmptyException()

                    val id = taskDao.insert(task)
                    //自身のツリーを追加する
                    val myId = treePathsDao.insertAll(TreePaths(id, id, 0)).first()

                    if (ancestor != null && descendant != null) {
                        //TODO Lengthの計算が必要
                        treePathsDao.insertAll(TreePaths(ancestor, descendant, 1))
                    } else if (ancestor != null) {
                        treePathsDao.insertAll(TreePaths(ancestor, myId, 1))
                    }

                    it.onSuccess(id)

                } catch (e: Exception) {
                    it.onError(e)
                }
            }

    override fun insertAll(vararg tasks: Task): Single<List<Long>> {
        return Single.just(taskDao.insertAll(*tasks))
    }

    override fun delete(task: Task) = taskDao.delete(task)

    override fun getAllTree(): Single<List<TreePaths>> = treePathsDao.getAll()
}
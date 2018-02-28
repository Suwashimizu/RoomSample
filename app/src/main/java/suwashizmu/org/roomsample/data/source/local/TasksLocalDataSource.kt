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
class TasksLocalDataSource(private val db: AppDatabase) : TasksRepository {

    private val taskDao: TaskDao = db.taskDao()
    private val treePathsDao: TreePathsDao = db.treePathDao()

    override fun getAll(): Observable<List<Task>> {

        return taskDao.getAll().toObservable()
    }

    override fun loadAllByIds(vararg ids: Long): Single<List<Task>> = taskDao.loadAllByIds(*ids)

    override fun findBySummary(summary: String) = taskDao.findBySummary(summary)

    override fun insertRoot(task: Task): Single<Long> =
            Single.create<Long> {

                try {
                    //空ならError
                    if (task.summary.isBlank()) throw ValueEmptyException()

                    val id = taskDao.insert(task)
                    //自身のツリーを追加する
                    treePathsDao.insertAll(TreePaths(id, id, 0)).first()

                    it.onSuccess(id)

                } catch (e: Exception) {
                    it.onError(e)
                }
            }

    override fun insert(task: Task, ancestor: Long, descendant: Long?): Single<Long> =

            Single.create<Long> {
                try {
                    //空ならError
                    if (task.summary.isBlank()) throw ValueEmptyException()

                    val id = taskDao.insert(task)
                    //自身のツリーを追加する
                    treePathsDao.insertAll(TreePaths(id, id, 0)).first()

                    it.onSuccess(id)

                } catch (e: Exception) {
                    it.onError(e)
                }
            }
                    .flatMap { id ->
                        //祖先の取得
                        treePathsDao.findAncestorByDescendantId(ancestor).map { Pair(id, it) }
                    }
                    .map { idAndTasks ->
                        treePathsDao.insertAll(*idAndTasks.second.map { TreePaths(it.uid, idAndTasks.first, 0) }.toTypedArray())
                        idAndTasks.first
                    }

    override fun insertAll(vararg tasks: Task): Single<List<Long>> {
        return Single.just(taskDao.insertAll(*tasks))
    }

    override fun delete(task: Task) = taskDao.delete(task)

    override fun getTree(): Single<List<TreePaths>> = treePathsDao.getAll()
}
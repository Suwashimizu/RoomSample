package suwashizmu.org.roomsample.data.source

import io.reactivex.Observable
import io.reactivex.Single
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.TreePaths

/**
 * Created by KEKE on 2018/02/25.
 */
interface TasksRepository {

    fun getAll(): Observable<List<Task>>

    fun loadAllByIds(vararg ids: Long): Single<List<Task>>

    fun findBySummary(summary: String): Single<List<Task>>

    fun insert(task: Task, ancestor: Long? = null, descendant: Long? = null): Single<Long>

    fun insertAll(vararg tasks: Task): Single<List<Long>>

    fun delete(task: Task)

    fun getAllTree(): Single<List<TreePaths>>
}
package suwashizmu.org.roomsample.data.source

import io.reactivex.Observable
import io.reactivex.Single
import suwashizmu.org.roomsample.data.Task

/**
 * Created by KEKE on 2018/02/25.
 */
interface TasksRepository {

    fun getAll(): Observable<List<Task>>

    fun loadAllByIds(ids: List<Int>): Single<List<Task>>

    fun findBySummary(summary: String): Single<List<Task>>

    fun insert(task: Task, ancestor: Long? = null, descendant: Long? = null): Long

    fun insertAll(vararg tasks: Task): Single<List<Long>>

    fun delete(task: Task)
}
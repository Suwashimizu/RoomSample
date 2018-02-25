package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single
import suwashizmu.org.roomsample.data.Task

/**
 * Created by KEKE on 2018/02/24.
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): Single<List<Task>>

    @Query("SELECT * FROM task WHERE uid IN (:ids)")
    fun loadAllByIds(ids: List<Int>): Single<List<Task>>

    @Query("SELECT * FROM task WHERE summary LIKE :summary")
    fun findBySummary(summary: String): Single<List<Task>>

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)
}
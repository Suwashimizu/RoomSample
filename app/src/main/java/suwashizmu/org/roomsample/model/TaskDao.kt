package suwashizmu.org.roomsample.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import suwashizmu.org.roomsample.model.entity.Task

/**
 * Created by KEKE on 2018/02/24.
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE uid IN (:ids)")
    fun loadAllByIds(ids: List<Int>): List<Task>

    @Query("SELECT * FROM task WHERE summary LIKE :summary")
    fun findBySummary(summary: String): List<Task>

    @Insert
    fun insertAll(vararg tasks: Task)

    @Insert
    fun delete(task: Task)
}
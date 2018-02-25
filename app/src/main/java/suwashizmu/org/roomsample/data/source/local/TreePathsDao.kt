package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single
import suwashizmu.org.roomsample.data.TreePaths

/**
 * Created by KEKE on 2018/02/25.
 */
@Dao
interface TreePathsDao {

    @Query("SELECT * FROM treePaths")
    fun getAll(): Single<List<TreePaths>>

    @Insert
    fun insertAll(vararg paths: TreePaths)
}
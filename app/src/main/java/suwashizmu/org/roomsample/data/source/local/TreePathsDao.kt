package suwashizmu.org.roomsample.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.TreePaths

/**
 * Created by KEKE on 2018/02/25.
 */
@Dao
interface TreePathsDao {

    //親がuidのTaskを抽出する,joinする時は子のuid行う
    @Query("SELECT * FROM task INNER JOIN treePaths ON task.uid = treePaths.descendant WHERE treePaths.ancestor=:uid")
    fun findAncestorById(uid: Long): Single<List<Task>>

    //子がuidのTaskを抽出する,joinする時は親のuid行う
    @Query("SELECT * FROM task INNER JOIN treePaths ON task.uid = treePaths.ancestor WHERE treePaths.descendant=:uid")
    fun findDescendantById(uid: Long): Single<List<Task>>

    @Query("SELECT * FROM treePaths")
    fun getAll(): Single<List<TreePaths>>

    //子孫でグループしカウントが１のものをrootとして抽出する
    @Query("SELECT *,COUNT(*) as cnt FROM task INNER JOIN treePaths ON task.uid = treePaths.ancestor GROUP BY descendant HAVING cnt = 1")
    fun getRoots(): Single<List<Task>>

    @Query("SELECT COUNT(*) FROM treePaths")
    fun getCount(): Single<Int>

    @Insert
    fun insertAll(vararg paths: TreePaths)

    //UNION ALL使えない!
//    @Insert("INSERT INTO treePaths(ancestor,descendant) SELECT t.ancestor,:ancestor FROM treePaths AS t WHERE t.descendant = :descendant UNION ALL SELECT :ancestor,:ancestor")
//    fun insert(ancestor: Long, descendant: Long, task: Task)
}
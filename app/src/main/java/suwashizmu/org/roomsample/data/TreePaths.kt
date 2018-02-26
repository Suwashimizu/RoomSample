package suwashizmu.org.roomsample.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE

/**
 * Created by KEKE on 2018/02/25.
 */
@Entity(foreignKeys = [
    ForeignKey(
            entity = Task::class,
            parentColumns = ["uid"],
            childColumns = ["ancestor"],
            onDelete = CASCADE),
    ForeignKey(
            entity = Task::class,
            parentColumns = ["uid"],
            childColumns = ["descendant"],
            onDelete = CASCADE)],
        primaryKeys = ["ancestor", "descendant"])
data class TreePaths(
        val ancestor: Long,
        val descendant: Long,
        val pathLength: Int
)
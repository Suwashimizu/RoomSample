package suwashizmu.org.roomsample.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by KEKE on 2018/02/24.
 */
@Entity
data class Task(
        @PrimaryKey(autoGenerate = true)
        val uid: Long = 0,

        @ColumnInfo(name = "summary")
        val summary: String
)


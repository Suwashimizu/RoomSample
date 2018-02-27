package suwashizmu.org.roomsample

import suwashizmu.org.roomsample.data.Task

/**
 * Created by KEKE on 2018/02/28.
 */
interface Navigator {

    fun finish()

    fun gotoDetails(task: Task?)
}
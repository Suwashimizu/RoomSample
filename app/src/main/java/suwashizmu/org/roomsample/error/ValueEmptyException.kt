package suwashizmu.org.roomsample.error

import java.lang.IllegalStateException

/**
 * Created by KEKE on 2018/02/28.
 */
class ValueEmptyException(msg: String = "must not empty to summary") : IllegalStateException(msg)
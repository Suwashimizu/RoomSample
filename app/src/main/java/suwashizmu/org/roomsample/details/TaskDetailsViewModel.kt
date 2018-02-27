package suwashizmu.org.roomsample.details

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import suwashizmu.org.roomsample.Navigator
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.source.TasksRepository
import suwashizmu.org.roomsample.error.ValueEmptyException

/**
 * Created by KEKE on 2018/02/28.
 */
class TaskDetailsViewModel(private val tasksRepository: TasksRepository,
                           private val navigator: Navigator) : BaseObservable() {

    val errorMessage: ObservableField<String?> = ObservableField()

    @Bindable
    var summary: String = ""

    fun onSaveButtonClick() {

        tasksRepository.insert(Task(summary = summary))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            //success
                            navigator.finish()
                        },
                        {
                            it.printStackTrace()

                            val preMsg = errorMessage.get()
                            val newMsg = when (it) {
                                is ValueEmptyException -> "入力して下さい"
                                else -> "エラーです"
                            }

                            //同じobjでも通知を行う処理
                            if (preMsg != newMsg) {
                                errorMessage.set(newMsg)
                            } else {
                                errorMessage.notifyChange()
                            }
                        })
    }

    fun onBackbuttonClick() {
        navigator.finish()
    }
}
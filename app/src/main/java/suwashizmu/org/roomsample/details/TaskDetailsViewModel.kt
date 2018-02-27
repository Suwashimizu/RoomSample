package suwashizmu.org.roomsample.details

import android.databinding.BaseObservable
import android.databinding.Bindable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import suwashizmu.org.roomsample.Navigator
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.source.TasksRepository

/**
 * Created by KEKE on 2018/02/28.
 */
class TaskDetailsViewModel(private val tasksRepository: TasksRepository,
                           private val navigator: Navigator) : BaseObservable() {

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
                        })
    }

    fun onBackbuttonClick() {
        navigator.finish()
    }
}
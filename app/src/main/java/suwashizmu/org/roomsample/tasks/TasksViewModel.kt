package suwashizmu.org.roomsample.tasks

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.source.TasksRepository

/**
 * Created by KEKE on 2018/02/25.
 */
class TasksViewModel(private val tasksRepository: TasksRepository) : BaseObservable() {

    val items: ObservableList<Task> = ObservableArrayList<Task>()

    fun start() {
        loadTasks()
    }

    private fun loadTasks() {

        tasksRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            items.clear()
                            items.addAll(it)
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }
}
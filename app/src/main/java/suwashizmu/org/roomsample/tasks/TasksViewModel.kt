package suwashizmu.org.roomsample.tasks

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import suwashizmu.org.roomsample.Navigator
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.data.source.TasksRepository

/**
 * Created by KEKE on 2018/02/25.
 */
class TasksViewModel(private val tasksRepository: TasksRepository,
                     private val navigator: Navigator) : BaseObservable() {

    val items: ObservableList<Task> = ObservableArrayList<Task>()
    val deletedItem: ObservableField<Task> = ObservableField()

    fun start() {
        loadTasks()
    }

    fun addTasks(vararg tasks: Task) {

        Completable.create {
            tasksRepository.insertAll(*tasks)
            it.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items.addAll(tasks) }

    }

    fun delete(task: Task) {
        Single.create<Task> {
            tasksRepository.delete(task)
            it.onSuccess(task)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1: Task, t2: Throwable? ->
                    t2?.printStackTrace()
                    deletedItem.set(t1)
                    items.remove(task)
                }
    }

    fun onAddButtonClick() {
        navigator.gotoDetails(null)
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
package suwashizmu.org.roomsample.tasks

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.tasks_act.*
import suwashizmu.org.roomsample.R
import suwashizmu.org.roomsample.data.Task
import suwashizmu.org.roomsample.databinding.TaskItemBinding
import suwashizmu.org.roomsample.databinding.TasksFragBinding
import suwashizmu.org.roomsample.details.TaskDetailsActivity
import java.util.*


/**
 * A placeholder fragment containing a simple view.
 */
class TasksFragment : Fragment() {

    companion object {
        private const val REQUEST_DETAILS = 1
    }

    private lateinit var binding: TasksFragBinding

    var tasksViewModel: TasksViewModel? = null
    private lateinit var listAdapter: TasksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.tasks_frag, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            val intent = Intent(context, TaskDetailsActivity::class.java)
            startActivityForResult(intent, REQUEST_DETAILS)
            tasksViewModel?.addTasks(Task(0, Date().toString()))
        }

        listAdapter = TasksAdapter({
            tasksViewModel?.delete(it)
        })
        binding.listView.adapter = listAdapter
        binding.listView.layoutManager = LinearLayoutManager(activity)

        tasksViewModel?.items?.addOnListChangedCallback(taskListCallback)
        tasksViewModel?.deletedItem?.addOnPropertyChangedCallback(taskDeleteCallback)

        tasksViewModel?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        tasksViewModel?.items?.removeOnListChangedCallback(taskListCallback)
        tasksViewModel?.deletedItem?.removeOnPropertyChangedCallback(taskDeleteCallback)
    }

    /**
     * タスクが削除された際のコールバック
     * SnackBarを表示する
     */
    private val taskDeleteCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(p0: Observable?, p1: Int) {
            val deletedItem = tasksViewModel?.deletedItem?.get()
            Snackbar.make(view!!, "Deleted $deletedItem", Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    /**
     * TaskListが変化した際のコールバック
     * 初回はonItemRangeInsertedが呼ばれる
     */
    private val taskListCallback = object : ObservableList.OnListChangedCallback<ObservableList<Task>>() {
        override fun onChanged(p0: ObservableList<Task>?) {
            Logger.d(tasksViewModel?.items ?: "empty")
        }

        override fun onItemRangeRemoved(p0: ObservableList<Task>?, p1: Int, p2: Int) {
            Logger.d(tasksViewModel?.items ?: "empty")
            tasksViewModel?.items?.let {
                listAdapter.setItems(it)
                listAdapter.notifyDataSetChanged()
            }
        }

        override fun onItemRangeMoved(p0: ObservableList<Task>?, p1: Int, p2: Int, p3: Int) {
            Logger.d(tasksViewModel?.items ?: "empty")

        }

        override fun onItemRangeInserted(p0: ObservableList<Task>?, p1: Int, p2: Int) {
            Logger.d(tasksViewModel?.items ?: "empty")
            tasksViewModel?.items?.let {
                listAdapter.setItems(it)
                listAdapter.notifyDataSetChanged()
            }
        }

        override fun onItemRangeChanged(p0: ObservableList<Task>?, p1: Int, p2: Int) {
            Logger.d(tasksViewModel?.items ?: "empty")
        }
    }

    private class TasksAdapter(private val itemClickCallback: (task: Task) -> Unit) : RecyclerView.Adapter<TaskItemViewHolder>() {

        private var items: List<Task> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskItemViewHolder {
            val binding: TaskItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.task_item, parent, false)
            return TaskItemViewHolder(binding.root, binding).apply {
                itemView.setOnClickListener {
                    itemClickCallback(items[adapterPosition])
                }
            }
        }

        override fun onBindViewHolder(holder: TaskItemViewHolder?, position: Int) {
            holder?.binding?.task = items[position]
        }

        override fun getItemCount(): Int = items.count()

        fun setItems(items: List<Task>) {
            this.items = items
        }
    }

    private class TaskItemViewHolder(view: View, val binding: TaskItemBinding) : RecyclerView.ViewHolder(view)
}

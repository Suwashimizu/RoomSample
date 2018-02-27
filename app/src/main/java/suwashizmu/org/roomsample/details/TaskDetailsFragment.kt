package suwashizmu.org.roomsample.details


import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import suwashizmu.org.roomsample.R
import suwashizmu.org.roomsample.databinding.TaskDetailsFragBinding

/**
 * Created by KEKE on 2018/02/27.
 */
class TaskDetailsFragment : Fragment() {

    private lateinit var binding: TaskDetailsFragBinding
    var taskDetailsViewModel: TaskDetailsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.task_details_frag, container, false)


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        taskDetailsViewModel?.errorMessage
                ?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(p0: Observable?, p1: Int) {
                        taskDetailsViewModel?.errorMessage?.get()?.let {
                            Snackbar.make(view!!, it, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                        }
                    }
                })
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = taskDetailsViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()

        taskDetailsViewModel = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.task_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save -> taskDetailsViewModel?.onSaveButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }
}
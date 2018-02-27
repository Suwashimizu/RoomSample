package suwashizmu.org.roomsample.details


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import suwashizmu.org.roomsample.R
import suwashizmu.org.roomsample.databinding.TaskDetailsFragBinding

/**
 * Created by KEKE on 2018/02/27.
 */
class TaskDetailsFragment : Fragment() {

    private lateinit var binding: TaskDetailsFragBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.task_details_frag, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
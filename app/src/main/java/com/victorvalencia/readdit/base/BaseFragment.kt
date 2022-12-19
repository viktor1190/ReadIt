package com.victorvalencia.readdit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.victorvalencia.readdit.BR
import com.victorvalencia.readdit.util.LoadingView

abstract class BaseFragment<FRAGMENT_VIEW_MODEL : BaseViewModel, BINDING: ViewDataBinding> : Fragment() {

    protected abstract val fragmentViewModel: FRAGMENT_VIEW_MODEL

    private var _binding: BINDING? = null

    protected val binding: BINDING
        get() = _binding!!

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.observeUiEvents(this)
        setupBinding(binding)
    }

    private fun setupBinding(binding: BINDING) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, fragmentViewModel)
        binding.setVariable(BR.parentLifeCycleOwner, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading(showLoading: Boolean) {
        loadingView.visibility = if (showLoading) View.VISIBLE else View.GONE
    }

    // Delay creation/adding of loading view until first usage.
    private val loadingView by lazy {
        LoadingView(requireContext()).apply {
            activity?.window?.addContentView(
                this,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    fun showErrorDialog(dialogContent: DialogContent) {
        SimpleDialogFragment(dialogContent).show(parentFragmentManager, null)
    }

}
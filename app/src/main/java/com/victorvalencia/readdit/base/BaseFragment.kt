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

abstract class BaseFragment<FRAGMENT_VIEW_MODEL : BaseViewModel, BINDING: ViewDataBinding> : Fragment() {

    protected abstract val fragmentViewModel: FRAGMENT_VIEW_MODEL

    private var _binding: BINDING? = null

    private val binding: BINDING
        get() = _binding!!

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

}
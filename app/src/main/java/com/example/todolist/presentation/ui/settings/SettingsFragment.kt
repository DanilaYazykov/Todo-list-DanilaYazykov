package com.example.todolist.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.example.todolist.app.App
import com.example.todolist.databinding.FragmentSettingsBinding
import com.example.todolist.presentation.presenters.settingsViewModel.SettingsViewModel
import com.example.todolist.presentation.presenters.settingsViewModel.SettingsViewModelFactory
import com.example.todolist.utils.BindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

/**
 * SettingsFragment - UI класс одного из фрагментов, который отвечает за отображение настроек.
 */
class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var vmFactory: SettingsViewModelFactory
    private val viewModel: SettingsViewModel by viewModels { vmFactory }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as App).appComponent.fragmentComponentFactory().create()
            .inject(this)
        bottomSheetManager()
        switchTheme()
    }

    private fun switchTheme() {
        binding.lightTheme.setOnClickListener {
            viewModel.switchTheme(2)
        }
        binding.darkTheme.setOnClickListener {
            viewModel.switchTheme(1)
        }
        binding.systemTheme.setOnClickListener {
            viewModel.switchTheme(-1)
        }
    }

    private fun bottomSheetManager() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            binding.dimView.visibility = View.GONE
        }

        binding.checkTheme.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.dimView.visibility = View.VISIBLE
        }

        binding.dimView.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.dimView.visibility = View.GONE
        }
        bottomSheetCallBack(bottomSheetBehavior)
    }

    private fun bottomSheetCallBack(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>) {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> Unit
                    BottomSheetBehavior.STATE_COLLAPSED -> Unit
                    BottomSheetBehavior.STATE_HIDDEN -> binding.dimView.visibility = View.GONE
                    else -> Unit
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
    }

}
package com.app.meatz.presentation.featureBoxes.newBox

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.hideKeyboard
import com.app.meatz.data.utils.extensions.isEmpty
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentNewBoxBinding
import com.app.meatz.presentation.featureBoxes.BoxesViewModel


class NewBoxFragment : BaseFragment<FragmentNewBoxBinding>() {

    private val viewModel by viewModels<BoxesViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAddBox.setOnClickListener {
                requireContext().hideKeyboard(it)
                if (etBoxName.isEmpty()) {
                    requireActivity().setSnackbar(btnAddBox, getString(R.string.newbox_validate_empty_box_name), true)
                    etBoxName.requestFocus()
                } else
                    addNewBox()
            }
        }
    }

    private fun addNewBox() {
        viewModel.createBox(binding.etBoxName.text.toString()).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAddBox, it.message, true)
                    mainController.popBackStack()
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAddBox, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnAddBox, it.message)
                }
            }
        })
    }

}
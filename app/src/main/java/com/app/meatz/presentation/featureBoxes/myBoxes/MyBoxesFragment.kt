package com.app.meatz.presentation.featureBoxes.myBoxes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.meatz.R
import com.app.meatz.core.BaseFragment
import com.app.meatz.data.application.BOX_ID
import com.app.meatz.data.network.ERROR
import com.app.meatz.data.network.FAILURE
import com.app.meatz.data.network.LOADING
import com.app.meatz.data.network.SUCCESS
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.data.utils.setSnackbar
import com.app.meatz.databinding.FragmentMyBoxesBinding
import com.app.meatz.domain.remote.MyBox
import com.app.meatz.presentation.featureAuth.AuthActivity
import com.app.meatz.presentation.featureBoxes.BoxesViewModel
import com.app.meatz.presentation.home.MainActivity
import org.jetbrains.anko.support.v4.intentFor


class MyBoxesFragment : BaseFragment<FragmentMyBoxesBinding>() {
    private val viewModel by viewModels<BoxesViewModel>()
    private lateinit var myBoxAdapter: MyBoxRvAdapter
    private var isActive = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewsClick()
        handleViewsAfterUserCheck()
    }

    private fun handleViewsAfterUserCheck() {
        if (!viewModel.userIsLogged())
            binding.apply {
                clUnLoggedUser.visible()
                rlRoot.gone()
            }
        else {
            binding.apply {
                clUnLoggedUser.gone()
                rlRoot.visible()
            }
            getMyBoxes()
        }
    }

    private fun getMyBoxes() {
        viewModel.getMyBox().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    rlRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    binding.apply {
                        rlRoot.visible()
                        shimmer.root.gone()
                    }
                    it?.data?.let {
                        updateViewsAfterBoxCheck(it)
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.btnLogin, it?.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }

    private fun updateViewsAfterBoxCheck(myBox: List<MyBox>) {
        myBoxAdapter = MyBoxRvAdapter()
        if (myBox.isEmpty()) {
            binding.apply {
                emptyLayout.visible()
                rvMyBoxes.gone()
            }
        } else {
            binding.apply {
                emptyLayout.gone()
                rvMyBoxes.visible()
            }
            myBoxAdapter.fill(myBox)
            initMyBoxesRv()
        }
    }


    private fun initMyBoxesRv() {
        binding.rvMyBoxes.apply {
            linearLayoutManager()
            adapter = myBoxAdapter
        }
        myBoxAdapter.setOnClickListener { itemview, item, _ ->
            when (itemview.id) {
                R.id.flRemove -> {
                    showAlert(getString(R.string.mybox_delete_box), getString(R.string.mybox_delete_box_msg),
                            getString(R.string.base_ok), {
                        deleteBox(item.id)
                    }, true, true)
                }
                R.id.btnAddCart -> {
                    if (item.is_active == 0)
                        isActive = true
                    addBoxToMyCart(item.id)
                }
                R.id.btnView -> {
                    val bundle = Bundle()
                    bundle.putInt(BOX_ID, item.id)
                    mainController.navigate(R.id.action_myBox_boxDetails, bundle)
                }
            }
        }
    }

    private fun addBoxToMyCart(boxId: Int) {
        viewModel.addBoxToCart(boxId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    if (isActive)
                        showUnAvailableDialog()
                    isActive = false
                    requireActivity().setSnackbar(binding.btnLogin, it.message, true)

                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it.message)
                }
            }
        })
    }

    private fun showUnAvailableDialog() {
        showAlert(getString(R.string.mybox_items_unavailable), getString(R.string.mybox_items_unavailable_msg),
                getString(R.string.edit_profile_ok), {}, true, true)
    }

    private fun deleteBox(boxId: Int) {
        viewModel.deleteBox(boxId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it?.message.toString(), true)
                    it?.data?.let {
                        updateViewsAfterBoxCheck(it)
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it?.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnLogin, it.message)
                }
            }
        })
    }

    private fun handleViewsClick() {
        binding.apply {
            btnLogin.setOnClickListener { startActivity(intentFor<AuthActivity>()) }
            flnewBox.setOnClickListener {
                mainController.navigate(R.id.action_myBox_newBox)
            }
        }

    }
}
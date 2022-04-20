package com.app.meatz.presentation.featureBoxes.myBoxDetails

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
import com.app.meatz.databinding.FragmentMyBoxDetailsBinding
import com.app.meatz.domain.remote.BoxProducts
import com.app.meatz.presentation.featureBoxes.BoxesViewModel
import com.app.meatz.presentation.home.MainActivity


class MyBoxDetailsFragment : BaseFragment<FragmentMyBoxDetailsBinding>() {

    private val viewModel by viewModels<BoxesViewModel>()
    private lateinit var boxProdutsRvAdapter: MyBoxProductsRvAdapter
    private var boxId = 0
    private var deltedItemsIds = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            boxId = it.getInt(BOX_ID)
        }
        getBoxDetails()
        handleViewsCLick()

    }


    private fun getBoxDetails() {
        viewModel.getMyBoxDetails(boxId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> binding.apply {
                    clRoot.gone()
                    shimmer.root.visible()
                }
                SUCCESS -> {
                    binding.apply {
                        clRoot.visible()
                        shimmer.root.gone()
                    }
                    it.data?.let {
                        initBoxProductsViews(it.boxProducts)
                        binding.tvBoxName.text = it.box.name
                    }
                }
                ERROR -> {
                    binding.shimmer.root.gone()
                    requireActivity().setSnackbar(binding.btnUpate, it.message.toString(), true)
                }
                FAILURE -> {
                    handleError(it.message) { MainActivity.showHideViewsInCaseNoConnections.value = false }
                }
            }
        })
    }


    private fun initBoxProductsRv() {
        binding.rvBoxProducts.apply {
            linearLayoutManager()
            adapter = boxProdutsRvAdapter
        }

        boxProdutsRvAdapter.setOnClickListener { itemView, item, position ->
            if (itemView.id == R.id.flRemove) {
                deltedItemsIds += item.id.toString() + ","
                boxProdutsRvAdapter.removeItem(position)
                boxProdutsRvAdapter.notifyItemRemoved(position)
                boxProdutsRvAdapter.notifyDataSetChanged()
                if (boxProdutsRvAdapter.itemCount == 0) {
                    binding.apply {
                        rvBoxProducts.gone()
                        emptyLayout.visible()
                    }

                }

                binding.btnUpate.visible()
            }

        }
    }

    private fun handleViewsCLick() {
        binding.btnUpate.setOnClickListener {
            removeProductsFromBox()
        }
    }

    private fun initBoxProductsViews(list: List<BoxProducts>) {


        if (list.isEmpty()) {
            binding.apply {
                rvBoxProducts.gone()
                emptyLayout.visible()

            }
        } else {
            boxProdutsRvAdapter = MyBoxProductsRvAdapter()
            boxProdutsRvAdapter.fill(list)


            initBoxProductsRv()
        }
    }

    private fun removeProductsFromBox() {
        viewModel.removeProductFromBox(boxId, deltedItemsIds).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LOADING -> showLoading()
                SUCCESS -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnUpate, it.message, true)
                    if (boxProdutsRvAdapter.itemCount == 0) {
                        binding.apply {
                            btnUpate.gone()
                            rvBoxProducts.gone()
                            emptyLayout.visible()
                        }
                    }
                }
                ERROR -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnUpate, it.message.toString(), true)
                }
                FAILURE -> {
                    dismissLoading()
                    requireActivity().setSnackbar(binding.btnUpate, it.message)
                }
            }
        })
    }


}
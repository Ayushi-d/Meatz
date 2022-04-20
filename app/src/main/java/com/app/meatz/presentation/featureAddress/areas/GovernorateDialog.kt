package com.app.meatz.presentation.featureAddress.areas

import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import com.app.meatz.R
import com.app.meatz.data.utils.LocaleHelper
import com.app.meatz.data.utils.extensions.linearLayoutManager
import com.app.meatz.databinding.DialogGovernoratesBinding
import com.app.meatz.domain.remote.area.Governorates

class GovernorateDialog(context: Context, var governateList: List<Governorates>) :
        AppCompatDialog(context, R.style.full_screen_dialog) {

    private val binding = DialogGovernoratesBinding.inflate(layoutInflater)
    private lateinit var governatesAdapter: GovernoratesAdapter

    fun initDialog() {
        this.apply {
            setContentView(binding.root)
            setCancelable(true)
            window?.apply {
                setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT
                )
            }
            if (LocaleHelper.locale == "en")
                binding.ivClose.rotation = 90f
            else
                binding.ivClose.rotation = 270f
            binding.ivClose.setOnClickListener { dismiss() }
            governatesAdapter = GovernoratesAdapter()
            binding.rvGovernorate.apply {
                linearLayoutManager()
                adapter = governatesAdapter
            }
            governatesAdapter.fill(governateList)
            governatesAdapter.setOnClickListener { _, _, _ ->
                this.dismiss()

            }
            this.show()
        }
    }
}
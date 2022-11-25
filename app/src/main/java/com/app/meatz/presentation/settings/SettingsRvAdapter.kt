package com.app.meatz.presentation.settings

import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import com.app.meatz.R
import com.app.meatz.core.BaseAdapter
import com.app.meatz.data.preferences.notificationsAllowed
import com.app.meatz.data.utils.extensions.gone
import com.app.meatz.data.utils.extensions.load
import com.app.meatz.data.utils.extensions.visible
import com.app.meatz.databinding.ItemProfileBinding
import com.app.meatz.domain.local.ProfileRsm

class SettingsRvAdapter : BaseAdapter<ItemProfileBinding, ProfileRsm>() {
    override fun setContent(binding: ItemProfileBinding, item: ProfileRsm, position: Int) {
        binding.apply {
            if (position == 0)
                swNotifiction.visible()
            else
                swNotifiction.gone()


            if (notificationsAllowed)
                setNotificationSwitchTextOn(swNotifiction)
            else
                setNotificationsSwitchTextOff(swNotifiction)


            swNotifiction.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    notificationsAllowed = true
                    setNotificationSwitchTextOn(swNotifiction)
                } else {
                    notificationsAllowed = false
                    setNotificationsSwitchTextOff(swNotifiction)
                }
            })
            root.setOnClickListener { onViewClicked(it, item, position) }
            if (item.isApiPage){
                if (position == 2){
                    ivProfile.setImageResource(R.drawable.meatzicon)
                }else{
                    ivProfile.load(item.apiPageImage)
                }
            }
            else
                   ivProfile.load(item.image)
            tvTitle.text = item.title
        }
    }

    private fun setNotificationSwitchTextOn(switch: SwitchCompat) {
        switch.setSwitchTextAppearance(switch.rootView.context, R.style.notificationSwitchTextStyleOn)
        switch.isChecked = true
    }

    private fun setNotificationsSwitchTextOff(switch: SwitchCompat) {
        switch.setSwitchTextAppearance(switch.rootView.context, R.style.notificationSwitchTextStyleOff)
        switch.isChecked = false
    }
}
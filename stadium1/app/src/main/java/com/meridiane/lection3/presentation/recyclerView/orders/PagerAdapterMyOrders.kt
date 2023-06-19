package com.meridiane.lection3.presentation.recyclerView.orders

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.meridiane.lection3.presentation.ui.profile.ActiveOrdersFragment
import com.meridiane.lection3.presentation.ui.profile.AllOrdersFragment

class PagerAdapterMyOrders(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AllOrdersFragment()
            else -> ActiveOrdersFragment()
        }
    }
}
package com.madeean.livestream.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = arrayListOf<Fragment>()

    private val diffutil = object: DiffUtil.ItemCallback<Fragment>() {
        override fun areItemsTheSame(oldItem: Fragment, newItem: Fragment): Boolean
        = oldItem == newItem
        override fun areContentsTheSame(oldItem: Fragment, newItem: Fragment): Boolean
        = oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffutil)

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        differ.submitList(fragmentList)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return differ.currentList[position]
    }
}
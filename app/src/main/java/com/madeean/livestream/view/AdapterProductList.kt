package com.madeean.livestream.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madeean.livestream.R
import com.madeean.livestream.databinding.ItemProductListV2Binding
import com.madeean.livestream.domain.products.model.ModelProductListDomain

class AdapterProductList : RecyclerView.Adapter<AdapterProductList.AdapterProductListViewHolder>() {
  private val listData: ArrayList<ModelProductListDomain> = arrayListOf()
  private lateinit var context: Context

  class AdapterProductListViewHolder(
    val binding: ItemProductListV2Binding,
  ) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): AdapterProductListViewHolder {
    context = parent.context
    val binding =
      ItemProductListV2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AdapterProductListViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: AdapterProductListViewHolder,
    position: Int
  ) {
    holder.binding.txtProductName.text = listData[position].name
    holder.binding.txtProductPrice.text = listData[position].productPrice.toString()
    holder.binding.viewDiscount.tvDiscountLabel.text =
      listData[position].productSpesialPrice.toString()
    holder.binding.viewDiscount.tvDiscount.text = listData[position].productDiscount.toString()

    Glide.with(context).load(listData[position].image)
      .placeholder(context.getDrawable(R.drawable.default_image)).into(holder.binding.imgProduct)
  }

  override fun getItemCount(): Int {
    return listData.size
  }

  fun addData(data: ArrayList<ModelProductListDomain>) {
    listData.clear()
    listData.addAll(data)
    notifyDataSetChanged()
  }


}
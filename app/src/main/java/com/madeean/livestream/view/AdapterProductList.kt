package com.madeean.livestream.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.madeean.livestream.R
import com.madeean.livestream.databinding.ItemProductListV2Binding
import com.madeean.livestream.domain.products.model.ModelProductList

class AdapterProductList : RecyclerView.Adapter<AdapterProductList.AdapterProductListViewHolder>() {
  private val listData:ArrayList<ModelProductList> = arrayListOf()
  private lateinit var context: Context

  class AdapterProductListViewHolder(
    val binding: ItemProductListV2Binding,
  ) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ):AdapterProductListViewHolder {
    context = parent.context
    val binding =ItemProductListV2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AdapterProductListViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: AdapterProductListViewHolder,
    position: Int
  ) {
      holder.binding.imgProduct.setImageDrawable(context.getDrawable(R.drawable.default_image))
    holder.binding.txtProductName.text = listData[position].name
    holder.binding.txtProductPrice.text = listData[position].price
    holder.binding.viewDiscount.tvDiscountLabel.text = listData[position].discountPrice
    holder.binding.viewDiscount.tvDiscount.text = listData[position].discountRate
  }

  override fun getItemCount(): Int {
    return listData.size
  }

  fun addData(data:ArrayList<ModelProductList>){
    listData.clear()
    listData.addAll(data)
    notifyDataSetChanged()
  }


}
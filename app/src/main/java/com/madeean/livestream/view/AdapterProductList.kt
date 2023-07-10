package com.madeean.livestream.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madeean.livestream.R
import com.madeean.livestream.databinding.ItemProductListV2Binding
import com.madeean.livestream.domain.products.model.ModelProductListDomain
import java.util.Collections

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

  var dataHighlight: ModelProductListDomain? = null
  var dataHighlightPosition: Int = -1
  override fun onBindViewHolder(
    holder: AdapterProductListViewHolder,
    position: Int
  ) {

    if (listData[position].isHighlight == 1) {
      dataHighlight = listData[position]
      dataHighlightPosition = position
//      holder.binding.clItemProduct.setBackgroundColor(context.getColor(R.color.green))
    }

    if (listData.size > 1) {
      if (listData[position].isHighlight == 1) {
        swapData(dataHighlight!!, dataHighlightPosition, listData[0], position)
        if (position == 0) {

          holder.binding.highlightProduct.setBackgroundResource(R.drawable.bg_atas_product)
        }
      }
    }

    holder.binding.txtProductName.text = listData[position].name
    holder.binding.txtProductPrice.text = listData[position].productPrice.toString()
    holder.binding.viewDiscount.tvDiscountLabel.text =
      listData[position].productSpesialPrice.toString()
    holder.binding.viewDiscount.tvDiscount.text = listData[position].productDiscount.toString()

    Glide.with(context).load(listData[position].image)
      .placeholder(context.getDrawable(R.drawable.default_image)).into(holder.binding.imgProduct)


  }

  private fun swapData(
    dataHighlight: ModelProductListDomain,
    dataHighlightPosition: Int,
    data0: ModelProductListDomain,
    pos0: Int
  ) {
//    listData.removeAt(pos0)
    listData.set(0, dataHighlight)

//    listData.removeAt(dataHighlightPosition)
    listData.set(dataHighlightPosition, data0)

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
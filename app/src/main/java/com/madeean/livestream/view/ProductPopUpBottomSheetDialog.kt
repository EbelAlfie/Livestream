package com.madeean.livestream.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.madeean.livestream.R
import com.madeean.livestream.databinding.BottomsheetProductPopupBinding
import com.madeean.livestream.domain.products.model.ModelProductList


class ProductPopUpBottomSheetDialog(val listData: ArrayList<ModelProductList>): BottomSheetDialogFragment() {
    private lateinit var binding: BottomsheetProductPopupBinding
    private lateinit var adapter:AdapterProductList
//    private var listData:ArrayList<ModelProductList> = arrayListOf()

    companion object {
        fun newInstance(listData:ArrayList<ModelProductList>): ProductPopUpBottomSheetDialog {
            return ProductPopUpBottomSheetDialog(listData)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottomsheet_product_popup, container, false)
        binding = BottomsheetProductPopupBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(true)
        setupAdapter()
        setupRecyclerView()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val dialog = dialog as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return bottomSheetDialog
    }

    private fun setupAdapter() {
        adapter = AdapterProductList()
        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductList.adapter = adapter
    }

    private fun setupRecyclerView() {
        adapter.addData(listData)
    }

}
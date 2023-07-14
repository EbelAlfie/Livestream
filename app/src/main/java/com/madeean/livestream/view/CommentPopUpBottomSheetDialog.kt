package com.madeean.livestream.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.madeean.livestream.R
import com.madeean.livestream.databinding.BottomsheetCommentBinding

class CommentPopUpBottomSheetDialog(private val listener: SetOnCommentSend) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomsheetCommentBinding
    companion object {
        fun newInstance(listener: SetOnCommentSend): CommentPopUpBottomSheetDialog {
            return CommentPopUpBottomSheetDialog(listener)
        }
    }

    interface SetOnCommentSend {
        fun onCommentSend(text: String)
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
        val view = inflater.inflate(R.layout.bottomsheet_comment, container, false)
        binding = BottomsheetCommentBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(true)
        binding.apply {
            etComment.apply{
                requestFocus()
                onFocusChangeListener =
                    View.OnFocusChangeListener { _, isFocused -> if (!isFocused) dismiss() }
            }
            btnSendComment.setOnClickListener {
                val text = etComment.text.toString()
                if (text.isBlank()) return@setOnClickListener
                listener.onCommentSend(text)
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val commentDialog = dialog as BottomSheetDialog
            val bottomSheet =
                commentDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return bottomSheetDialog
    }
}
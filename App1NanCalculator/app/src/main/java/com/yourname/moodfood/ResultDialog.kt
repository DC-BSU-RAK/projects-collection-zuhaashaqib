package com.yourname.moodfood

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ResultDialog : DialogFragment() {

    companion object {
        fun newInstance(foodName: String, description: String, imageRes: Int): ResultDialog {
            val args = Bundle().apply {
                putString("food", foodName)
                putString("desc", description)
                putInt("image", imageRes)
            }
            return ResultDialog().apply { arguments = args }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_result, null)

        val foodName = arguments?.getString("food") ?: ""
        val desc = arguments?.getString("desc") ?: ""
        val imageRes = arguments?.getInt("image") ?: R.drawable.food_placeholder

        view.findViewById<TextView>(R.id.tvResultFood).text = foodName
        view.findViewById<TextView>(R.id.tvResultDesc).text = desc
        view.findViewById<ImageView>(R.id.ivResultFood).setImageResource(imageRes)

        // Animate the result card
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.result_pop)
        view.startAnimation(anim)

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MoodDialog)
            .setView(view)
            .create()

        view.findViewById<MaterialButton>(R.id.btnResultClose).setOnClickListener {
            dismiss()
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
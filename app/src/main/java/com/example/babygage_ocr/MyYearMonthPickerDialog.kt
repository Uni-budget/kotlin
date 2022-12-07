package com.example.babygage_ocr


import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import java.util.*


class MyYearMonthPickerDialog : DialogFragment() {
    private var listener: OnDateSetListener? = null
    var cal: Calendar = Calendar.getInstance()
    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    var btnConfirm: Button? = null
    var btnCancel: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().getLayoutInflater()
        val dialog: View = inflater.inflate(R.layout.year_month_picker, null)
        btnConfirm = dialog.findViewById(R.id.btn_confirm)
        btnCancel = dialog.findViewById(R.id.btn_cancel)
        val monthPicker = dialog.findViewById(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById(R.id.picker_year) as NumberPicker

        btnCancel!!.setOnClickListener {
            this@MyYearMonthPickerDialog.getDialog()!!.cancel()
            }
        btnConfirm!!.setOnClickListener {
            listener!!.onDateSet(null, yearPicker.value, monthPicker.value, 0)
            this@MyYearMonthPickerDialog.getDialog()!!.cancel()
        }
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal.get(Calendar.MONTH) + 1
        val year: Int = cal.get(Calendar.YEAR)
        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year
        builder.setView(dialog) // Add action buttons
        /*
        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MyYearMonthPickerDialog.this.getDialog().cancel();
            }
        })
        */
        return builder.create()
    }

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 1980
    }
}

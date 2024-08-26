package com.example.carbonfootprint.CalculationFun

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.carbonfootprint.R

class ButtonChangingColor {

    fun setButtonBackgroundColorSelectYes(buttonNo: Button, buttonYes: Button, context: Context) {
        val inactiveColor = ContextCompat.getColor(context, R.color.grey)
        val activeColor = ContextCompat.getColor(context, R.color.colorPrimary)

        buttonNo.backgroundTintList = ColorStateList.valueOf(inactiveColor)
        buttonYes.backgroundTintList = ColorStateList.valueOf(activeColor)
    }

    @SuppressLint("ResourceAsColor")
    fun setButtonBackgroundColorSelectNo(buttonNo: Button, buttonYes: Button, context: Context) {
        val inactiveColor = ContextCompat.getColor(context, R.color.grey)
        val activeColor = ContextCompat.getColor(context, R.color.colorPrimary)

        buttonNo.backgroundTintList = ColorStateList.valueOf(activeColor)
        buttonYes.backgroundTintList = ColorStateList.valueOf(inactiveColor)
    }

    fun setButtonBackgroundColorSelectDefault(buttonNo: Button, buttonYes: Button, context: Context) {
        val inactiveColor = ContextCompat.getColor(context, R.color.grey)

        buttonNo.backgroundTintList = ColorStateList.valueOf(inactiveColor)
        buttonYes.backgroundTintList = ColorStateList.valueOf(inactiveColor)
    }
}
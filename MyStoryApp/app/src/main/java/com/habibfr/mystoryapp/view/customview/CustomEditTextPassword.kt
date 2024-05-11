package com.habibfr.mystoryapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.habibfr.mystoryapp.R

class CustomEditTextPassword @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var textInputLayout: TextInputLayout? = null
    private var customButton: CustomButton? = null
    private var isValid: Boolean = false

    init {
        customButton?.isEnabled = false
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    fun setTextInputLayout(textInputLayout: TextInputLayout) {
        this.textInputLayout = textInputLayout
    }

    fun getButtonIsValid(): Boolean {
        return this.isValid
    }

    fun setButton(button: CustomButton) {
        this.customButton = button
    }

    private fun validatePassword(password: String) {
        if (password.length < 8) {
            customButton?.isEnabled = false
            isValid = false
            textInputLayout?.error = context.getString(R.string.error_pw)
        } else {
            textInputLayout?.isErrorEnabled = false
            isValid = true
            customButton?.isEnabled = true
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (textInputLayout?.isErrorEnabled == true) {
            textInputLayout?.isErrorEnabled = false
        }
        return false
    }
}
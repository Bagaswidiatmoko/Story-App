package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.picodiploma.loginwithanimation.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val email = s.toString().trim()
        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            setError(context.getString(R.string.error_invalid_email_label), null)
        } else {
            error = null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return true
    }

}
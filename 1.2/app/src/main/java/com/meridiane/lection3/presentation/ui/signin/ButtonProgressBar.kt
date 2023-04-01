package com.meridiane.lection3.presentation.ui.signin

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.ButtonProgressBarBinding

class ButtonProgressBar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: ButtonProgressBarBinding =
        ButtonProgressBarBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        initializeAttributes(context,attrs)
    }

        var isLoading: Boolean = false
        set(value) {
            field = value
            if (value){
                binding.progressBar.visibility = VISIBLE
                binding.buttonStart.text = ""
            }
            else{
                binding.progressBar.visibility = GONE

            }
        }

    fun colorButton(color:Int){
         binding.buttonStart.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setText(text:String?) {
        binding.progressBar.visibility = View.GONE
        binding.buttonStart.text = text ?: "Войти"
    }

    private fun initializeAttributes(context: Context, attrs: AttributeSet) {

        val typedAttributeSet = context.obtainStyledAttributes(attrs, R.styleable.ButtonProgressBar)

        val buttonText = typedAttributeSet.getString(R.styleable.ButtonProgressBar_textString)
        setText(buttonText)


        val buttonColor = typedAttributeSet.getColor(
            R.styleable.ButtonProgressBar_bagroundColor,
            ContextCompat.getColor(context, R.color.purple_700)
        )
        colorButton(buttonColor)

        this.isLoading =
            typedAttributeSet.getBoolean(R.styleable.ButtonProgressBar_progressBar, false)

        typedAttributeSet.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.buttonStart.setOnClickListener(l)
    }

}
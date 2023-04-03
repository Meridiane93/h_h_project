package com.meridiane.lection3.presentation.ui.catalog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.ViewProgressContainerBinding
import kotlin.properties.Delegates

private const val MAX_CHILD_COUNT = 3

typealias onBottomListener = () -> Unit

class ProgressContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var listener: onBottomListener ?= null

    private var binding : ViewProgressContainerBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_progress_container, this, true)
        binding = ViewProgressContainerBinding.bind(this)
        initialiseListener()
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        if (childCount > MAX_CHILD_COUNT) {
            throw IllegalStateException("ProgressContainer can host only one direct child")
        }
    }

    private fun findContentView(): View? =
        children.firstOrNull {
            it.id != R.id.layoutLoading && it.id != R.id.layoutNotice
        }

    var state: State by Delegates.observable(State.Loading) { _, _, state ->
        when (state) {
            is State.Loading -> {
                binding.layoutLoading.isVisible = true
                binding.layoutNotice.isVisible = false
                findContentView()?.isVisible = false
            }
            is State.Notice -> {
                binding.layoutLoading.isVisible = false
                binding.layoutNotice.isVisible = true
                findContentView()?.isVisible = false
                binding.textViewErrorMerge.text = state.message
            }
            is State.Success -> {
                binding.layoutLoading.isVisible = false
                binding.layoutNotice.isVisible = false
                findContentView()?.isVisible = true
            }
        }
    }

    private fun initialiseListener(){
        binding.startButtonMerge.setOnClickListener{
            this.listener?.invoke()
        }
    }

    fun setListener(list: onBottomListener?){
        this.listener = list
    }

    sealed class State {
        object Loading : State()
        data class Notice(val message: String) : State()
        object Success : State()
    }
}
package com.rebeca.spacewallpaper

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T>(private val callback: ((item: T) -> Unit)? = null) :
    RecyclerView.Adapter<DataBindingViewHolder<T>>() {

    private var downloadCallback: ((item: T) -> Unit)? = null
    private var _items: MutableList<T> = mutableListOf()

    /**
     * Returns the _items data
     */
    private val items: List<T>?
        get() = this._items

    override fun getItemCount() = _items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil
            .inflate<ViewDataBinding>(layoutInflater, getLayoutRes(viewType), parent, false)

        binding.lifecycleOwner = getLifecycleOwner()

        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            callback?.invoke(item)
        }
        val myButton = holder.itemView.findViewById<AppCompatImageButton>(R.id.downloadButton) // This is your Button, you declared in your xml file.
        myButton.setOnClickListener {
            downloadCallback?.invoke(item)
            scaleDownloadButton(myButton)
        }
    }

    fun setDownloadCallback(callback: ((item: T) -> Unit)? = null) {
        downloadCallback = callback
    }

    private fun scaleDownloadButton(button: ImageButton) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            button, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    fun getItem(position: Int) = _items[position]

    /**
     * Adds data to the actual Dataset
     *
     * @param items to be merged
     */
    fun addData(items: List<T>) {
        _items.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * Clears the _items data
     */
    fun clear() {
        _items.clear()
        notifyDataSetChanged()
    }

    @LayoutRes
    abstract fun getLayoutRes(viewType: Int): Int

    open fun getLifecycleOwner(): LifecycleOwner? {
        return null
    }
}


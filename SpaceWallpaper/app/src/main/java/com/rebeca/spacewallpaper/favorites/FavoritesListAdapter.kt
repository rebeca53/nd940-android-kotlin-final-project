package com.rebeca.spacewallpaper.favorites

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rebeca.spacewallpaper.BaseRecyclerViewAdapter
import com.rebeca.spacewallpaper.R
import com.rebeca.spacewallpaper.SpaceImage

//Use data binding to show the reminder on the item
class FavoritesListAdapter(callBack: (selectedImage: SpaceImage) -> Unit) :
    BaseRecyclerViewAdapter<SpaceImage>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.list_view_item
}

/**
 * Extension function to setup the RecyclerView
 */
fun <T> RecyclerView.setup(
    adapter: BaseRecyclerViewAdapter<T>
) {
    this.apply {
        layoutManager = LinearLayoutManager(this.context)
        this.adapter = adapter
    }
}
package com.app.meatz.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.app.meatz.data.utils.EndlessScrollListener
import com.app.meatz.data.utils.extensions.isConnectedToNetwork
import com.app.meatz.databinding.ItemLoadMoreBinding
import com.app.meatz.databinding.ItemLoadMoreErrorBinding
import java.lang.reflect.ParameterizedType

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */

private const val PRIMARY = 0
private const val LOADING_ERROR = 2

abstract class BaseAdapter<VB : ViewBinding, T> : Adapter<ViewHolder>(), Filterable {

  private var hasMore = false
  private var isError = false
  private var items = listOf<T>()
  private var mutableItems = items.toMutableList<T?>()
  private var filteredList = mutableListOf<T?>()
  private var clickListener: ((clickedView: View, item: T, position: Int) -> Unit)? = null
  private var loadMoreListener: ((page: Int) -> Unit)? = null
  private var scrollListener: EndlessScrollListener? = null
  private lateinit var parent: ViewGroup

  private val loadBinding by lazy {
    ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  }

  private val errorBinding by lazy {
    ItemLoadMoreErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  }

  override fun getItemCount() = mutableItems.size


  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
      super.onAttachedToRecyclerView(recyclerView)
      scrollListener = EndlessScrollListener(recyclerView.layoutManager!!).setOnLoadMoreListener {
          if (hasMore) loadMoreListener?.let { listener ->
              showLoading()
              listener.invoke(it)
          }
      }
      recyclerView.addOnScrollListener(scrollListener!!)
  }

  override fun getItemViewType(position: Int) = if (hasMore) {
    when (mutableItems[position]) {
      null -> LOADING_ERROR
      else -> PRIMARY
    }
  } else super.getItemViewType(position)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    this.parent = parent
    return when (viewType) {
      LOADING_ERROR -> when {
        isError -> ErrorViewHolder(errorBinding)
        else -> LoadingViewHolder(loadBinding)
      }
      else -> MainViewHolder(getMainBinding())
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when (getItemViewType(position)) {
      LOADING_ERROR -> if (isError) (holder as BaseAdapter<VB, T>.ErrorViewHolder).setContent()
      PRIMARY -> (holder as BaseAdapter<VB, T>.MainViewHolder).setContent(mutableItems[position])
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun getFilter() = object : Filter() {
    override fun performFiltering(constraint: CharSequence): FilterResults {
      val charString = constraint.toString()
      mutableItems = if (charString.isEmpty()) items.toMutableList() else filteredList

      val filterResults = FilterResults()
      filterResults.values = mutableItems
      return filterResults
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
      mutableItems = results.values as MutableList<T?>
      notifyDataSetChanged()
    }
  }

  /**
   * Set on view click listener
   * @param clickListener (clickedView, clickedItem, clickedPosition)
   */
  fun setOnClickListener(clickListener: (clickedView: View, item: T, position: Int) -> Unit) {
    this.clickListener = clickListener
  }

  /**
   * Set on load more listener
   * @param loadMoreListener (page)
   */
  fun setOnLoadMoreListener(loadMoreListener: (page: Int) -> Unit) {
    this.loadMoreListener = loadMoreListener
    hasMore = true
  }

  /**
   * Set filtered data
   * @param filteredList
   */
  fun submitFilteredList(filteredList: MutableList<T?>) {
    this.filteredList = filteredList
  }

  /**
   * Get items
   */
  fun getCurrentItems() = items

  /**
   * Get item by position
   * @param position
   */
  fun getItem(position: Int) = mutableItems[position]!!

  /**
   * Replace current items with new items
   * @param items New items to fill
   */
  fun fill(items: List<T>) {
    this.items = items
    mutableItems.clear()
    mutableItems.addAll(items)
    notifyDataSetChanged()
  }

  fun filloption(items: List<T>){
    this.items = items
    mutableItems.clear()
    for (i in 0..items.size){
      mutableItems.addAll(items)
    }
    notifyDataSetChanged()

  }

  /**
   * Add items to end of list
   * @param items New items to add
   */
  fun addItems(items: List<T>) {
    if (hasMore) dismissLoading()
    mutableItems.addAll(items)
    notifyDataSetChanged()
  }

  /**
   * Add item to end of list
   * @param item New item to add
   */
  fun addItem(item: T) {
    mutableItems.add(item)
    notifyItemInserted(mutableItems.lastIndex)
  }

  /**
   * Add item to certain position in list
   * @param position Where to add item
   * @param item New item to add
   */
  fun addItem(position: Int, item: T) {
    mutableItems.add(position, item)
    notifyDataSetChanged()
  }

  /**
   * Replace item in certain position
   * @param position Where to add item
   * @param item New item to replace with
   */
  fun replace(position: Int, item: T) {
    mutableItems[position] = item
    notifyItemChanged(position)
  }

  /**
   * Remove item from list
   * @param position Item position
   * @return List size
   */
  fun removeItem(position: Int): Int {
    mutableItems.removeAt(position)
    notifyItemRemoved(position)
    return mutableItems.size
  }

  /**
   * Remove all items from list
   */
  fun clear() {
    items = listOf()
    mutableItems.clear()
    notifyDataSetChanged()
  }


    /**
   * Show loading
   */
  fun showLoading() {
    mutableItems.add(null)
    notifyItemInserted(mutableItems.lastIndex)
  }


    /**
   * Dismiss loading
   */
  fun dismissLoading() {
    if (mutableItems.isNotEmpty()) {
      mutableItems.removeAt(mutableItems.lastIndex)
      notifyItemRemoved(mutableItems.size)
    }
  }

  /**
   * Notify that no more items
   */
  fun setLoaded() {
    hasMore = false
    scrollListener?.setLoaded()
  }

  /**
   * Show error
   */
  fun showError() {
    dismissLoading()
    isError = true
    mutableItems.add(null)
    notifyItemInserted(mutableItems.lastIndex)
  }

  /**
   * Dismiss error
   */
  fun dismissError() {
    if (mutableItems.isNotEmpty()) {
      hasMore = true
      isError = false
      mutableItems.removeAt(mutableItems.lastIndex)
      notifyItemRemoved(mutableItems.size)
    }
  }

  /**
   * Get current page
   */
  fun getPage() = scrollListener?.getPage() ?: 1

  /**
   * Reset page to 1
   */
  fun resetPage() = scrollListener?.resetPage()

  /**
   * Set view content
   * @param binding Bind view
   * @param item Item object
   * @param position Item position
   */
  protected abstract fun setContent(binding: VB, item: T, position: Int)

  /**
   * On view clicked
   * @param view Clicked view
   * @param item Clicked item
   * @param position Clicked position
   */
  protected fun onViewClicked(view: View, item: T, position: Int) {
    clickListener?.invoke(view, item, position)
  }

  @Suppress("UNCHECKED_CAST")
  private fun getMainBinding(): VB {
    val superclass = javaClass.genericSuperclass as ParameterizedType
    val method = (superclass.actualTypeArguments[0] as Class<Any>).getDeclaredMethod("inflate",
        LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
  }

  inner class MainViewHolder(private val binding: VB) : ViewHolder(binding.root) {

    fun setContent(item: T?) {
      if (item != null) setContent(binding, item, bindingAdapterPosition)
    }
  }

  inner class LoadingViewHolder(binding: ItemLoadMoreBinding) : ViewHolder(binding.root)

  inner class ErrorViewHolder(private val binding: ItemLoadMoreErrorBinding) : ViewHolder(binding.root) {

    fun setContent() {
      if (isError) binding.llError.setOnClickListener {
          if (binding.root.context.isConnectedToNetwork()) {
              dismissError()
              showLoading()
              loadMoreListener?.invoke(scrollListener!!.getPage())
          }
      }
    }
  }
}
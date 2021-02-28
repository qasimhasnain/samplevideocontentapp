package com.challenge.starzplay.Home.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.challenge.datamanager.Model.DataItem
import com.challenge.datamanager.Model.MediaItem
import com.challenge.starzplay.Interface.ViewInterface
import com.challenge.starzplay.R
import kotlinx.android.synthetic.main.item_list.view.*
import java.lang.Exception

class ListItemsAdapter (private val mContext: Context,private val viewInterface: ViewInterface, private var parents : ArrayList<DataItem>) : RecyclerView.Adapter<ListItemsAdapter.ListItemViewHolder>(){

    private var viewPool = RecyclerView.RecycledViewPool()
    private var viewList : ArrayList<RecyclerView> = ArrayList()
    private var itemInsertedAt : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        val viewHolder: ListItemViewHolder = ListItemViewHolder(v)
        if (itemInsertedAt > -1) {
            viewList.add(itemInsertedAt, viewHolder.recyclerView)
            itemInsertedAt = -1
        }
        else
            viewList.add(viewHolder.recyclerView)
        return viewHolder
    }
    override fun getItemCount(): Int {
        return parents.size
    }

    fun updateDataList(list : List<DataItem>){
        if (list.size > parents.size){
            for (i in parents.indices){
                if (!list[i].title.equals(parents[i].title, false)){
                    parents.add(i, list[i])
                    notifyItemInserted(i)
                    itemInsertedAt = i;
                }
            }

            if (list.size > parents.size){
                parents.addAll(list.subList(parents.size, list.size))
                itemInsertedAt = -1;
                notifyDataSetChanged()
            }
        }
    }

    fun updateChildDataList(list: List<DataItem>){
        for (i in parents.indices){
            val index : Int = getIndexOfMediaType(parents[i].title, list)
            if (index > -1 && list[index].items.size > parents[i].items.size) {
                try{
                    val adapter : MediaItemAdapter? = if (itemInsertedAt > -1 && i > itemInsertedAt)
                        viewList[i - itemInsertedAt].adapter as MediaItemAdapter
                    else
                        viewList[i].adapter as MediaItemAdapter
                    adapter!!.updateAdapter(list[index].items.subList(parents[i].items.size, list[index].items.size))
                }catch (e : Exception){
                    e.printStackTrace()
                }
                parents[i].items.addAll(list[index].items.subList(parents[i].items.size, list[index].items.size))
            }
        }
    }

    private fun getIndexOfMediaType(mediaType : String, list: List<DataItem>) : Int{
        for (i in list.indices){
            if (mediaType.equals(list[i].title, true))
                return i
        }
        return -1
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val parent = parents[position]
        holder.title.text = parent.title.capitalize()
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context, HORIZONTAL, false)
            adapter = MediaItemAdapter(mContext,object : ViewInterface {
                override fun onMediaItemClick(mediaItem: MediaItem) {
                    super.onMediaItemClick(mediaItem)
                    viewInterface.onMediaItemClick(mediaItem)
                }
            }, ArrayList(parent.items))
            setRecycledViewPool(viewPool)
        }

        holder.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollHorizontally(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    loadMoreItems(position)
                }
            }
        })
    }

    private fun loadMoreItems(position : Int){
        viewInterface.onLoadMoreData(parents[position].items.size,parents[position].title, position )
    }

    class ListItemViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_child
        val title: TextView = itemView.title
    }
}
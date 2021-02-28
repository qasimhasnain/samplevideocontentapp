package com.challenge.starzplay.Home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.challenge.datamanager.Exceptions.NoInternet
import com.challenge.datamanager.Model.DataItem
import com.challenge.datamanager.Model.MediaItem
import com.challenge.starzplay.Interface.AlertDialogInterface
import com.challenge.starzplay.Interface.ViewInterface
import com.challenge.starzplay.Core.ViewImp
import com.challenge.starzplay.Home.Adapters.ListItemsAdapter
import com.challenge.starzplay.MediaDetails.MediaDetailsActivity
import com.challenge.starzplay.R
import com.challenge.starzplay.Utils.Config
import com.challenge.starzplay.Utils.Utils
import com.challenge.starzplay.Utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() , ViewImp{

    private lateinit var homePagePresenter: HomePagePresenter
    private var updatePosition : Int =  0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpRecyclerView()
        setupSearchView()

        homePagePresenter = HomePagePresenter(this)
    }

    private fun setUpRecyclerView(){
        home_list_view.apply {
            layoutManager = LinearLayoutManager(this@Home, VERTICAL, false)
        }
    }

    private fun setupSearchView(){
        home_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty())
                    homePagePresenter.getSearchResults(p0.toString(), 1)
                else{
                    home_list_view.adapter = null
                }
            }
        })
    }

    override fun showProgress() {
        progress_indicator.show()
    }

    override fun onUpdateDataList(dataList: List<Any>?) {
        super.onUpdateDataList(dataList)
        @Suppress("UNCHECKED_CAST")
        (home_list_view.adapter as ListItemsAdapter).updateDataList(dataList as List<DataItem>)
        (home_list_view.adapter as ListItemsAdapter).updateChildDataList(dataList)
    }

    override fun hideProgress() {
        progress_indicator.hide()
    }

    override fun onResponseFailure(obj: Any?) {
        home_search.hideKeyboard()
        when(obj){
            is NoInternet ->{
                Utils.getInstance().showTwoButtonAlert(this, resources.getString(R.string.alert), resources.getString(R.string.retry),
                        resources.getString(R.string.cancel), (obj).message!!,object : AlertDialogInterface {
                    override fun onCancel() {
                        super.onCancel()
                        finish()
                    }
                    override fun onRetry() {
                        super.onRetry()
                        recreate()
                    }
                })
            }else ->{
            Utils.getInstance().showSingleButtonAlert(this@Home, resources.getString(R.string.error),
                    resources.getString(R.string.ok), obj as String,object : AlertDialogInterface {
                override fun onOK() {
                    super.onOK()
                }
            })
        }
        }
    }

    override fun setDataList(dataList: List<Any>?) {
        super.setDataList(dataList)
        home_list_view.apply {
            adapter = ListItemsAdapter(this@Home,object : ViewInterface {
                override fun onMediaItemClick(mediaItem: MediaItem) {
                    super.onMediaItemClick(mediaItem)
                    openDetailScreen(mediaItem)
                }
                override fun onLoadMoreData(size: Int, mediaType: String, position: Int) {
                    super.onLoadMoreData(size, mediaType, position)
                    updatePosition = position
                    homePagePresenter.loadMoreData(mediaType, size)
                }
            }, ArrayList(dataList as List<DataItem>))
        }
    }

    private fun openDetailScreen(mediaItem: MediaItem){
        startActivity(Intent(this@Home, MediaDetailsActivity::class.java)
                .putExtra(Config.KEY_MEDIA_ID,mediaItem.id))
    }
}
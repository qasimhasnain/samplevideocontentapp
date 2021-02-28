package com.challenge.starzplay.Home

import com.challenge.datamanager.DataManager
import com.challenge.datamanager.Exceptions.NoInternet
import com.challenge.datamanager.Interface.OnDataResponse
import com.challenge.datamanager.Model.DataItem
import com.challenge.starzplay.Core.PresenterImp
import com.challenge.starzplay.Core.ViewImp

class HomePagePresenter (private var homePageView : ViewImp?) : PresenterImp{

    private lateinit var query: String
    private  var page: Int = 1
    private lateinit var dataList: ArrayList<DataItem>
    private var dataRequested : Boolean = false

    override fun onDestroy() {
        homePageView = null
    }

    override fun getSearchResults(query: String, page: Int) {
        super.getSearchResults(query, page)
        if (!dataRequested){
            dataRequested = true
            homePageView!!.showProgress()
            this.query = query
            this.page = page
            val dataManager : DataManager = DataManager.getInstance()
            try {
                dataManager.getMultiSearchResults(query, page, object : OnDataResponse {
                    override fun onSuccess(obj: Any?) {
                        dataRequested = false
                        dataList = ArrayList(obj as List<DataItem>)
                        homePageView!!.setDataList(obj)
                        homePageView!!.hideProgress()
                    }
                    override fun onFailure(obj: Any?) {
                        dataRequested = false
                        homePageView!!.hideProgress()
                        homePageView!!.onResponseFailure(obj)
                    }
                })
            }catch (e: NoInternet){
                dataRequested = false
                homePageView!!.hideProgress()
                homePageView!!.onResponseFailure(e)
            }
        }
    }

    override fun loadMoreData(mediaType : String, index : Int) {
        super.loadMoreData(mediaType, index)
        if (!dataRequested){
            dataRequested = true
            homePageView!!.showProgress()

            try {
                DataManager.getInstance().getMoreResultsForMediaType(query, object : OnDataResponse{
                    override fun onFailure(obj: Any?) {
                        dataRequested = false
                        homePageView!!.hideProgress()
                    }

                    override fun onSuccess(obj: Any?) {
                        dataRequested = false
                        var newData : List<DataItem> = obj as List<DataItem>
                        homePageView!!.onUpdateDataList(newData)
                        homePageView!!.hideProgress()
                    }

                    override fun onUpdateMultiSearch(obj: Any?) {
                        dataRequested = false
                        super.onUpdateMultiSearch(obj)
                        homePageView!!.hideProgress()
                    }
                })
            }catch (e: NoInternet){
                dataRequested = false
                homePageView!!.hideProgress()
                homePageView!!.onResponseFailure(e)
            }
        }
    }
}
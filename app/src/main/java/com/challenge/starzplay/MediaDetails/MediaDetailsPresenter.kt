package com.challenge.starzplay.MediaDetails

import com.challenge.datamanager.DataManager
import com.challenge.datamanager.Interface.OnDataResponse
import com.challenge.starzplay.Core.PresenterImp
import com.challenge.starzplay.Core.ViewImp

class MediaDetailsPresenter(private var mediaDetailView : ViewImp?) : PresenterImp {

    override fun onDestroy() {
        mediaDetailView = null;
    }

    override fun getItem(id: Int) {
        super.getItem(id)
        mediaDetailView!!.showProgress()
        DataManager.getInstance().getItemForId(id, object : OnDataResponse{
            override fun onSuccess(obj: Any?) {
                mediaDetailView!!.hideProgress()
                mediaDetailView!!.setDataItem(obj)
            }
            override fun onFailure(obj: Any?) {
                mediaDetailView!!.hideProgress()
                mediaDetailView!!.onResponseFailure(obj)
            }
        })
    }
}
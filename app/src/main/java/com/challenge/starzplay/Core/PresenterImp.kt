package com.challenge.starzplay.Core

interface PresenterImp {
    fun onDestroy()
    fun getSearchResults(query : String, page:Int){}
    fun loadMoreData(mediaType : String, index : Int){}
    fun getItem(id : Int){}
}
package com.challenge.starzplay.Core

interface ViewImp {
    fun showProgress()
    fun hideProgress()
    fun setDataList(dataList: List<Any>?){}
    fun onUpdateDataList(dataList: List<Any>?){}
    fun setDataItem(dataItem : Any?){}
    fun onResponseFailure(obj : Any?)
}


package com.challenge.starzplay.Interface

import com.challenge.datamanager.Model.MediaItem

interface ViewInterface {
     fun onMediaItemClick(mediaItem: MediaItem){}
     fun onLoadMoreData(size : Int, mediaType : String, position : Int){}
}
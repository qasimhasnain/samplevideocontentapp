package com.challenge.starzplay.MediaDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.challenge.datamanager.Model.MediaItem
import com.challenge.starzplay.Core.ViewImp
import com.challenge.starzplay.R
import com.challenge.starzplay.Utils.Config
import com.challenge.starzplay.Core.PlayerView
import com.challenge.starzplay.Home.Adapters.MediaItemAdapter
import com.challenge.starzplay.Interface.ViewInterface
import kotlinx.android.synthetic.main.activity_media_item_details.*


class MediaDetailsActivity : AppCompatActivity(), ViewImp{

    private var mediaName: String? = null

    lateinit var mediaDetailsPresenter: MediaDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_item_details)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{ this@MediaDetailsActivity.onBackPressed() }

        initViews()
        val id = intent.getIntExtra(Config.KEY_MEDIA_ID, 0)

        mediaDetailsPresenter = MediaDetailsPresenter(this)
        mediaDetailsPresenter.getItem(id)
    }

    private fun initViews() {
        appbar.setExpanded(true)
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }


    override fun setDataItem(dataItem: Any?) {
        super.setDataItem(dataItem)

        val mediaItem : MediaItem = dataItem as MediaItem
        var imageUrl : String? = mediaItem.backdrop_path
        try {
            when(mediaItem.media_type){
                Config.MovieContent ->{
                    collapsing_toolbar.title = mediaItem.title
                    mediaName = mediaItem.title
                    overview_container.visibility = View.VISIBLE
                    if (imageUrl == null)
                        imageUrl = mediaItem.poster_path
                    person_card.visibility = View.GONE
                }
                Config.TVContent ->{
                    collapsing_toolbar.title = mediaItem.name
                    mediaName = mediaItem.name
                    overview_container.visibility = View.VISIBLE

                    if (imageUrl == null)
                        imageUrl = mediaItem.poster_path

                    person_card.visibility = View.GONE
                }
                Config.Person -> {
                    mediaName = mediaItem.name
                    collapsing_toolbar.title = mediaItem.name
                    overview_container.visibility = View.GONE
                    imageUrl = mediaItem.profile_path

                    if (mediaItem.known_for != null && mediaItem.known_for!!.isNotEmpty()){
                        person_card.visibility = View.VISIBLE
                        known_for_list.apply {
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = MediaItemAdapter(this@MediaDetailsActivity,object : ViewInterface {
                                override fun onMediaItemClick(mediaItem: MediaItem) {
                                    super.onMediaItemClick(mediaItem)
                                }
                            }, ArrayList(mediaItem.known_for))
                        }
                    }
                }
            }
            imageUrl?.let {
                Glide.with(this)
                        .load(Config.ImageBaseUrl + Config.ImageSizeLarge + imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(backdrop)
            }

            pop_value.text = mediaItem.popularity.toString()
        }catch (e : java.lang.Exception){
            e.printStackTrace()
        }


        description.text = mediaItem.overview
        play_button.setOnClickListener{startActivity(Intent(this@MediaDetailsActivity, PlayerView::class.java))}
    }

    override fun onResponseFailure(obj: Any?) {
    }
}
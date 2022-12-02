package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.News

class NewsViewModel: ViewModel() {

    var headlineNewsList = arrayListOf<News>(
        News(id = 1,imgUrl = R.drawable.carousel1 ),
        News(id = 2,imgUrl = R.drawable.carousel2 ),
        News(id = 3,imgUrl = R.drawable.carousel3 ),
        News(id = 4,imgUrl = R.drawable.carousel4 ),
    )

    var newsListLiveData: MutableLiveData<List<News>> = MutableLiveData()

    var headlineListLiveData: MutableLiveData<List<News>> = MutableLiveData()
    fun getHeadlinesData(){
        val headlines = headlineNewsList
        headlineListLiveData.value = headlines
    }
}
package com.ahmedfathy.articles.ui.articleInfo

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.data.ArticleDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ArticlesInfoViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    //get article data from previous screen to use it in showing data in webView
    val article = state.get<ArticleEntity>("article_info")
    //export this data as liveData
    val articleLiveData = MutableLiveData<ArticleEntity>()

    init {
        articleLiveData.value = article
    }

}
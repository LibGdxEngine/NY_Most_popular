package com.ahmedfathy.articles.ui.deleteallcompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val articleDao: ArticleDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        articleDao.deleteCompletedTasks()
    }
}
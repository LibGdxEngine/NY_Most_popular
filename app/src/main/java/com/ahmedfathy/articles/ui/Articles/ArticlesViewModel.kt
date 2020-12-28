package com.ahmedfathy.articles.ui.Articles

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ahmedfathy.articles.data.PreferencesManager
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.repository.MainRepository
import com.skydoves.pokedex.base.LiveCoroutinesViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ArticlesViewModel @ViewModelInject constructor(
    private val mainRepository : MainRepository,
    private val articleDao: ArticleDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : LiveCoroutinesViewModel() {

    val isLoading : MutableLiveData<Boolean> = MutableLiveData(true)

    private var articlesFromApi : LiveData<Flow<List<ArticleEntity>>>

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    init {
        articlesFromApi = launchOnViewModelScope {
            this.mainRepository.fetchArticlesList(
                onSuccess = { isLoading.postValue(false) },
                onError = { isLoading.postValue(false) }
            ).asLiveData()
        }
    }

    private val articlesFlow = combine(
        articlesFromApi.asFlow(),
        searchQuery.asFlow(),
        preferencesFlow
    ) { articles , query, filterPreferences ->
        Triple(articles , query, filterPreferences)
    }.flatMapLatest { (articles , query, filterPreferences) ->
        articleDao.getArticles(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    var articlesLiveData = articlesFlow.asLiveData()


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onArticleSelected(articleEntity: ArticleEntity) = viewModelScope.launch {
        articleDao.update(articleEntity.copy(completed = true))
        tasksEventChannel.send(TasksEvent.NavigateToArticleInfoScreen(articleEntity))
    }

    sealed class TasksEvent {
        data class NavigateToArticleInfoScreen(val articleEntity: ArticleEntity) : TasksEvent()
    }
}
package com.ahmedfathy.articles.ui.Articles

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ahmedfathy.articles.data.PreferencesManager
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.models.Article
import com.ahmedfathy.articles.repository.MainRepository
import com.ahmedfathy.articles.ui.ADD_TASK_RESULT_OK
import com.ahmedfathy.articles.ui.EDIT_TASK_RESULT_OK
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


    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    var articlesFromApi : LiveData<Flow<List<ArticleEntity>>>

    init {
        articlesFromApi = launchOnViewModelScope {
            this.mainRepository.fetchArticlesList(
                onSuccess = { isLoading.postValue(false) },
                onError = { isLoading.postValue(false) }
            ).asLiveData()
        }
    }


    private val tasksFlow = combine(
        articlesFromApi.asFlow(),
        searchQuery.asFlow(),
        preferencesFlow
    ) { articles , query, filterPreferences ->
        Triple(articles , query, filterPreferences)
    }.flatMapLatest { (articles , query, filterPreferences) ->
        articleDao.getArticles(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    var tasks = tasksFlow.asLiveData()


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(articleEntity: ArticleEntity) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(articleEntity))
    }

    fun onTaskSwiped(articleEntity: ArticleEntity) = viewModelScope.launch {
        articleDao.delete(articleEntity)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(articleEntity))
    }

    fun onUndoDeleteClick(articleEntity: ArticleEntity) = viewModelScope.launch {
        articleDao.insert(articleEntity)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val articleEntity: ArticleEntity) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val articleEntity: ArticleEntity) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}
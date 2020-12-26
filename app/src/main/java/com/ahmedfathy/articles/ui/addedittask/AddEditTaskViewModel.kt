package com.ahmedfathy.articles.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.ui.ADD_TASK_RESULT_OK
import com.ahmedfathy.articles.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val articleDao: ArticleDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<ArticleEntity>("task")

    var taskName = state.get<String>("taskName") ?: task?.title ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.readed ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(title = taskName, readed = taskImportance)
            updateTask(updatedTask)
        } else {
            val newTask = ArticleEntity(title = taskName, readed = taskImportance)
            createTask(newTask)
        }
    }

    private fun createTask(articleEntity: ArticleEntity) = viewModelScope.launch {
        articleDao.insert(articleEntity)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(articleEntity: ArticleEntity) = viewModelScope.launch {
        articleDao.update(articleEntity)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}
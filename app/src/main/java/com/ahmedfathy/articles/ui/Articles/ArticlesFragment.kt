package com.ahmedfathy.articles.ui.Articles

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedfathy.articles.R
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.databinding.FragmentArticlesBinding
import com.ahmedfathy.articles.util.exhaustive
import com.ahmedfathy.articles.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesFragment : Fragment(R.layout.fragment_articles), ArticlesAdapter.OnItemClickListener {

    private val viewModel: ArticlesViewModel by viewModels()

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArticlesBinding.bind(view)
        //main articles adapter
        val taskAdapter = ArticlesAdapter(this)

        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.articlesLiveData.observe(viewLifecycleOwner) {
            //update recycler data
            taskAdapter.submitList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            //observe loading state
            binding.progressBar.isVisible = isLoading
        }

        //collect data about screen events and take actions to respond
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is ArticlesViewModel.TasksEvent.NavigateToArticleInfoScreen -> {
                        val action =
                            ArticlesFragmentDirections.actionArticleFragmentToArticleInfoFragment(
                                "${event.articleEntity.section}",
                                      event.articleEntity
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(articleEntity: ArticleEntity) {
        viewModel.onArticleSelected(articleEntity)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        //use extension function here to make search query
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
        //get state of HideCompleted articles button from PreferencesManager
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //stop listening to user query after destroying this fragment
        searchView.setOnQueryTextListener(null)
    }


}
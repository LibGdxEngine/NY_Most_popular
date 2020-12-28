package com.ahmedfathy.articles.ui.addedittask

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ahmedfathy.articles.R
import com.ahmedfathy.articles.databinding.FragmentArticleInfoBinding
import com.ahmedfathy.articles.ui.articleInfo.ArticlesInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleInfoFragment : Fragment(R.layout.fragment_article_info) {

    private val viewModel: ArticlesInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArticleInfoBinding.bind(view)
        //show information in webview
        viewModel.articleLiveData.observe(viewLifecycleOwner) { article ->
            binding.apply {
                val webSettings: WebSettings = webView.settings
                webSettings.javaScriptEnabled = true
                //load url to webviewsout
                webView.loadUrl(article.url!!)
            }
        }
    }
}
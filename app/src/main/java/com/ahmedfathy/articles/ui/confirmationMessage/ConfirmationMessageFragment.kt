package com.ahmedfathy.articles.ui.confirmationMessage

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ahmedfathy.articles.ui.Articles.ArticlesFragmentDirections
import com.ahmedfathy.articles.ui.Articles.ArticlesViewModel
import com.ahmedfathy.articles.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ConfirmationMessageFragment : DialogFragment() {
    //A fragment used when user want to exit the app
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Hire Me")
            .setMessage("Will you hire me ?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finishAndRemoveTask()
            }
            .create()
}
package com.thecatapi.cats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.thecatapi.cats.databinding.FragmentCatListBinding
import com.thecatapi.cats.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint

class CatListFragment : Fragment() {

    private lateinit var binding: FragmentCatListBinding

    private var adapter = CatsAdapter()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cat_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCats().subscribe {
            GlobalScope.launch(Dispatchers.IO) {
                adapter.submitData(it)
            }
        }

    }

    private fun setupRecyclerView() {
        binding.catsRecyclerView.adapter = adapter
    }

}
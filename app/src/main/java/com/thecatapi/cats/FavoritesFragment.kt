package com.thecatapi.cats

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecatapi.cats.databinding.FragmentFavoritesBinding
import com.thecatapi.cats.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentFavoritesBinding

    private val compositeDisposable = CompositeDisposable()

    private val favoritesListener = object: (Int, FavoriteItemViewModel) -> Unit {
        override fun invoke(position: Int, catItem: FavoriteItemViewModel) {
            viewModel.removeFromFavorites(catItem.favoriteId, position)
        }
    }

    private val adapter = FavoritesAdapter(favoritesListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable.add(viewModel.loadFavorites().subscribe({},{ Timber.e(it)}))
        viewModel.favoritesLiveData.observe(this, {
            adapter.submitList(it)
        })
    }

    private fun setupRecyclerView() {
        binding.favoritesRecyclerView.adapter = adapter
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)

        val dividerItemDecoration = object: DividerItemDecoration(
            requireContext(),
            (binding.favoritesRecyclerView.layoutManager as LinearLayoutManager).orientation
        ) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(8, 8, 8, 8)
            }
        }.apply { setDrawable(drawable!!) }
        binding.favoritesRecyclerView.addItemDecoration(dividerItemDecoration)
    }
}
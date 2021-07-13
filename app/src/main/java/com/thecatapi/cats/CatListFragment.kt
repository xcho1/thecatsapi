package com.thecatapi.cats

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.thecatapi.cats.databinding.FragmentCatListBinding
import com.thecatapi.cats.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatListFragment : Fragment() {

    private lateinit var binding: FragmentCatListBinding

    private val itemClickHandler = object: (CatItemViewModel) -> Unit {
        override fun invoke(catItem: CatItemViewModel) {
            val action = CatsFragmentDirections.actionCatsFragmentToCatDetailsFragment(catItem.imageId,
                catItem.favoriteId ?: -1, catItem.breed)
            parentFragment?.findNavController()?.navigate(action)
        }
    }

    private val favoriteListener = object: (Int, CatItemViewModel) -> Unit {
        override fun invoke(position: Int, catItem: CatItemViewModel) {
            if (!catItem.isFavorite.get()) {
                viewModel.addToFavorites(catItem.imageId, position)
            } else {
                viewModel.removeFromFavorites(catItem.favoriteId, position)
            }
        }
    }

    private var adapter = CatsAdapter(itemClickHandler, favoriteListener)

    private val viewModel: MainViewModel by activityViewModels()

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
        (parentFragment as CatsFragment).performDropDownClick()
        viewModel.catsLiveData.observe(this, {
            GlobalScope.launch(Dispatchers.IO) {
                adapter.submitData(it)
            }
        })
        viewModel.favoriteResultLiveData.observe(this, {
            adapter.updateItemState(it.first, it.second)
        })
    }

    private fun setupRecyclerView() {
        binding.catsRecyclerView.adapter = adapter
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)

        val dividerItemDecoration = object: DividerItemDecoration(
            requireContext(),
            (binding.catsRecyclerView.layoutManager as GridLayoutManager).orientation
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
        binding.catsRecyclerView.addItemDecoration(dividerItemDecoration)
        (binding.catsRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
}
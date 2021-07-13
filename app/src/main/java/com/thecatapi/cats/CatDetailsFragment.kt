package com.thecatapi.cats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.thecatapi.cats.databinding.FragmentCatDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class CatDetailsFragment : Fragment() {

    private val args: CatDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentCatDetailsBinding

    private val viewModel: CatDetailsViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    private var favoriteId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cat_details, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(viewModel.loadCatDetails(args.imageId).subscribe({}, {Timber.e(it)}))

        binding.favoriteCheckbox.isChecked = args.favoriteId != -1

        favoriteId = args.favoriteId

        binding.favoriteCheckbox.setOnClickListener {
            if (binding.favoriteCheckbox.isChecked) {
                compositeDisposable.add(viewModel.addToFavorites(args.imageId)
                    .subscribe({ favoriteId = it.id }, { Timber.e(it) }))
            } else {
                compositeDisposable.add(viewModel.removeFromFavorites(favoriteId)
                    .subscribe({ favoriteId = null }, { Timber.e(it) }))
            }
        }

        binding.breedName.text = args.breed?.name
        binding.description.text = args.breed?.description
        binding.country.text = args.breed?.countryCode
        binding.dogFriendlyIndicator.indicatorBar.progress = args.breed?.dogFriendly ?: 1
        binding.childFriendlyIndicator.indicatorBar.progress = args.breed?.dogFriendly ?: 1
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
package com.thecatapi.cats

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.thecatapi.cats.databinding.FragmentCatsBinding
import com.thecatapi.cats.model.MainViewModel
import com.thecatapi.cats.util.FabAnimator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class CatsFragment : Fragment() {

    private val titles = arrayOf(R.string.cats_tab, R.string.favorites_tab)

    private lateinit var binding: FragmentCatsBinding

    private val viewModel: MainViewModel by activityViewModels()

    private var isFabRotated = false

    private var latestTmpUri: Uri? = null

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cats, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = SectionsPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(titles[position])
        }.attach()
        setupBreedDropDownMenu()
        setupMainFab()
        setupSmallFabs()
    }

    private fun setupBreedDropDownMenu() {

        val adapter = BreedListArrayAdapter(requireContext())
        viewModel.getBreeds()

        viewModel.breedLiveData.observe(viewLifecycleOwner, { menuItems ->
            adapter.addAll(menuItems)
        })
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            viewModel.loadCats(adapter.getItem(position).breedId).subscribeWith(viewModel.catDataObserver)
        }
    }

    private fun setupMainFab() {
        binding.fab.setOnClickListener {
            isFabRotated = FabAnimator.rotateFab(it, !isFabRotated)

            Timber.e("pizdec $isFabRotated")
            if(isFabRotated) {
                FabAnimator.showIn(binding.fabUploadFromCamera);
                FabAnimator.showIn(binding.fabUploadFromFile);
            } else{
                FabAnimator.showOut(binding.fabUploadFromCamera);
                FabAnimator.showOut(binding.fabUploadFromFile);
            }
        }
    }

    private fun setupSmallFabs() {
        binding.fabUploadFromCamera.setOnClickListener {
            takePicture()
        }
        binding.fabUploadFromFile.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun takePicture() {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takeImageResult.launch(uri)
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
//                previewImage.setImageURI(uri)
                Timber.e("$uri")
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".jpg", requireActivity().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext().applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}
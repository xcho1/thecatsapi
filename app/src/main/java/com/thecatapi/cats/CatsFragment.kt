package com.thecatapi.cats

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.switchMap
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thecatapi.cats.databinding.DialogImagePreviewBinding
import com.thecatapi.cats.databinding.FragmentCatsBinding
import com.thecatapi.cats.model.MainViewModel
import com.thecatapi.cats.util.ImageCache
import com.thecatapi.cats.util.FabAnimator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class CatsFragment : Fragment() {

    private val titles = arrayOf(R.string.cats_tab, R.string.favorites_tab)

    private lateinit var binding: FragmentCatsBinding

    private val viewModel: MainViewModel by activityViewModels()

    private var isFabRotated = false

    private var latestTmpUri: Uri? = null

    private var tmpFile: File? = null

    private val compositeDisposable = CompositeDisposable()

    var lastSelectedDropDownPosition: Int? = null

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                latestTmpUri = uri
                tmpFile = ImageCache.saveImgToCache(requireContext(), tmpFile!!, uri)
                showImageUploadDialog()
            }
        }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess && latestTmpUri != null) {
                showImageUploadDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tmpFile = ImageCache.createTempFile(requireContext())
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

        setupTabs()
        setupBreedDropDownMenu()
        setupMainFab()
        setupSmallFabs()
    }

    fun performDropDownClick() {
        if (lastSelectedDropDownPosition != null) {
            binding.autoCompleteTextView
                .onItemClickListener
                .onItemClick(null, null, lastSelectedDropDownPosition!!, 0)
        }
    }

    private fun setupTabs() {
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    binding.fab.hide()
                    binding.dropDownMenu.visibility = View.INVISIBLE
                } else {
                    binding.fab.show()
                    binding.dropDownMenu.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
        binding.viewPager.adapter = SectionsPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(titles[position])
        }.attach()
    }

    private fun setupBreedDropDownMenu() {

        val adapter = BreedListArrayAdapter(requireContext())

        viewModel.getBreeds().observe(viewLifecycleOwner, { menuItems ->
            adapter.add(BreedDropDownMenuItem("All Breeds", null))
            adapter.addAll(menuItems)
            val pos = if (lastSelectedDropDownPosition == null) 0 else lastSelectedDropDownPosition!!
            binding.autoCompleteTextView.onItemClickListener.onItemClick(null, null, pos, 0)
        })
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                Timber.e("${adapter.getItem(position).breedId} $position")
                lastSelectedDropDownPosition = position
                viewModel.loadCatsWithFavorites(adapter.getItem(position).breedId)
                    .subscribeWith(viewModel.catDataObserver)
            }
    }

    private fun setupMainFab() {
        binding.fab.setOnClickListener {
            isFabRotated = FabAnimator.rotateFab(it, !isFabRotated)

            if (isFabRotated) {
                FabAnimator.showIn(binding.fabUploadFromCamera);
                FabAnimator.showIn(binding.fabUploadFromFile);
            } else {
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
        tmpFile?.let { file ->
            latestTmpUri = ImageCache.toUri(requireContext(), file)
            takeImageResult.launch(latestTmpUri)
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun showImageUploadDialog() {
        val dialogBinding = DataBindingUtil.inflate<DialogImagePreviewBinding>(
            layoutInflater,
            R.layout.dialog_image_preview, null, false
        )

        dialogBinding.viewModel = viewModel

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .show()

        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, latestTmpUri)

        dialogBinding.imagePreview.setImageBitmap(bitmap)

        dialogBinding.cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialogBinding.uploadButton.setOnClickListener {
            compositeDisposable.add(viewModel.uploadImage(tmpFile!!)
                .subscribe({
                    dialog.dismiss()
                    Snackbar.make(binding.root, R.string.upload_success, Snackbar.LENGTH_SHORT).show()
                }, {
                    Timber.e(it)
                    Toast.makeText(requireContext(), R.string.upload_failure, Toast.LENGTH_LONG).show()
                }))
        }
    }
}
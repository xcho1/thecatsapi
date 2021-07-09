package com.thecatapi.cats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.thecatapi.cats.databinding.FragmentCatDetailsBinding

class CatDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCatDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cat_details, container, false)
        return binding.root
    }
}
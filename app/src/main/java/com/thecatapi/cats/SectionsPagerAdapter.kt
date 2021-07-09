package com.thecatapi.cats

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    companion object {
        private const val CAT_LIST_FRAGMENT_POSITION = 0
        private const val FAVORITES_FRAGMENT_POSITION = 1
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when(position) {
        CAT_LIST_FRAGMENT_POSITION -> CatListFragment()
        FAVORITES_FRAGMENT_POSITION -> FavoritesFragment()
        else -> Fragment()
    }

//    override fun getItem(position: Int): Fragment {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1)
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return context.resources.getString(TAB_TITLES[position])
//    }
//
//    override fun getCount(): Int {
//        // Show 2 total pages.
//        return 2
//    }
}
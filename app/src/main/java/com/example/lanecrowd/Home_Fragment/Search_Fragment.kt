package com.example.lanecrowd.Home_Fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.lancrowd.activity.home_fragments.Recent_Chat_Fragment
import com.example.lancrowd.activity.home_fragments.Tag_Fragment
import com.example.lanecrowd.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.content_home.*


class Search_Fragment : Fragment(), SearchView.OnQueryTextListener {


    var tsblayout: TabLayout? = null
    var viewPage: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }







private fun setupViewPager(viewPager: ViewPager) {
    val adapter = ViewPagerAdapter(this.requireFragmentManager())
    adapter.addFrag(Trending_Fragment(), "Trending")
    adapter.addFrag(People_Fragment(), "People")
    adapter.addFrag(Tag_Fragment(), "Tag")
    adapter.addFrag(Hashtag_Fragment(), "Hashtag")
    viewPager.adapter = adapter
}


private fun initViews(view:View) {

    viewPage = view.findViewById<ViewPager>(R.id.search_viewpager)
    viewPage!!.offscreenPageLimit=5
    setupViewPager(viewPage!!)

    tsblayout = view.findViewById<View>(R.id.search_tabs) as TabLayout
    tsblayout!!.setupWithViewPager(viewPage)


}



internal  class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList.get(position)
    }


}


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}

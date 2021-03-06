package com.example.lanecrowd.activity


import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lancrowd.activity.home_fragments.Recent_Chat_Fragment
import com.example.lanecrowd.Home_Fragment.Friend_Request_Fragment
import com.example.lanecrowd.Home_Fragment.Home_Post_Fragment
import com.example.lanecrowd.Home_Fragment.Notification_Fragment
import com.example.lanecrowd.Home_Fragment.Search_Fragment
import com.example.lanecrowd.R
import com.example.lanecrowd.Session_Package.SessionManager
import com.example.lanecrowd.util.URL
import com.example.lanecrowd.view_modal.MySessionVM
import com.example.lanecrowd.view_modal.factory.ViewModelProvider_Session
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity(), KodeinAware, NavigationView.OnNavigationItemSelectedListener {




    var drawer: DrawerLayout? = null
    var tabLayout: TabLayout? = null
    var viewPage: ViewPager? = null

    override val kodein by kodein()
    private val session: SessionManager by  instance()

    //this factory method will create and return one object of SessionVM
    var videomodelfactory: ViewModelProvider_Session? = null
    var mySessionVM: MySessionVM? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        initViews()
        set_Up_Tab_Text_And_Icons()

    }





    private fun set_Up_Tab_Text_And_Icons() {
        tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.home_icon)
        tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.search_icon)
        tabLayout!!.getTabAt(2)!!.setIcon(R.drawable.message_icon)
        tabLayout!!.getTabAt(3)!!.setIcon(R.drawable.friend_icon_2)
        tabLayout!!.getTabAt(4)!!.setIcon(R.drawable.notification_icon)


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(Home_Post_Fragment(), "")
        adapter.addFrag(Search_Fragment(), "")
        adapter.addFrag(Recent_Chat_Fragment(), "")
        adapter.addFrag(Friend_Request_Fragment(), "")
        adapter.addFrag(Notification_Fragment(), "")
        viewPager.adapter = adapter
    }


    private fun initViews() {

      //  session = SessionManager(applicationContext)
        //this factory method will create and return one object
        videomodelfactory =
            ViewModelProvider_Session(
                MySessionVM.instance
            )
        mySessionVM = ViewModelProvider(this, videomodelfactory!!).get(MySessionVM::class.java)


        storeUserSessionValueTOURLCLass()

        viewPage = findViewById<ViewPager>(R.id.viewpager)
        viewPage!!.offscreenPageLimit = 5
        setupViewPager(viewPage!!)






        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPage)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)

        val listHeader = LayoutInflater.from(baseContext).inflate(R.layout.nav_header_home, null)

        val iv_cover = headerView.findViewById<ImageView>(R.id.iv_cover)
        val iv_profile = headerView.findViewById<ImageView>(R.id.iv_profile)
        val txt_name = headerView.findViewById<TextView>(R.id.txt_name)
        val txt_mail = headerView.findViewById<TextView>(R.id.txt_mail)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)



        setProfileANdCoverImageandName(iv_profile, iv_cover, txt_name, txt_mail)

        headerView.setOnClickListener(View.OnClickListener {
            closeDrawerLayout()
            startActivity(
                Intent(
                    applicationContext,
                    Profile_Activity::class.java
                )
                    .putExtra("postUserId", URL.userId)

                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        })


        //change the navigation title color
        val menu = navigationView.menu

        val more_nav = menu.findItem(R.id.more_nav)

        val s3 = SpannableString(more_nav.title)

        s3.setSpan(TextAppearanceSpan(this, R.style.Navigation_View_TITLE), 0, s3.length, 0)

        more_nav.title = s3
        navigationView.setNavigationItemSelectedListener(this)
        //end change the navigation title color
    }

    private fun setProfileANdCoverImageandName(
        profileImage: ImageView?,
        headerView: ImageView?,
        txtName: TextView,
        txtMail: TextView) {

        val user: HashMap<String, String?> = session!!.userDetails
        setUseDataTOVIew(txtName, txtMail, profileImage, headerView, user)



        mySessionVM!!.getName()!!.observe(this, Observer { resultPi ->


            setUseDataTOVIew(txtName, txtMail, profileImage, headerView, resultPi)


        })


    }

    private fun setUseDataTOVIew(
        txtName: TextView,
        txtMail: TextView,
        profileImage: ImageView?,
        headerView: ImageView?,
        resultPi: HashMap<String, String?>
    ) {


        txtName.text = resultPi[SessionManager.KEY_FULL_NAME]


            txtMail.text = resultPi[SessionManager.KEY_EMAILPHONE]



        Glide.with(baseContext)
            .load(URL.profilePicPath + resultPi[SessionManager.KEY_PROFILE_PICTURE])
            .apply(RequestOptions().placeholder(R.drawable.placeholder_profile))
            .thumbnail(0.01f).into(profileImage!!)

        Glide.with(baseContext)
            .load(URL.coverPicPath + resultPi[SessionManager.KEY_COVER_PHOTO]).apply(
                RequestOptions().placeholder(R.drawable.placeholder)
            ).thumbnail(0.01f).into(headerView!!)



        println("ObserChanged")
        println("KEY_PROFILE_PICTURE" + URL.profilePicPath + resultPi[SessionManager.KEY_PROFILE_PICTURE])
        println("KEY_COVER_PHOTO" + URL.coverPicPath + resultPi[SessionManager.KEY_COVER_PHOTO])

    }

    private fun storeUserSessionValueTOURLCLass() {

        // get user data from session
        val user: HashMap<String, String?> = session!!.userDetails


        val KEY_USERID = user[SessionManager.KEY_USERID]
        val KEY_FULL_NAME = user[SessionManager.KEY_FULL_NAME]
        val KEY_EMAILPHONE = user[SessionManager.KEY_EMAILPHONE]
        val KEY_PASSWORD = user[SessionManager.KEY_PASSWORD]
        val KEY_PROFILE_PICTURE = user[SessionManager.KEY_PROFILE_PICTURE]
        val KEY_COVER_PHOTO = user[SessionManager.KEY_COVER_PHOTO]
        val KEY_DOB = user[SessionManager.KEY_DOB]
        val KEY_GENDER = user[SessionManager.KEY_GENDER]

        URL.userId = KEY_USERID!!
        URL.fullName = KEY_FULL_NAME!!
        URL.emailphone = KEY_EMAILPHONE!!
        URL.password = KEY_PASSWORD!!
        URL.profilePic = KEY_PROFILE_PICTURE!!
        URL.coverPic = KEY_COVER_PHOTO!!
        URL.dob = KEY_DOB!!
        URL.gender = KEY_GENDER!!


        println("activity_password"+URL.emailphone+" "+URL.password)


    }

    private fun closeDrawerLayout() {
        drawer!!.closeDrawer(GravityCompat.START)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        /*if (id == R.id.mi_search) {
            startActivity(new Intent(getApplicationContext(),Search_Products_Activity.class));
            // return true;
        }*/

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        val id = item.itemId

        if (id == R.id.activity) {
            closeDrawerLayout()
            startActivity(
                Intent(
                    applicationContext,
                    Post_Activity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        } else if (id == R.id.friends) {
            closeDrawerLayout()
            startActivity(
                Intent(
                    applicationContext,
                    My_Friend_Activity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        } else if (id == R.id.photos) {
            closeDrawerLayout()
            startActivity(
                Intent(applicationContext, Show_Photo_Video_Album_Activity::class.java)
                    .putExtra("from", "photo")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        } else if (id == R.id.videos) {
            closeDrawerLayout()
            startActivity(
                Intent(applicationContext, Show_Photo_Video_Album_Activity::class.java)
                    .putExtra("from", "video")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        } else if (id == R.id.logout) {
            closeDrawerLayout()
            session!!.logoutUser()
        }

        /*
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_body) {

        } else if (id == R.id.nav_face) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_makeup) {

        }  else if (id == R.id.nav_cart) {

        } else if (id == R.id.navigation_locate) {

        } else if (id == R.id.navigation_care) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }*/

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
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


    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                  finishAffinity()
              //  super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true

            DynamicToast.make(applicationContext, "Please click back again to exit").show()

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

        }
    }


}


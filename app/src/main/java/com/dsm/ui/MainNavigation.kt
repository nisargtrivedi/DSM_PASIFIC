package com.dsm.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsm.R
import com.dsm.databinding.ActivityMainNavigationBinding
import com.dsm.ui.adapter.NavMenuAdapter
import com.dsm.ui.adapter.SearchItemAdapter
import com.dsm.ui.fragment.*
import com.dsm.ui.listener.onSearchItemClick
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.NavMenuModel
import com.dsm.ui.model.SearchItemModel


class MainNavigation : BaseActivity() {

    lateinit var binding: ActivityMainNavigationBinding
    lateinit var mDrawerToggle : ActionBarDrawerToggle

    var searchList:MutableList<String> = mutableListOf()
    var categortList : ArrayList<JewelleryCategoryModel> = ArrayList()
    var selectedPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
        setupToolbar()
        setupDrawerToggle()
        loadMenuItems()
    }
    fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun setupDrawerToggle() {
        mDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_name
        )
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState()
        mDrawerToggle.isDrawerIndicatorEnabled = false;
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu)


        mDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun loadMenuItems(){
        val drawerItem: Array<NavMenuModel?> = arrayOfNulls<NavMenuModel>(4)

        drawerItem[0] = NavMenuModel(R.drawable.ic_diamond, "Diamonds")
        drawerItem[1] = NavMenuModel(R.drawable.ic_ring, "Jewellery")
        drawerItem[2] = NavMenuModel(R.drawable.ic_account, "My account")
        drawerItem[3] = NavMenuModel(0, "<u>Logout</u>")
//        drawerItem[3] = NavMenuModel(0, getString(R.string.logout))
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)

        val adapter = NavMenuAdapter(this, R.layout.menu_row, drawerItem)
        binding.lstMenu.adapter = adapter
        binding.lstMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            selectItem(i)
        }
        if(intent.extras!=null){
            if(intent.getStringExtra("portal")=="j"){
                selectItem(1)
            }else{
                selectItem(0)
            }
        }

    }


    fun selectItem(position: Int) {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = HomeFragment()
                setTitle(resources.getString(R.string.toolbar_text))
            }1 -> {
                fragment = JewelleryFragment()
                setTitle(resources.getString(R.string.jewellery_text))
            }2 -> {
                fragment = HomeFragment()
                setTitle(resources.getString(R.string.toolbar_text))
            }10 -> {
                fragment = InventoryFragment()
                setTitle(resources.getString(R.string.toolbar_text))
                binding.lstMenu.setItemChecked(0, true)
                binding.lstMenu.setSelection(0)
            }
            else -> {
            }
        }
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, fragment).commit()
            if(position!=10) {
                binding.lstMenu.setItemChecked(position, true)
                binding.lstMenu.setSelection(position)
            }
            //setTitle(mNavigationDrawerItemTitles.get(position))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }

    fun setTitle(s:String){
        binding.toolbarText.text = s
    }

    fun subCategoryFragment(categoryID:JewelleryCategoryModel) {
        var fragment: Fragment? = null
            fragment = JewellerySubCategoryFragment.newInstance(categoryID)
            setTitle(resources.getString(R.string.jewellery_text))
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, fragment).addToBackStack("SubCat").commit()
            //setTitle(mNavigationDrawerItemTitles.get(position))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }

    fun jewelleryListFragment(subcategoryID:JewelleryCategoryModel) {
        var fragment: Fragment? = null
        fragment = JewelleryListFragment.newInstance(subcategoryID)
        setTitle(resources.getString(R.string.jewellery_text))
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, fragment).addToBackStack("list").commit()
            //setTitle(mNavigationDrawerItemTitles.get(position))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }

    fun jewelleryCategoryFragment() {
        var fragment: Fragment? = null
        fragment = JewelleryFragment.newInstance()
        setTitle(resources.getString(R.string.jewellery_text))
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, fragment)
                .commit()
            //setTitle(mNavigationDrawerItemTitles.get(position))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }

    fun searchFragment() {
        var fragment: Fragment? = null
        fragment = SearchFragment.newInstance()
        setTitle(resources.getString(R.string.toolbar_text))
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, fragment).commit()
            //setTitle(mNavigationDrawerItemTitles.get(position))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        Log.i("Count------?",count.toString())
        if (count == 1) {
            //val fragmentPopped: Boolean = supportFragmentManager.popBackStackImmediate("SubCat", 0)
            //if(fragmentPopped) {
                supportFragmentManager.popBackStack(
                    "SubCat",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                    //}
        }else if (count == 2) {
            supportFragmentManager.popBackStack(
                "list",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        } else {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit from application?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        run {
                            finishAffinity()
                            super.onBackPressed()
                        }
                    })
                .setNegativeButton("No", null)
                .show()
        }
    }


    fun setCategoryList(list:List<JewelleryCategoryModel>) : ArrayList<JewelleryCategoryModel>{
        categortList.clear()
        categortList.addAll(list)
        return categortList
    }
    fun setSelection(spinner: Spinner, aID: Int) {
        var size = categortList.size - 1
        for(i in 0..size){
            if(categortList[i].attribute_id == aID){
                spinner.setSelection(i)
            }
        }
    }
    fun setLastSelection(aID: Int) {
        selectedPosition = aID
    }
    fun getLastSelection():Int{
        return selectedPosition
    }


    fun loadSearchItem(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this, 4)
        rv.setHasFixedSize(true)
        val models: MutableList<SearchItemModel> = java.util.ArrayList()
        models.add(SearchItemModel("ROUND"))
        models.add(SearchItemModel("PRINCESS"))
        models.add(SearchItemModel("CUSHION"))
        models.add(SearchItemModel("CUSHION B"))
        models.add(SearchItemModel("PEAR"))
        models.add(SearchItemModel("OVAL"))
        models.add(SearchItemModel("MARQUISE"))
        models.add(SearchItemModel("HEART"))
        models.add(SearchItemModel("EMERALD"))
        models.add(SearchItemModel("EMERALD SQ"))
        models.add(SearchItemModel("RADIANT"))
        models.add(SearchItemModel("RADIANT SQ"))
        val adapter = SearchItemAdapter(this, models, 1)
        rv.adapter = adapter
        adapter.onJwelleryClick(object : onSearchItemClick {
            override fun onClick(obj: SearchItemModel) {
                obj.isSelected = !obj.isSelected
                addShape(obj.name)
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun loadColor(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this, 9)
        rv.setHasFixedSize(true)
        val models: MutableList<SearchItemModel> = java.util.ArrayList()
        models.add(SearchItemModel("D"))
        models.add(SearchItemModel("E"))
        models.add(SearchItemModel("F"))
        models.add(SearchItemModel("G"))
        models.add(SearchItemModel("H"))
        models.add(SearchItemModel("I"))
        models.add(SearchItemModel("J"))
        models.add(SearchItemModel("K"))
        models.add(SearchItemModel("L"))
        models.add(SearchItemModel("M"))
        models.add(SearchItemModel("N"))
        val adapter = SearchItemAdapter(this, models, 2)

        rv.adapter = adapter
        adapter.onJwelleryClick(object : onSearchItemClick {
            override fun onClick(obj: SearchItemModel) {
                obj.isSelected = !obj.isSelected
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun loadCut(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this, 1)
        rv.setHasFixedSize(true)
        val models: MutableList<SearchItemModel> = java.util.ArrayList()
        models.add(SearchItemModel("EX"))
        models.add(SearchItemModel("VG"))
        models.add(SearchItemModel("GD"))
        models.add(SearchItemModel("G"))
        models.add(SearchItemModel("F"))
        val adapter = SearchItemAdapter(this, models, 2)

        rv.adapter = adapter
        adapter.onJwelleryClick(object : onSearchItemClick {
            override fun onClick(obj: SearchItemModel) {
                obj.isSelected = !obj.isSelected
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun loadClarity(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.setHasFixedSize(true)
        val models: MutableList<SearchItemModel> = java.util.ArrayList()
        models.add(SearchItemModel("IF"))
        models.add(SearchItemModel("VVS"))
        models.add(SearchItemModel("VVS1"))
        models.add(SearchItemModel("VVS2"))
        models.add(SearchItemModel("VS"))
        models.add(SearchItemModel("VS1"))
        models.add(SearchItemModel("VS2"))
        models.add(SearchItemModel("SI"))
        models.add(SearchItemModel("SI1"))
        models.add(SearchItemModel("SI2"))
        models.add(SearchItemModel("SI3"))
        models.add(SearchItemModel("I1"))
        models.add(SearchItemModel("I2"))
        models.add(SearchItemModel("I3"))
        val adapter = SearchItemAdapter(this, models, 2)

        rv.adapter = adapter
        adapter.onJwelleryClick(object : onSearchItemClick {
            override fun onClick(obj: SearchItemModel) {
                obj.isSelected = !obj.isSelected
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun addShape(shape:String){
        if(searchList.contains(shapeKey(shape))){
            searchList.remove(shapeKey(shape))
        }else{
            searchList.add(shapeKey(shape))
        }
        //Log.e("TAG---",android.text.TextUtils.join(",", searchList))
    }
    fun shapeKey(shape: String) : String{
        var key = ""
        when(shape.toUpperCase()){
            "ROUND" -> key="2441442"
            "PRINCESS" -> key="78"
            "CUSHION" -> key="19"
            "CUSHION B" -> key="85"
            "PEAR" -> key="17"
            "OVAL" -> key="88"
            "MARQUISE" -> key="84"
            "HEART" -> key="83"
            "EMERALD" -> key="77"
            "EMERALD SQ" -> key="76"
            "RADIANT" -> key="87"
            "RADIANT SQ" -> key="218"
        }
        return key
    }
}
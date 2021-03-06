package com.dsm.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.dsm.ui.model.labModel
import com.dsm.ui.util.AppPreferences
import com.dsm.ui.util.Cognito
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainNavigation : BaseActivity() , CoroutineScope {

    lateinit var binding: ActivityMainNavigationBinding
    lateinit var mDrawerToggle : ActionBarDrawerToggle

    var searchList:MutableList<String> = mutableListOf()
    var ClarityList:MutableList<String> = mutableListOf()
    var diaList:MutableList<String> = mutableListOf()
    var cutList:MutableList<String> = mutableListOf()
    var polList:MutableList<String> = mutableListOf()
    var symList:MutableList<String> = mutableListOf()

    var edtCaratList:MutableList<String> = mutableListOf()
    var edtPriceList:MutableList<String> = mutableListOf()


    var labsList:MutableList<labModel> = mutableListOf()


    var searchEditext = ""
    var lab = ""
    var categortList : ArrayList<JewelleryCategoryModel> = ArrayList()
    var selectedPosition = -1
    var cognito: Cognito? = null
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    lateinit var appPreferences: AppPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreferences= AppPreferences(this)
        cognito = Cognito(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
        setupToolbar()
        job = Job()
        launch {
            setupDrawerToggle()
            loadMenuItems()
        }

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
            }
            1 -> {
                fragment = JewelleryFragment()
                setTitle(resources.getString(R.string.jewellery_text))
            }
            2 -> {
                fragment = GeneralSettingsFragment()
                setTitle(resources.getString(R.string.toolbar_text))
            }
            10 -> {
                fragment = InventoryFragment()
                setTitle(resources.getString(R.string.toolbar_text))
                binding.lstMenu.setItemChecked(0, true)
                binding.lstMenu.setSelection(0)
            }
            3 -> {
                try {
                    val dialogBuilder = AlertDialog.Builder(this)
                    val inflater: LayoutInflater = getLayoutInflater()
                    val dialogView: View = inflater.inflate(R.layout.dialog_confirm, null)
                    dialogBuilder.setView(dialogView)
                    dialogBuilder.setCancelable(false)
                    val btnCancel: MaterialButton = dialogView.findViewById(R.id.btnCancel)
                    val btnOk: MaterialButton = dialogView.findViewById(R.id.btnOk)
                    val alertDialog = dialogBuilder.create()
                    alertDialog.show()
                    btnOk.setOnClickListener {
                        appPreferences.set("USERNAME", "")
                        appPreferences.set("EMAIL","")
                        cognito!!.userLogout(this, appPreferences.getString("USERNAME"),alertDialog)
                        //getActivity().finish();
                    }
                    btnCancel.setOnClickListener { alertDialog.dismiss() }
                } catch (ex: Exception) {
                }
            }
            else -> {
            }
        }
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
                R.id.content_frame,
                fragment
            ).commit()
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

    fun setTitle(s: String){
        binding.toolbarText.text = s
    }

    fun subCategoryFragment(categoryID: JewelleryCategoryModel) {
        var fragment: Fragment?
            fragment = JewellerySubCategoryFragment.newInstance(categoryID)
            setTitle(resources.getString(R.string.jewellery_text))
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
            R.id.content_frame,
            fragment
        ).addToBackStack("SubCat").commit()
        //setTitle(mNavigationDrawerItemTitles.get(position))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun jewelleryListFragment(subcategoryID: JewelleryCategoryModel) {
        var fragment: Fragment?
        fragment = JewelleryListFragment.newInstance(subcategoryID)
        setTitle(resources.getString(R.string.jewellery_text))
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
            R.id.content_frame,
            fragment
        ).addToBackStack("list").commit()
        //setTitle(mNavigationDrawerItemTitles.get(position))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun jewelleryCategoryFragment() {
        var fragment: Fragment?
        fragment = JewelleryFragment.newInstance()
        setTitle(resources.getString(R.string.jewellery_text))
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
            R.id.content_frame,
            fragment
        )
            .commit()
        //setTitle(mNavigationDrawerItemTitles.get(position))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun searchFragment() {
        var fragment: Fragment?
        fragment = SearchFragment.newInstance()
        setTitle(resources.getString(R.string.toolbar_text))
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
            R.id.content_frame,
            fragment
        ).commit()
        //setTitle(mNavigationDrawerItemTitles.get(position))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun ProfileScreens() {
        var fragment: Fragment?
        fragment = ProfileFragment.newInstance()
        setTitle(resources.getString(R.string.profile_setting))
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
            R.id.content_frame,
            fragment
        ).commit()
        //setTitle(mNavigationDrawerItemTitles.get(position))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        Log.i("Count------?", count.toString())
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
                    DialogInterface.OnClickListener { _, _ ->
                        run {
                            finishAffinity()
                            super.onBackPressed()
                        }
                    })
                .setNegativeButton("No", null)
                .show()
        }
    }


    fun setCategoryList(list: List<JewelleryCategoryModel>) : ArrayList<JewelleryCategoryModel>{
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
        rv.layoutManager = GridLayoutManager(this@MainNavigation, 4)
        rv.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {

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
                val adapter = SearchItemAdapter(this@MainNavigation, models, 1)
                rv.adapter = adapter
                adapter.onJwelleryClick(object : onSearchItemClick {
                    override fun onClick(obj: SearchItemModel) {
                        obj.isSelected = !obj.isSelected
                        addShape(obj.name)
                        adapter.notifyDataSetChanged()
                    }
                })

                if(searchList.size>0) {
                    for (i in 0..models.size-1) {
                            if (searchList.contains(shapeKey(models[i].name))) {
                                models[i].isSelected = true
                            }
                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

    fun loadColor(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this@MainNavigation, 9)
        rv.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {

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
                val adapter = SearchItemAdapter(this@MainNavigation, models, 2)

                rv.adapter = adapter
                adapter.onJwelleryClick(object : onSearchItemClick {
                    override fun onClick(obj: SearchItemModel) {
                        obj.isSelected = !obj.isSelected
                        addClarity(obj.name)
                        adapter.notifyDataSetChanged()
                    }
                })
                if(ClarityList.size>0) {
                    for (i in 0..models.size-1) {
                        if (ClarityList.contains(clerityKey(models[i].name))) {
                            models[i].isSelected = true
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

    fun loadCut(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this@MainNavigation, 1)
        rv.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {

                val models: MutableList<SearchItemModel> = java.util.ArrayList()
                models.add(SearchItemModel("EX"))
                models.add(SearchItemModel("VG"))
                models.add(SearchItemModel("GD"))
                models.add(SearchItemModel("G"))
                models.add(SearchItemModel("F"))
                val adapter = SearchItemAdapter(this@MainNavigation, models, 2)

                rv.adapter = adapter
                adapter.onJwelleryClick(object : onSearchItemClick {
                    override fun onClick(obj: SearchItemModel) {
                        obj.isSelected = !obj.isSelected
                        addCut(obj.name)
                        adapter.notifyDataSetChanged()
                    }
                })
                if(cutList.size>0) {
                    for (i in 0..models.size-1) {
                        if (cutList.contains(FcutKey(models[i].name))) {
                            models[i].isSelected = true
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

    fun loadPol(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this@MainNavigation, 1)
        rv.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {

                val models: MutableList<SearchItemModel> = java.util.ArrayList()
                models.add(SearchItemModel("EX"))
                models.add(SearchItemModel("VG"))
                models.add(SearchItemModel("GD"))
                models.add(SearchItemModel("G"))
                models.add(SearchItemModel("F"))
                val adapter = SearchItemAdapter(this@MainNavigation, models, 2)

                rv.adapter = adapter
                adapter.onJwelleryClick(object : onSearchItemClick {
                    override fun onClick(obj: SearchItemModel) {
                        obj.isSelected = !obj.isSelected
                        addPolKey(obj.name)
                        adapter.notifyDataSetChanged()
                    }
                })
                if(polList.size>0) {
                    for (i in 0..models.size-1) {
                        if (polList.contains(diaPolKey(models[i].name))) {
                            models[i].isSelected = true
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

    fun loadSym(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this@MainNavigation, 1)
        rv.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {

                val models: MutableList<SearchItemModel> = java.util.ArrayList()
                models.add(SearchItemModel("EX"))
                models.add(SearchItemModel("VG"))
                models.add(SearchItemModel("GD"))
                models.add(SearchItemModel("G"))
                models.add(SearchItemModel("F"))
                val adapter = SearchItemAdapter(this@MainNavigation, models, 2)

                rv.adapter = adapter
                adapter.onJwelleryClick(object : onSearchItemClick {
                    override fun onClick(obj: SearchItemModel) {
                        obj.isSelected = !obj.isSelected
                        addSymKey(obj.name)
                        adapter.notifyDataSetChanged()
                    }
                })
                if(symList.size>0) {
                    for (i in 0..models.size-1) {
                        if (symList.contains(diasymKey(models[i].name))) {
                            models[i].isSelected = true
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

    fun loadClarity(rv: RecyclerView) {
        rv.layoutManager = GridLayoutManager(this@MainNavigation, 2)
        rv.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {

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
                val adapter = SearchItemAdapter(this@MainNavigation, models, 2)

                rv.adapter = adapter
                adapter.onJwelleryClick(object : onSearchItemClick {
                    override fun onClick(obj: SearchItemModel) {
                        obj.isSelected = !obj.isSelected
                        addDia(obj.name)
                        adapter.notifyDataSetChanged()
                    }
                })
                if(diaList.size>0) {
                    for (i in 0..models.size-1) {
                        if (diaList.contains(diaKey(models[i].name))) {
                            models[i].isSelected = true
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

    fun addShape(shape: String){
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
            "ROUND" -> key = "2,441,442"
            "PRINCESS" -> key = "78"
            "CUSHION" -> key = "19"
            "CUSHION B" -> key = "85"
            "PEAR" -> key = "17"
            "OVAL" -> key = "88"
            "MARQUISE" -> key = "84"
            "HEART" -> key = "83"
            "EMERALD" -> key = "77"
            "EMERALD SQ" -> key = "76"
            "RADIANT" -> key = "87"
            "RADIANT SQ" -> key = "218"
        }
        return key
    }


    fun addClarity(shape: String){
        if(ClarityList.contains(clerityKey(shape))){
            ClarityList.remove(clerityKey(shape))
        }else{
            ClarityList.add(clerityKey(shape))
        }
        //Log.e("TAG---",android.text.TextUtils.join(",", searchList))
    }

    fun clerityKey(shape: String) : String{
        var key = ""
        when(shape.toUpperCase()){
            "D" -> key = "3"
            "E" -> key = "11"
            "F" -> key = "22"
            "G" -> key = "23"
            "H" -> key = "26"
            "I" -> key = "27"
            "J" -> key = "28"
            "K" -> key = "29"
            "L" -> key = "30"
            "M" -> key = "31"
            "N" -> key = "32"
        }
        return key
    }



    fun addDia(shape: String){
        if(diaList.contains(diaKey(shape))){
            diaList.remove(diaKey(shape))
        }else{
            diaList.add(diaKey(shape))
        }
        //Log.e("TAG---",android.text.TextUtils.join(",", searchList))
    }
    fun diaKey(shape: String) : String{
        var key = ""
        when(shape.toUpperCase()){
            "IF" -> key = "188"
            "VVS" -> key = "238"
            "VVS1" -> key = "4"
            "VVS2" -> key = "12"
            "VS" -> key = "220"
            "VS1" -> key = "34"
            "VS2" -> key = "35"
            "SI" -> key = "219"
            "SI1" -> key = "36"
            "SI2" -> key = "37"
            "SI3" -> key = "189"
            "I1" -> key = "38"
            "I2" -> key = "38"
            "I3" -> key = "38"
        }
        return key
    }

    fun addCut(shape: String){
        if(cutList.contains(FcutKey(shape))){
            cutList.remove(FcutKey(shape))
        }else{
            cutList.add(FcutKey(shape))
        }
        //Log.e("TAG---",android.text.TextUtils.join(",", searchList))
    }
    fun FcutKey(shape: String) : String{
        var key = ""
        when(shape.toUpperCase()){
            "EX" -> key = "6"
            "VG" -> key = "13"
            "GD" -> key = "46"
            "G" -> key = "47"
            "F" -> key = "48" }
        return key
    }


    fun addPolKey(shape: String){
        if(polList.contains(diaPolKey(shape))){
            polList.remove(diaPolKey(shape))
        }else{
            polList.add(diaPolKey(shape))
        }
        //Log.e("TAG---",android.text.TextUtils.join(",", searchList))
    }
    fun diaPolKey(shape: String) : String{
        var key = ""
        when(shape.toUpperCase()){
            "EX" -> key = "7"
            "VG" -> key = "14"
            "GD" -> key = "52"
            "G" -> key = "53"
            "F" -> key = "54" }
        return key
    }


    fun addSymKey(shape: String){
        if(symList.contains(diasymKey(shape))){
            symList.remove(diasymKey(shape))
        }else{
            symList.add(diasymKey(shape))
        }
        //Log.e("TAG---",android.text.TextUtils.join(",", searchList))
    }
    fun diasymKey(shape: String) : String{
        var key = ""
        when(shape.toUpperCase()){
            "EX" -> key = "8"
            "VG" -> key = "15"
            "GD" -> key = "49"
            "G" -> key = "50"
            "F" -> key = "51" }
        return key
    }
}
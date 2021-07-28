package com.dsm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsm.R
import com.dsm.databinding.FragmentInventoryBinding
import com.dsm.ui.MainNavigation
import com.dsm.ui.adapter.InventoryAdapter
import com.dsm.ui.listener.onDialogClick
import com.dsm.ui.model.DiamondModel
import com.dsm.ui.model.ShapeModel
import com.dsm.ui.network.RetrofitBuilder
import com.dsm.ui.util.Status
import com.dsm.ui.viewmodel.DiamondViewModel
import com.dsm.ui.viewmodel.ShapeViewModel
import com.dsm.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class InventoryFragment : BaseFragment(),CoroutineScope  {

    lateinit var binding:FragmentInventoryBinding
    var diamondList: MutableList<DiamondModel> =  ArrayList()
    var shapeList: MutableList<ShapeModel> =  ArrayList()
    lateinit var inventoryAdapter: InventoryAdapter

    private var isLoading: Boolean = false
    var pageNo=0
    var endPageNo=1
    private lateinit var job: Job
    lateinit var shapeViewModel: ShapeViewModel
    private lateinit var layoutManager : LinearLayoutManager
    // context for io thread
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_inventory, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        loadData()
        onFloatingButtonClick()
        onSendEmail()
        getShapes()
    }

    private fun loadData(){
        binding.rvInventory.setHasFixedSize(true)

        launch {

            withContext(Dispatchers.Main) {
                layoutManager = LinearLayoutManager(activity)
                binding.rvInventory.layoutManager = layoutManager
                inventoryAdapter = InventoryAdapter(activity?.baseContext!!, diamondList)

//                binding.rvInventory.layoutManager = LinearLayoutManager(
//                    activity,
//                    LinearLayoutManager.VERTICAL,
//                    false
//                )
                binding.rvInventory.adapter = inventoryAdapter

                inventoryAdapter.DialogOpen(object : onDialogClick {
                    override fun onDialogOpen(obj:DiamondModel) {
                        (context as MainNavigation).openSelectedImageDialog(obj.diamond_img_path,obj.diamond_lot_no)
                    }
                })

                binding.rvInventory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (!isLoading) {
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == diamondList.size - 1) {
                                if(diamondList.size>0) {
                                    isLoading = true
                                    getDiamondFromShape(shapeList[binding.tabShapes.selectedTabPosition].shapeID.toString())
                                }
                            }
                        }
                    }
                })

            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
    private fun onFloatingButtonClick(){
        binding.floatingActionButton.setOnClickListener {
            if(binding.tvDiamongEnquiry.visibility == View.GONE && binding.tvSelectAll.visibility == View.GONE){
                binding.tvDiamongEnquiry.visibility = View.VISIBLE
                binding.tvSelectAll.visibility = View.VISIBLE
                binding.floatingActionButton.setImageResource(R.drawable.ic_close)
            }else{
                binding.tvDiamongEnquiry.visibility = View.GONE
                binding.tvSelectAll.visibility = View.GONE
                binding.floatingActionButton.setImageResource(R.drawable.ic_flat_menu)
            }
        }
        binding.edtSearch.setOnClickListener{
            (context as MainNavigation).searchFragment()
        }
    }
    private fun onSendEmail(){
        binding.tvDiamongEnquiry.setOnClickListener {
            (context as MainNavigation).openDialog()
        }
    }

    //Load Data from API
    private fun getShapes(){
            shapeViewModel= ViewModelProvider(this,ViewModelFactory(RetrofitBuilder.apiService)).get(ShapeViewModel::class.java)
            shapeViewModel.getAllShapes().observe(viewLifecycleOwner, Observer {
                it?.let {
                        resource ->
                    when(resource.status){
                        Status.LOADING ->{
                           showLoading(activity)
                        }Status.SUCCESS ->{
                            hideLoading()
                            if(resource.data!!.ResponseStatus==200){
                                if(resource.data.list!=null && resource.data.list.list.isNotEmpty()){
                                    shapeList.clear()
                                    shapeList.addAll(resource.data.list.list)
                                    retrieveShapeList(resource.data.list.list)
                                }
                            }
                        }Status.ERROR ->{
                        hideLoading()
                        }
                    }
                }
            })
    }

    fun getDiamondFromShape(shape:String){
        if(pageNo>endPageNo){
            showToast("No More Diamond Details found",activity)
            isLoading = false
            return
        }

        var search = if((context as MainNavigation).searchList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).searchList) +"]"
        var clr = if((context as MainNavigation).ClarityList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).ClarityList) +"]"
        var dia = if((context as MainNavigation).diaList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).diaList) +"]"

        var cut = if((context as MainNavigation).cutList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).cutList) +"]"
        var pol = if((context as MainNavigation).polList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).polList) +"]"
        var sym = if((context as MainNavigation).symList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).symList) +"]"

        var edtSearch =  if((context as MainNavigation).searchEditext.isNullOrEmpty()) null else (context as MainNavigation).searchEditext

        var diamondViewModel  = ViewModelProvider(this,ViewModelFactory(RetrofitBuilder.apiService)).get(DiamondViewModel::class.java)
        pageNo++
        diamondViewModel.getAllDiamonds("harsh.shah@siimteq.com",shape,pageNo,edtSearch,null,null,search,null,clr,dia,null).observe(requireActivity(),{
            it?.let {
                    resource ->
                when(resource.status){
                    Status.LOADING ->{
                        showLoading(activity)
                    }Status.SUCCESS ->{
                    hideLoading()
                    if(resource.data!!.ResponseStatus==200){
                       inventoryAdapter.addPermission(resource.data.diamondData.permissionModel)
                        if(resource.data!!.diamondData.permissionModel.withdraw_website_access){
                            binding.rvInventory.visibility = View.GONE
                        }else{
                            binding.rvInventory.visibility = View.VISIBLE
                            if(resource.data.diamondData!=null && resource.data.diamondData.diamonds.data.isNotEmpty()){
                                //diamondList.clear()
                                diamondList.addAll(resource.data.diamondData.diamonds.data)
                                //diamondList.notifyAll()
                                endPageNo = resource.data.diamondData.diamonds.last_page
                                isLoading = false
                                inventoryAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }Status.ERROR ->{
                    hideLoading()
                }
                }
            }
        })
    }

    private fun retrieveShapeList(shapes : List<ShapeModel>){
        for(i in shapes) {
            try {
                binding.tabShapes.addTab(
                    binding.tabShapes.newTab().setText(i.shapeName).setId(i.shapeID.toInt())
                )
            }catch (e:Exception){
                showToast(e.toString(),activity)
            }
        }
        binding.tabShapes.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //showToast(tab!!.id.toString(),activity)
                pageNo=0
                diamondList.clear()
                inventoryAdapter.notifyDataSetChanged()
                binding.rvInventory.scrollToPosition(0)

                getDiamondFromShape(tab!!.id.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //showToast(tab!!.position.toString() +"Reselet",activity)
            }
        })
        getDiamondFromShape(shapes[0].shapeID)
    }


}
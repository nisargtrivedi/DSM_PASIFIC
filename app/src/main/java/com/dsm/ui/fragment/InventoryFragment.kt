package com.dsm.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
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
import com.dsm.ui.listener.onPriceLinkClick
import com.dsm.ui.model.DiamondModel
import com.dsm.ui.model.ShapeModel
import com.dsm.ui.network.RetrofitBuilder
import com.dsm.ui.util.AppPreferences
import com.dsm.ui.util.MailCredentials.EmailTemplate
import com.dsm.ui.util.MailCredentials.MAIL
import com.dsm.ui.util.MailCredentials.PASSWORD
import com.dsm.ui.util.MailSender
import com.dsm.ui.util.Status
import com.dsm.ui.util.onDiamondClick
import com.dsm.ui.viewmodel.DiamondViewModel
import com.dsm.ui.viewmodel.SendMailViewModel
import com.dsm.ui.viewmodel.ShapeViewModel
import com.dsm.ui.viewmodel.ViewModelFactory
import com.google.android.material.button.MaterialButton
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
    var currentPage=0
    private lateinit var job: Job
    lateinit var shapeViewModel: ShapeViewModel
    lateinit var mailViewModel: SendMailViewModel
    private lateinit var layoutManager : LinearLayoutManager

    lateinit var preferences: AppPreferences
    var selecteddiamondlistforenquiry:MutableList<String> = mutableListOf()

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
        preferences= AppPreferences(activity)
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
                    override fun onDialogOpen(obj: DiamondModel) {
                        (context as MainNavigation).openSelectedImageDialog(obj.diamond_img_path, obj.diamond_lot_no + " "+ obj.diamond_clr +" "+obj.diamond_cla+" "+obj.diamond_size)
                    }
                })
                inventoryAdapter.onPriceClick(object :onPriceLinkClick{
                    override fun onPriceLink(obj: DiamondModel) {
                        showPriceLink(obj)

                    }
                })

                binding.rvInventory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (!isLoading) {
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == diamondList.size - 1) {
                                if (diamondList.size > 0) {
                                    isLoading = true
                                    getDiamondFromShape(shapeList[binding.tabShapes.selectedTabPosition].shapeID.toString())
                                }
                            }
                        }
                    }
                })
                inventoryAdapter.onClick(object : onDiamondClick {
                    override fun onClick(task: DiamondModel) {
                        if (task.isCheched == 1)
                            task.isCheched = 0
                        else {
                            task.isCheched = 1
                        }
                        if(selecteddiamondlistforenquiry.contains(task.diamond_id)){
                            selecteddiamondlistforenquiry.remove(task.diamond_id)
                        }else{
                            selecteddiamondlistforenquiry.add(task.diamond_id)
                        }
                        inventoryAdapter.notifyDataSetChanged()
                    }

                })

                binding.btnClear.setOnClickListener {
                    (context as MainNavigation).searchList.clear()
                    (context as MainNavigation).ClarityList.clear()
                    (context as MainNavigation).diaList.clear()
                    (context as MainNavigation).cutList.clear()
                    (context as MainNavigation).polList.clear()
                    (context as MainNavigation).symList.clear()
                    (context as MainNavigation).edtCaratList.clear()
                    (context as MainNavigation).edtPriceList.clear()
                    (context as MainNavigation).searchEditext = ""
                    (context as MainNavigation).lab = ""
                    pageNo = 0
                    endPageNo = 1
                    currentPage = 0
                    diamondList.clear()
                    inventoryAdapter.notifyDataSetChanged()
                    binding.rvInventory.scrollToPosition(0)
                    getShapes()
                }
            }
          }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
    private fun onFloatingButtonClick(){
        binding.floatingActionButton.setOnClickListener {

            if(selecteddiamondlistforenquiry.size>0) {

                if (binding.tvDiamongEnquiry.visibility == View.GONE) {
                    binding.tvDiamongEnquiry.visibility = View.VISIBLE
                    binding.tvSelectAll.visibility = View.GONE
                    binding.floatingActionButton.setImageResource(R.drawable.ic_close)
                } else {
                    binding.tvDiamongEnquiry.visibility = View.GONE
                    binding.tvSelectAll.visibility = View.GONE
                    binding.floatingActionButton.setImageResource(R.drawable.ic_flat_menu)
                }
            }else{
                (context as MainNavigation).showToast("Please select any diamond")
            }
        }

        binding.edtSearch.setOnClickListener{
            (context as MainNavigation).searchFragment()
        }
    }
    private fun onSendEmail(){
        binding.tvDiamongEnquiry.setOnClickListener {
                openDialog(android.text.TextUtils.join(",", selecteddiamondlistforenquiry))
        }
    }

    //Load Data from API
    private fun getShapes(){
            shapeViewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(ShapeViewModel::class.java)
            shapeViewModel.getAllShapes().observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {
                            showLoading(activity)
                        }
                        Status.SUCCESS -> {
                            hideLoading()
                            if (resource.data!!.ResponseStatus == 200) {
                                if (resource.data.list.list.isNotEmpty()) {
                                    shapeList.clear()
                                    shapeList.addAll(resource.data.list.list)
                                    retrieveShapeList(resource.data.list.list)
                                }
                            }
                        }
                        Status.ERROR -> {
                            hideLoading()
                        }
                    }
                }
            })
    }


    fun getDiamondFromShape(shape: String){
        Log.d("Data","Page no is $pageNo and End Page is $endPageNo")
        if(pageNo>endPageNo){
            showToast("No More Diamond Details found", activity)
            isLoading = false
            return
        }else if(currentPage==endPageNo){
            showToast("No More Diamond Details found", activity)
            isLoading = false
            return
        }
        if(!(context as MainNavigation).searchList.isNullOrEmpty()){
            binding.tabShapes.visibility = View.GONE
            binding.btnClear.visibility = View.VISIBLE
        }else{
            binding.btnClear.visibility = View.GONE
            binding.tabShapes.visibility = View.VISIBLE
        }
        var search = if((context as MainNavigation).searchList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).searchList)
        var clr = if((context as MainNavigation).ClarityList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).ClarityList)
        var dia = if((context as MainNavigation).diaList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).diaList)

        var cut = if((context as MainNavigation).cutList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).cutList)
        var pol = if((context as MainNavigation).polList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).polList)
        var sym = if((context as MainNavigation).symList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).symList)

        var edtSearch =  if((context as MainNavigation).searchEditext.isNullOrEmpty()) null else (context as MainNavigation).searchEditext
        var searchLab =  if((context as MainNavigation).lab.isNullOrEmpty()) null else (context as MainNavigation).lab

        var srchCarat = if((context as MainNavigation).edtCaratList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).edtCaratList)
        var srchPrice = if((context as MainNavigation).edtPriceList.isNullOrEmpty()) null else android.text.TextUtils.join(",", (context as MainNavigation).edtPriceList)


        var diamondViewModel  = ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(DiamondViewModel::class.java)
        pageNo++
        diamondViewModel.getAllDiamonds(preferences.getString("EMAIL"), if(search.isNullOrEmpty()) shape else search, pageNo, edtSearch, srchCarat, null, null, searchLab, clr, dia, srchPrice, cut, pol, sym).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(activity)
                    }
                    Status.SUCCESS -> {
                        Log.d("Error-","Call Success")
                        hideLoading()
                        if (resource.data!!.ResponseStatus == 200) {
                            inventoryAdapter.addPermission(resource.data.diamondData.permissionModel)
                            if (resource.data.diamondData.permissionModel.withdraw_website_access) {
                                binding.rvInventory.visibility = View.GONE
                                binding.tvMsg.visibility = View.VISIBLE
                                binding.tvMsg.text = resource.message
                            } else {
                                //binding.rvInventory.visibility = View.VISIBLE
                                    if(resource.data.diamondData.diamonds!=null) {
                                        if (!resource.data.diamondData.diamonds.data.isNullOrEmpty()) {
                                            Log.d("Error-", "Call If")
                                            //diamondList.clear()
                                            diamondList.addAll(resource.data.diamondData.diamonds.data)
                                            //diamondList.notifyAll()
                                            currentPage = resource.data.diamondData.diamonds.current_page
                                            endPageNo = resource.data.diamondData.diamonds.last_page
                                            if (resource.data.diamondData.lookUpData.labsList.size > 0) {
                                                (context as MainNavigation).labsList.clear()
                                                (context as MainNavigation).labsList.addAll(resource.data.diamondData.lookUpData.labsList)
                                            }
                                            isLoading = false
                                            inventoryAdapter.notifyDataSetChanged()
                                            binding.tvMsg.visibility = View.GONE
                                            binding.rvInventory.visibility = View.VISIBLE
                                        } else {
                                            Log.d("Error-", "Call Else")
                                            binding.tvMsg.visibility = View.VISIBLE
                                            binding.rvInventory.visibility = View.GONE
                                        }
                                    }else{
                                        binding.tvMsg.visibility = View.VISIBLE
                                        binding.rvInventory.visibility = View.GONE
                                    }
                            }
                            preferences.set("COMPANY",resource.data.diamondData.userModel.company_name+"")
                        }
                        else{
                            binding.rvInventory.visibility = View.GONE
                            binding.tvMsg.visibility = View.VISIBLE
                            binding.tvMsg.text = resource.message
                        }

                    }
                    Status.ERROR -> {
                        hideLoading()
                        binding.tvMsg.visibility = View.VISIBLE
                        binding.rvInventory.visibility = View.GONE
                        Log.d("Error-","Call Error")
                    }
                }
            }
        })
    }

    private fun retrieveShapeList(shapes: List<ShapeModel>){
        for(i in shapes) {
            try {
                binding.tabShapes.addTab(
                        binding.tabShapes.newTab().setText(i.shapeName).setId(i.shapeID.toInt())
                )
            }catch (e: Exception){
                showToast(e.toString(), activity)
            }
        }
        binding.tabShapes.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //showToast(tab!!.id.toString(),activity)
                (context as MainNavigation).searchList.clear()
                pageNo = 0
                endPageNo = 1
                currentPage = 0
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



    var onCheck = true
    private fun openDialog(msg: String) {

                val builder1 = AlertDialog.Builder(requireActivity())
                val view = LayoutInflater.from(requireActivity()).inflate(R.layout.email_dialog, null, false)
                builder1.setView(view)
                builder1.setCancelable(true)
                val alert11 = builder1.create()
                alert11.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alert11.show()
                val btnClose = view.findViewById<TextView>(R.id.btnClose)
                val tvCheck = view.findViewById<TextView>(R.id.tvCheck)
                val edtEmail = view.findViewById<EditText>(R.id.edtEmail)
                val edtMessage=view.findViewById<AppCompatEditText>(R.id.edtMessage)
                val btnSend: MaterialButton = view.findViewById(R.id.btnSend)
                btnSend.setOnClickListener {

                    when {
                        TextUtils.isEmpty(edtEmail.text.toString()) -> {
                            (context as MainNavigation).showToast("Please enter email")
                        }
                        TextUtils.isEmpty(edtMessage.text.toString()) -> {
                            (context as MainNavigation).showToast("Please enter message")
                        }
                        selecteddiamondlistforenquiry.size<=0 ->{
                            (context as MainNavigation).showToast("Please select any diamond")
                        }
                        else -> {
                            sendMailAPI(
                                edtEmail.text.toString(),
                                edtMessage.text.toString(),
                                msg,
                                if (onCheck) "Yes" else "No",
                                    alert11
                            )

                        }
                    }
                }
                tvCheck.setOnClickListener {
                    onCheck = if (onCheck) {
                        tvCheck.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_rectangle, 0, 0, 0)
                        false
                    } else {
                        tvCheck.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_chk, 0, 0, 0)
                        true
                    }
                }
                btnClose.setOnClickListener {
                    alert11.cancel()
                    alert11.dismiss()
                }
    }

    private fun sendMailAPI(email:String,message:String,id:String,send:String,alert:AlertDialog){
        mailViewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(SendMailViewModel::class.java)
        mailViewModel.sendMail(email=email,message = message,enquiryID=id,send_me_also = send).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(activity)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        (context as MainNavigation).showToast(if(resource.data!!.ResponseMessage=="OK")"Enquiry has been submitted successfully" else resource.data!!.ResponseMessage)
                        alert.cancel()
                        alert.dismiss()
                        if (binding.tvDiamongEnquiry.visibility == View.GONE) {
                            binding.tvDiamongEnquiry.visibility = View.VISIBLE
                            binding.tvSelectAll.visibility = View.GONE
                            binding.floatingActionButton.setImageResource(R.drawable.ic_close)
                        } else {
                            binding.tvDiamongEnquiry.visibility = View.GONE
                            binding.tvSelectAll.visibility = View.GONE
                            binding.floatingActionButton.setImageResource(R.drawable.ic_flat_menu)
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        alert.cancel()
                        alert.dismiss()
                    }
                }
            }
        })
    }


    private fun showPriceLink(obj:DiamondModel){
        var diamondViewModel  = ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(DiamondViewModel::class.java)
        diamondViewModel.getPrice(preferences.getString("ACCESS_TOKEN"),preferences.getString("USERNAME"),obj.location,obj.diamond_id.toString()).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(activity)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        if (resource.data!!.ResponseStatus == 200) {
                            obj.isshowPrice=1
                            inventoryAdapter.notifyDataSetChanged()
                               // Log.e("CAlledddd-------------","----------------------")
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                    }
                }
            }
        })
    }
//    Function for Send Mail From Android Application
//    fun sendDiamondsEnquiry(diamondList:DiamondModel): String {
//        var i = 0
//
//        data.append("<table border='1' width='100%'>")
//        data.append("<tr><td>Lot No</td><td>Location</td><td>Carat</td><td>Lab</td><td>Certificate</td><td>Shape</td><td>CLR</td><td>CLA</td><td>FLR</td><td>FCUT</td><td>POL</td><td>SYM</td><td>TAB</td><td>DEP</td><td>Measurement</td><td>STATUS</td><td>SELLING PRICE</td></tr>")
//
//
//        if (diamondList.isCheched == 1) {
//            data.append("<tr>")
//            data.append("<td>" + diamondList.diamond_lot_no + "</td>")
//            data.append("<td>" + diamondList.location + "</td>")
//            data.append("<td>" + diamondList.diamond_size + "</td>")
//            data.append("<td>" + diamondList.diamond_lab + "</td>")
//            data.append("<td>" + diamondList.diamond_cert + "</td>")
//            data.append("<td>" + diamondList.diamond_shape + "</td>")
//            data.append("<td>" + diamondList.diamond_clr + "</td>")
//            data.append("<td>" + diamondList.diamond_cla + "</td>")
//            data.append("<td>" + diamondList.diamond_flr + "</td>")
//            data.append("<td>" + diamondList.diamond_fcut + "</td>")
//            data.append("<td>" + diamondList.diamond_pol + "</td>")
//            data.append("<td>" + diamondList.diamond_sym + "</td>")
//            data.append("<td>" + diamondList.diamond_tab + "</td>")
//            data.append("<td>" + diamondList.diamond_dep + "</td>")
//            data.append("<td>" + diamondList.diamond_meas1 + "x" + diamondList.diamond_meas2 + "x" + diamondList.diamond_meas3 + "</td>")
//            data.append("<td>" + diamondList.diamond_status + "</td>")
//            data.append("<td>" + diamondList.diamond_selling_price + "</td>")
//            data.append("</tr>")
//        }
//
//        data.append("</table>")
//
//        return data.toString()+"<br>"
//    }
}
package com.dsm.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsm.R
import com.dsm.databinding.ActivityItemDetailsBinding
import com.dsm.ui.adapter.DetailImageAdapter
import com.dsm.ui.adapter.JewelleryAdapter
import com.dsm.ui.adapter.ModelAdapter
import com.dsm.ui.listener.onJwelleryClick
import com.dsm.ui.model.JModels
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.JewelleryModel
import com.dsm.ui.network.RetrofitBuilder
import com.dsm.ui.util.AppPreferences
import com.dsm.ui.util.Status
import com.dsm.ui.viewmodel.JewelleryDetailViewModel
import com.dsm.ui.viewmodel.JewelleryListViewModel
import com.dsm.ui.viewmodel.SendMailViewModel
import com.dsm.ui.viewmodel.ViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailActivity : BaseActivity() , CoroutineScope {
    lateinit var binding: ActivityItemDetailsBinding
    lateinit var obj: JewelleryModel
    lateinit var objModel: JModels

    private lateinit var job: Job

    var imagesList: ArrayList<JewelleryModel.Images> = ArrayList()
    var images: ArrayList<String> = ArrayList()
    lateinit var mailViewModel: SendMailViewModel
    lateinit var imagesAdapter: DetailImageAdapter
    lateinit var metalAdapter: ModelAdapter
    // context for io thread
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    var id="0"

    var pName="N/A"
    var pmodelNo="N/A"
    var pmetal="N/A"
    var pdesc="N/A"
    var pprice="N/A"
    lateinit var appPreferences:AppPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_details)
        job = Job()
        setupToolbar()
        loadData()
        btnClick()
        appPreferences= AppPreferences(this)
    }
    private fun setupToolbar() {

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })
    }
    private fun loadData() {

        binding.rvSimilarItems.setHasFixedSize(true)
        try {
            val viewModel: JewelleryDetailViewModel =
                ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(
                    JewelleryDetailViewModel::class.java
                )

            if(intent.getSerializableExtra("detail")!= null)
            {
                obj = intent.getSerializableExtra("detail") as JewelleryModel
                viewModel.getAllJewelleryDetail(obj.jewelleryID.toString())
                    .observe(this@DetailActivity, Observer {

                        it?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {
                                    showLoading(this@DetailActivity)
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    if (resource.data!!.ResponseStatus == 200) {
                                        if (resource.data.list.isJewellery) {

                                            if (resource.data.list.jObj != null) {
                                                id= obj.jewelleryID.toString()

                                                pName =  resource.data.list.jObj!!.jewelleryfrontproductname
                                                pdesc =  resource.data.list.jObj!!.jewellery_description
                                                pmetal = resource.data.list.jObj!!.jewellery_metal
                                                pmodelNo = resource.data.list.jObj!!.jewelleryLotNo
                                                pprice = resource.data.list.jObj!!.jewellery_price

                                                binding.tvmodelNo.text = "Lot No :"
                                                binding.llMetal.visibility = View.GONE
                                                binding.spMetals.visibility = View.GONE
                                                binding.llCarat.visibility = View.VISIBLE
                                                binding.llTDW.visibility = View.VISIBLE
                                                binding.llWeight.visibility = View.VISIBLE

                                                binding.tvJewelleryName.text =
                                                    resource.data.list.jObj!!.jewelleryfrontproductname
                                                binding.tvCarat.text =
                                                    resource.data.list.jObj!!.jewellery_carat
                                                binding.tvDescription.text =
                                                    resource.data.list.jObj!!.jewellery_description
                                                binding.tvMetalName.text =
                                                    resource.data.list.jObj!!.jewellery_metal
                                                binding.tvModelNo.text =
                                                    resource.data.list.jObj!!.jewelleryLotNo
                                                binding.tvTDW.text =
                                                    resource.data.list.jObj!!.jewellery_tdw
                                                binding.tvWeight.text =
                                                    resource.data.list.jObj!!.gold_weight
                                                binding.tvPrice.text =
                                                    resource.data.list.jObj!!.jewellery_price + " + GST"

                                                if (!resource.data.list.jObj!!.image.isNullOrEmpty()) {
                                                    Glide.with(this)
                                                        .load(resource.data.list.jObj!!.image)
                                                        .into(binding.img);
                                                } else {
                                                    Glide.with(this)
                                                        .load(R.drawable.noimage)
                                                        .into(binding.img);
                                                }
                                                binding.rlImages.visibility = View.GONE
                                                binding.img.visibility = View.VISIBLE
                                            }

                                        } else {
                                            showToast("No Data Found.")
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
            else if(intent.getSerializableExtra("model_detail") != null){
                objModel = intent.getSerializableExtra("model_detail") as JModels
                viewModel.getAllJewelleryDetail("model_"+objModel.dataID.toString())
                    .observe(this@DetailActivity, Observer {
                        it?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {
                                    showLoading(this)
                                }
                                Status.SUCCESS -> {
                                    hideLoading()
                                    if (resource.data!!.ResponseStatus == 200) {
                                        if (!resource.data.list.isJewellery) {

                                            if (resource.data.list.listModel != null) {

                                                binding.tvmodelNo.text = "Model No :"
                                                binding.llMetal.visibility = View.VISIBLE
                                                binding.spMetals.visibility = View.VISIBLE
                                                binding.tvMetalName.visibility = View.GONE


                                                binding.llCarat.visibility = View.GONE
                                                //binding.llTDW.visibility = View.GONE
                                                binding.llWeight.visibility = View.GONE

                                                id= objModel.dataID.toString()



                                                binding.tvJewelleryName.text =
                                                    resource.data.list.listModel!!.jewelleryfrontproductname
                                                binding.tvCarat.text =
                                                    resource.data.list.listModel!!.jewellery_carat
                                                binding.tvDescription.text =
                                                    resource.data.list.listModel!!.jewellery_description
                                                binding.tvMetalName.text =
                                                    resource.data.list.listModel!!.jewellery_metal
                                                binding.tvModelNo.text =
                                                    resource.data.list.listModel!!.jewelleryLotNo
                                                binding.tvTDW.text =
                                                    resource.data.list.listModel!!.jewellery_tdw
                                                binding.tvWeight.text =
                                                    resource.data.list.listModel!!.gold_weight
                                                binding.tvPrice.text =
                                                    resource.data.list.listModel!!.jewellery_price + " + GST"

                                                imagesList.clear()
                                                imagesList.addAll(resource.data.list.listModel!!.images)

                                                binding.rlImages.visibility = View.VISIBLE
                                                binding.img.visibility = View.GONE

                                                images.clear()
                                                imagesAdapter = DetailImageAdapter(this@DetailActivity,images)
                                                binding.rlImages.layoutManager = GridLayoutManager(
                                                    this,
                                                    1
                                                )
                                                binding.rlImages.adapter = imagesAdapter

                                                metalAdapter = ModelAdapter(this@DetailActivity,
                                                    resource.data.list.listModel!!.metalsList
                                                )
                                                binding.spMetals.adapter = metalAdapter

                                                binding.spMetals.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                                                    override fun onItemSelected(
                                                        parent: AdapterView<*>?,
                                                        view: View?,
                                                        position: Int,
                                                        id: Long
                                                    ) {
                                                        var metal : JewelleryModel.Metals = parent!!.getItemAtPosition(position) as JewelleryModel.Metals
                                                            for(i in 0..imagesList.size){
                                                                if(metal.metalID== imagesList.get(i).attribute_id){
                                                                    images.clear()
                                                                    images.addAll(imagesList.get(i).images)
                                                                    imagesAdapter.notifyDataSetChanged()
                                                                    break
                                                                }
                                                            }
                                                    }

                                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                                    }

                                                }
                                                try {
                                                    pName = resource.data.list.listModel!!.jewelleryfrontproductname
                                                    pdesc = resource.data.list.listModel!!.jewellery_description
                                                    pmetal = binding.spMetals.selectedItem.toString()
                                                    pmodelNo = resource.data.list.listModel!!.jewelleryLotNo
                                                    pprice = resource.data.list.listModel!!.jewellery_price
                                                }catch (e:Exception){
                                                    showToast(e.toString())
                                                }


//                                                if (!resource.data.list.listModel!!.image.isNullOrEmpty()) {
//                                                    Glide.with(DetailActivity@ this)
//                                                        .load(resource.data.list.listModel!!.image)
//                                                        .into(binding.img);
//                                                } else {
//                                                    Glide.with(DetailActivity@ this)
//                                                        .load(R.drawable.noimage)
//                                                        .into(binding.img);
//                                                }
                                            }

                                        } else {
                                            showToast("No Data Found.")
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
        }catch (e:Exception){
            Log.e("Error",e.toString())
            showToast("Details not fetching properly")
        }
    }
    private fun btnClick(){
        binding.btnEnquiry.setOnClickListener {
            Dialog()
        }
    }
    private fun Dialog() {

        val builder1 = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.email_dialog, null, false)
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
                   showToast("Please enter email")
                }
                TextUtils.isEmpty(edtMessage.text.toString()) -> {
                    showToast("Please enter message")
                }
                else -> {
                    sendMailAPI(appPreferences.getString("EMAIL"),
                    edtMessage.text.toString())
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

    private fun sendMailAPI(email:String,message:String){


        mailViewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(
            SendMailViewModel::class.java)
        mailViewModel.sendJewelleryMail(email=email,pName,pmodelNo,pmetal,pdesc
        ,pprice,message).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(this)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        if (resource.data!!.ResponseStatus == 200) {
                           showToast(resource.message)
                        }else{
                            showToast(resource.message)
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
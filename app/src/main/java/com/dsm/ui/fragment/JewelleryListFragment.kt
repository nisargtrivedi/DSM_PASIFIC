package com.dsm.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dsm.R
import com.dsm.databinding.FragmentJewelleryBinding
import com.dsm.ui.DetailActivity
import com.dsm.ui.MainNavigation
import com.dsm.ui.adapter.JewelleryListAdapter
import com.dsm.ui.adapter.JewelleryListModelAdapter
import com.dsm.ui.adapter.SpinnerAdapter
import com.dsm.ui.listener.onJwelleryDetailClick
import com.dsm.ui.listener.onJwelleryModelClick
import com.dsm.ui.model.JModels
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.JewelleryModel
import com.dsm.ui.network.RetrofitBuilder
import com.dsm.ui.util.Status
import com.dsm.ui.viewmodel.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class JewelleryListFragment : BaseFragment(),CoroutineScope,AdapterView.OnItemSelectedListener,View.OnTouchListener   {


    lateinit var binding:FragmentJewelleryBinding
    var jewelleryList: MutableList<JewelleryModel> =  ArrayList()
    var jmodelList: MutableList<JModels> =  ArrayList()
    lateinit var jewelleryListViewModel: JewelleryListViewModel

    lateinit var jewelleryAdapter: JewelleryListAdapter
    lateinit var jewelleryModelAdapter: JewelleryListModelAdapter
    lateinit var spinnerAdapter: SpinnerAdapter


    private lateinit var job: Job
    lateinit var categoryModel : JewelleryCategoryModel
    private var isFirst = false

    // context for io thread
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    companion object {
        const val ARG_NAME = "categoryID"


        fun newInstance(name: JewelleryCategoryModel): JewelleryListFragment {
            val fragment = JewelleryListFragment()

            val bundle = Bundle().apply {
                putSerializable(
                    ARG_NAME, name)
            }

            fragment.arguments = bundle

            return fragment
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        categoryModel = arguments?.getSerializable(ARG_NAME) as JewelleryCategoryModel
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_jewellery, container, false
        )

        var view : View = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        loadCategory()
        loadData(categoryModel.attribute_id.toString())
        sortingPrice()
    }
    private fun loadData(categoryID:String){
        jewelleryListViewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(
                JewelleryListViewModel::class.java)
        jewelleryListViewModel.getAllJewelleryList(categoryID).observe(viewLifecycleOwner, Observer {
                it?.let {
                        resource ->
                    when(resource.status){
                        Status.LOADING ->{
                            showLoading(activity)
                        }
                        Status.SUCCESS ->{
                        hideLoading()
                        if(resource.data!!.ResponseStatus==200){
                            if(resource.data.list!=null && resource.data.list.list.isNotEmpty()){
                                jewelleryList.clear()
                                jewelleryList.addAll(resource.data.list.list)
                                jewelleryAdapter.notifyDataSetChanged()
                                if(jewelleryList.size<=0){
                                    binding.rvJewellery.visibility = View.GONE
                                }else{
                                    binding.rvJewellery.visibility = View.VISIBLE
                                }
                            }

                            if(resource.data.list!=null && resource.data.list.listModel.isNotEmpty()){
                                jmodelList.clear()
                                jmodelList.addAll(resource.data.list.listModel)
                                jewelleryModelAdapter.notifyDataSetChanged()
                                if(jmodelList.size<=0){
                                    binding.rvModel.visibility = View.GONE
                                }else{
                                    binding.rvModel.visibility = View.VISIBLE
                                }
                            }
                            binding.tvItemCount.text  = (jewelleryList.size + jmodelList.size).toString() + " product(s) found."

                        }
                    }
                        Status.ERROR ->{
                        hideLoading()
                    }
                    }
                }
            })
    }
    private fun loadCategory(){
        binding.rvJewellery.setHasFixedSize(true)
        binding.rvModel.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main){
                binding.rvJewellery.layoutManager = GridLayoutManager(
                    activity,
                    2
                )

                jewelleryAdapter = JewelleryListAdapter(activity?.baseContext!!, jewelleryList)
                binding.rvJewellery.adapter = jewelleryAdapter


                binding.rvModel.layoutManager = GridLayoutManager(
                    activity,
                    2
                )

                jewelleryModelAdapter = JewelleryListModelAdapter(activity?.baseContext!!, jmodelList)
                binding.rvModel.adapter = jewelleryModelAdapter

                spinnerAdapter = SpinnerAdapter(activity?.baseContext!!,(activity as MainNavigation).categortList)
                binding.spCategory.adapter = spinnerAdapter
                binding.spCategory.isSelected = false;  // otherwise listener will be called on initialization
                binding.spCategory.setSelection(0,true);
                binding.spCategory.setOnTouchListener(this@JewelleryListFragment)
                binding.spCategory.onItemSelectedListener = this@JewelleryListFragment
                //binding.spCategory.setSelection((activity as MainNavigation).getLastSelection())


                val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(activity?.baseContext!!,
                    R.array.price, R.layout.price_row)

                binding.spSorting.adapter = adapter

                jewelleryAdapter.onJwelleryClick(object : onJwelleryDetailClick {
                    override fun onClick(obj: JewelleryModel) {
//                        binding.tvCategory.text = obj.attribute_name
//                        binding.tvItemCount.text  = jewelleryList.size.toString() + " product(s) found."
//                        binding.llHeader.visibility = View.VISIBLE
//                        binding.tvCategory.visibility = View.VISIBLE
//                        binding.line.visibility = View.VISIBLE
//                        binding.tvItemCount.visibility = View.VISIBLE

                        startActivity(

                                Intent(activity, DetailActivity::class.java)
                                        .putExtra("detail",obj)
                        )
                    }
                })

                jewelleryModelAdapter.onJwelleryClick(object : onJwelleryModelClick {
                    override fun onClick(obj: JModels) {
//                        binding.tvCategory.text = obj.attribute_name
//                        binding.tvItemCount.text  = jewelleryList.size.toString() + " product(s) found."
//                        binding.llHeader.visibility = View.VISIBLE
//                        binding.tvCategory.visibility = View.VISIBLE
//                        binding.line.visibility = View.VISIBLE
//                        binding.tvItemCount.visibility = View.VISIBLE

                        startActivity(

                            Intent(activity, DetailActivity::class.java)
                                .putExtra("model_detail",obj)
                        )
                    }
                })


                binding.tvCategory.text = categoryModel.attribute_name
                binding.llHeader.visibility = View.VISIBLE
                binding.tvSortBy.visibility = View.VISIBLE
                binding.spSorting.visibility = View.VISIBLE
                binding.tvCategory.visibility = View.VISIBLE
                binding.line.visibility = View.VISIBLE
                binding.tvItemCount.visibility = View.VISIBLE



            }
        }
    }
    private fun sortingPrice(){
        binding.spSorting.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position==1){
                    if(jewelleryList.size>0) {
                        jewelleryList.sortBy {

                            it.jewelleryfrontcostaud.toInt()

                        }
                        jmodelList.sortBy {
                            it.price.toInt()
                        }
                        jewelleryAdapter.notifyDataSetChanged()
                        jewelleryModelAdapter.notifyDataSetChanged()
                    }

                }else if(position==2){
                    if(jewelleryList.size>0) {
                        jewelleryList.sortByDescending {
                            it.jewelleryfrontcostaud.toInt()
                        }
                        jmodelList.sortByDescending {
                            it.price.toInt()
                        }
                        jewelleryAdapter.notifyDataSetChanged()
                        jewelleryModelAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        if(isFirst){
//            isFirst = false
//        }else{
        if(isFirst){
            isFirst=false

            var obj=parent!!.getItemAtPosition(position) as JewelleryCategoryModel
            (activity as MainNavigation).subCategoryFragment(obj)
            //(activity as MainNavigation).jewelleryCategoryFragment()
        }

        //}
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        isFirst=true
        return false
    }
}
package com.dsm.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dsm.R
import com.dsm.databinding.FragmentJewelleryBinding
import com.dsm.ui.DetailActivity
import com.dsm.ui.MainNavigation
import com.dsm.ui.SelectPortalActivity
import com.dsm.ui.adapter.JewelleryAdapter
import com.dsm.ui.adapter.SpinnerAdapter
import com.dsm.ui.listener.onJwelleryClick
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.JewelleryModel
import com.dsm.ui.network.RetrofitBuilder
import com.dsm.ui.util.Status
import com.dsm.ui.viewmodel.JewelleryCategoryViewModel
import com.dsm.ui.viewmodel.JewellerySubCategoryViewModel
import com.dsm.ui.viewmodel.ShapeViewModel
import com.dsm.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class JewellerySubCategoryFragment : BaseFragment(),CoroutineScope ,View.OnTouchListener {


    lateinit var binding:FragmentJewelleryBinding
    var jewelleryList: MutableList<JewelleryCategoryModel> =  ArrayList()
    lateinit var jewellerySubCategoryViewModel: JewellerySubCategoryViewModel

    lateinit var jewelleryAdapter: JewelleryAdapter

    lateinit var spinnerAdapter: SpinnerAdapter

    private lateinit var job: Job
    lateinit var categoryModel : JewelleryCategoryModel
    private var isFirst = false

    // context for io thread
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    companion object {
        const val ARG_NAME = "categoryID"


        fun newInstance(name: JewelleryCategoryModel): JewellerySubCategoryFragment {
            val fragment = JewellerySubCategoryFragment()

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
        binding.tvCategory.text = categoryModel.attribute_name
    }
    private fun loadData(categoryID:String){
        jewellerySubCategoryViewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(
                JewellerySubCategoryViewModel::class.java)
        jewellerySubCategoryViewModel.getAllJewellerySubCategory(categoryID).observe(viewLifecycleOwner, Observer {
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
                                binding.rvJewellery.visibility= View.VISIBLE
                                jewelleryList.clear()
                                jewelleryList.addAll(resource.data.list.list)
                                jewelleryAdapter.notifyDataSetChanged()

                                binding.tvItemCount.text  = jewelleryList.size.toString() + " product(s) found."

                            }else{
                                binding.rvJewellery.visibility = View.GONE
                            }
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
        launch {
            withContext(Dispatchers.Main){
                binding.rvJewellery.layoutManager = GridLayoutManager(
                    activity,
                    2
                )
                jewelleryAdapter = JewelleryAdapter(activity?.baseContext!!, jewelleryList)

                binding.rvJewellery.adapter = jewelleryAdapter


                spinnerAdapter = SpinnerAdapter(activity?.baseContext!!,(activity as MainNavigation).categortList)
                binding.spCategory.adapter = spinnerAdapter
                (activity as MainNavigation).setSelection(binding.spCategory,categoryModel.attribute_id)




                val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(activity?.baseContext!!,
                    R.array.price, R.layout.price_row)

                binding.spSorting.adapter = adapter

                jewelleryAdapter.onJwelleryClick(object : onJwelleryClick {
                    override fun onClick(obj: JewelleryCategoryModel) {
                        isFirst=false
                        (activity as MainNavigation).jewelleryListFragment(obj)
//                        binding.tvCategory.text = obj.attribute_name
//                        binding.tvItemCount.text  = jewelleryList.size.toString() + " product(s) found."
//                        binding.llHeader.visibility = View.VISIBLE
//                        binding.tvCategory.visibility = View.VISIBLE
//                        binding.line.visibility = View.VISIBLE
//                        binding.tvItemCount.visibility = View.VISIBLE
//

                    }
                })
                binding.llHeader.visibility = View.VISIBLE
                binding.tvSortBy.visibility = View.INVISIBLE
                binding.spSorting.visibility = View.INVISIBLE
                binding.tvCategory.visibility = View.VISIBLE
                binding.line.visibility = View.VISIBLE
                binding.tvItemCount.visibility = View.VISIBLE

                binding.spCategory.setOnTouchListener(this@JewellerySubCategoryFragment)
                binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(isFirst){
                            var cmodel: JewelleryCategoryModel = parent!!.getItemAtPosition(position) as JewelleryCategoryModel
                            loadData(cmodel.attribute_id.toString())
                            binding.tvCategory.text = cmodel.attribute_name
                            (activity as MainNavigation).setLastSelection(position)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }

            }
        }
//        binding.spCategory.adapter.apply {
//            (activity as MainNavigation).setSelection(binding.spCategory,categoryModel.attribute_id)
//        }

    }
    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        isFirst=true
        return false
    }
}
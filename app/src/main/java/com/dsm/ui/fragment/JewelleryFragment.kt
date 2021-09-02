package com.dsm.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.dsm.ui.util.AppPreferences
import com.dsm.ui.util.Status
import com.dsm.ui.viewmodel.JewelleryCategoryViewModel
import com.dsm.ui.viewmodel.ShapeViewModel
import com.dsm.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class JewelleryFragment : BaseFragment(), CoroutineScope {


    lateinit var binding: FragmentJewelleryBinding
    var jewelleryList: MutableList<JewelleryCategoryModel> = ArrayList()
    lateinit var jewelleryCategoryViewModel: JewelleryCategoryViewModel

    lateinit var jewelleryAdapter: JewelleryAdapter


    private lateinit var job: Job

    lateinit var appPreferences: AppPreferences
    // context for io thread
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    companion object {
        const val ARG_NAME = "categoryID"


        fun newInstance(): JewelleryFragment {
            val fragment = JewelleryFragment()

            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_jewellery, container, false
        )

        var view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        appPreferences= AppPreferences(this.activity)
        loadCategory()

    }

    private fun loadData() {
        jewelleryCategoryViewModel =
            ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService)).get(
                JewelleryCategoryViewModel::class.java
            )
        jewelleryCategoryViewModel.getAllJewelleryCategory(appPreferences.getString("EMAIL")).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(activity)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        if (resource.data!!.ResponseStatus == 200) {
                            if (resource.data.list != null && resource.data.list.list.isNotEmpty()) {
                                binding.rvJewellery.visibility = View.VISIBLE
                                binding.tvMessage.visibility = View.GONE

                                jewelleryList.clear()
                                jewelleryList.addAll(resource.data.list.list)
                                (activity as MainNavigation).setCategoryList(jewelleryList)
                                jewelleryAdapter.notifyDataSetChanged()

                            }else{
                                binding.rvJewellery.visibility = View.GONE
                            }
                        }
                        else{
                            binding.rvJewellery.visibility =View.GONE
                            binding.tvMessage.visibility = View.VISIBLE
                            binding.tvMessage.text = resource.data.ResponseMessage
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showToast(resource.message, activity)
                    }
                }
            }
        })
    }

    private fun loadCategory() {
        binding.rvJewellery.setHasFixedSize(true)
        launch {
            withContext(Dispatchers.Main) {
                binding.rvJewellery.layoutManager = GridLayoutManager(
                    activity,
                    2
                )

                jewelleryAdapter = JewelleryAdapter(activity?.baseContext!!, jewelleryList)
                binding.rvJewellery.adapter = jewelleryAdapter




                val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
                    activity?.baseContext!!,
                    R.array.price, R.layout.price_row
                )

                binding.spSorting.adapter = adapter

                jewelleryAdapter.onJwelleryClick(object : onJwelleryClick {
                    override fun onClick(obj: JewelleryCategoryModel) {
                        (activity as MainNavigation).subCategoryFragment(obj)
//                        binding.tvCategory.text = obj.attribute_name
//                        binding.tvItemCount.text  = jewelleryList.size.toString() + " product(s) found."
//                        binding.llHeader.visibility = View.VISIBLE
//                        binding.tvCategory.visibility = View.VISIBLE
//                        binding.line.visibility = View.VISIBLE
//                        binding.tvItemCount.visibility = View.VISIBLE
//
//                        startActivity(
//
//                            Intent(activity, DetailActivity::class.java)
//                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//                        )
                    }
                })

            }
        }
        loadData()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
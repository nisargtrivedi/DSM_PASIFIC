package com.dsm.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dsm.R
import com.dsm.databinding.FragmentHomeBinding
import com.dsm.databinding.FragmentSearchBinding
import com.dsm.ui.MainNavigation
import com.dsm.ui.adapter.LabAdapter
import com.dsm.ui.model.labModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchFragment : Fragment() , CoroutineScope,View.OnTouchListener {

    lateinit var binding:FragmentSearchBinding
    private lateinit var job: Job
    lateinit var adapter: LabAdapter

    private var isFirst = false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    companion object {
        const val ARG_NAME = "categoryID"


        fun newInstance(): SearchFragment {
            val fragment = SearchFragment()

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
            R.layout.fragment_search, container, false
        )

        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        launch {
            withContext(Dispatchers.Main) {

                (context as MainNavigation).searchList.clear()
                (context as MainNavigation).ClarityList.clear()
                (context as MainNavigation).diaList.clear()
                (context as MainNavigation).cutList.clear()
                (context as MainNavigation).polList.clear()
                (context as MainNavigation).symList.clear()
                (context as MainNavigation).searchEditext = ""
                (context as MainNavigation).lab = ""

                binding.imgClose.setOnClickListener {
                    (context as MainNavigation).searchList.clear()
                    (context as MainNavigation).ClarityList.clear()
                    (context as MainNavigation).diaList.clear()
                    (context as MainNavigation).cutList.clear()
                    (context as MainNavigation).polList.clear()
                    (context as MainNavigation).symList.clear()

                    (context as MainNavigation).searchEditext = ""
                    (context as MainNavigation).selectItem(10)
                }

                (context as MainNavigation).loadSearchItem(binding.rvSearchItems);
                (context as MainNavigation).loadColor(binding.rvSearchColor);
                (context as MainNavigation).loadCut(binding.rvCut);
                (context as MainNavigation).loadPol(binding.rvPol);
                (context as MainNavigation).loadSym(binding.rvSym);
                (context as MainNavigation).loadClarity(binding.rvClarity);


                adapter= LabAdapter(activity,(context as MainNavigation).labsList)
                binding.spLabs.adapter = adapter
                binding.spLabs.isSelected = false;  // otherwise listener will be called on initialization
                binding.spLabs.setSelection(0,true);
                binding.spLabs.setOnTouchListener(this@SearchFragment)


                binding.btnApply.setOnClickListener {
                    //     var search = if((context as MainNavigation).searchList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).searchList) +"]"
                    (context as MainNavigation).selectItem(10)
                    if(TextUtils.isEmpty(binding.edtSearch.text)){
                        (context as MainNavigation).searchEditext = ""
                    }else{
                        (context as MainNavigation).searchEditext = binding.edtSearch.text.toString()
                    }
                }


                binding.spLabs.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                        if(isFirst){
                            isFirst=false
                            var labModel = p0!!.getItemAtPosition(p2) as labModel
                            (context as MainNavigation).lab = labModel.lab_id.toString()
                        }

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                }
            }

        }

    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        isFirst=true
        return false
    }

}
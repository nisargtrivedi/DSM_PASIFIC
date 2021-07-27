package com.dsm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dsm.R
import com.dsm.databinding.FragmentHomeBinding
import com.dsm.databinding.FragmentSearchBinding
import com.dsm.ui.MainNavigation
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchFragment : Fragment() , CoroutineScope {

    lateinit var binding:FragmentSearchBinding
    private lateinit var job: Job
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

                binding.imgClose.setOnClickListener {
                    (context as MainNavigation).searchList.clear()
                    (context as MainNavigation).selectItem(10)
                }

                (context as MainNavigation).loadSearchItem(binding.rvSearchItems);
                (context as MainNavigation).loadColor(binding.rvSearchColor);
                (context as MainNavigation).loadCut(binding.rvCut);
                (context as MainNavigation).loadCut(binding.rvPol);
                (context as MainNavigation).loadCut(binding.rvSym);
                (context as MainNavigation).loadClarity(binding.rvClarity);

                binding.btnApply.setOnClickListener {
                    //     var search = if((context as MainNavigation).searchList.isNullOrEmpty()) null else "[" + android.text.TextUtils.join(",", (context as MainNavigation).searchList) +"]"
                    (context as MainNavigation).selectItem(10)
                }
            }

        }

    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

}
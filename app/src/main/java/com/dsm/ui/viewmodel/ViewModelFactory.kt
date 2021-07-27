package com.dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsm.ui.network.APIInterface
import com.dsm.ui.repo.*


class ViewModelFactory(private val apiInterface: APIInterface) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //Register All ViewModel in ViewModel Factory
        if (modelClass.isAssignableFrom(ShapeViewModel::class.java)) {
            return ShapeViewModel(ShapeRepo(apiInterface)) as T
        }
        else if (modelClass.isAssignableFrom(JewelleryCategoryViewModel::class.java)) {
            return JewelleryCategoryViewModel(JewelleryCategoryRepo(apiInterface)) as T
        } else if (modelClass.isAssignableFrom(DiamondViewModel::class.java)) {
            return DiamondViewModel(DiamondRepo(apiInterface)) as T
        }else if (modelClass.isAssignableFrom(JewellerySubCategoryViewModel::class.java)) {
            return JewellerySubCategoryViewModel(JewellerySubCategoryRepo(apiInterface)) as T
        }else if (modelClass.isAssignableFrom(JewelleryListViewModel::class.java)) {
            return JewelleryListViewModel(JewelleryListRepo(apiInterface)) as T
        }else if (modelClass.isAssignableFrom(JewelleryDetailViewModel::class.java)) {
            return JewelleryDetailViewModel(JewelleryDetailRepo(apiInterface)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

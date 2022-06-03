package com.example.infystore.viewmodel

import android.app.Application
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.*
import com.example.infystore.R
import com.example.infystore.model.Product
import com.example.infystore.repository.ProductRepository
import com.example.infystore.utils.CommonUtils
import com.example.infystore.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The HomeViewModel class, to setup business logic
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: ProductRepository
) : AndroidViewModel(application) {
    /**
     * The _productRes, A MutableLiveData data instance
     */
    private val _productRes = MutableLiveData<List<Product>>()

    /**
     * The productList, A LiveData instance
     */
    val productList: LiveData<List<Product>> get() = _productRes

    /**
     * The orderList, An ArrayList Instance
     */
    var orderList:ArrayList<Product>? = ArrayList<Product>()

    init {
        doOnInit(application)
    }

    /**
     * The doOnInit method, to call API on class load
     */
    private fun doOnInit(application: Application) {
        if (CommonUtils.isOnline(application.applicationContext)) getAllProducts() else Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.internet_error), Toast.LENGTH_SHORT
        ).show()

        val alList = CommonUtils.getArrayListFromPref(Constants.ORDER_LIST)
        if (alList != null) {
            orderList = alList as ArrayList<Product>
        }
        productList.value
    }

    /**
     * The getAllProducts method, to call product API
     */
    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProductList().let { response ->
                if (response.isSuccessful) {
                    _productRes.postValue(response.body())
                }
            }
        }
    }

    /**
     * The onItemClick method, to open dialog on item click
     */
    fun onItemClick(view: View, product: Product) {
        showPurchaseDialog(view, product)
    }

    /**
     * The showPurchaseDialog method, to show purchase dialog on item click
     */
    private fun showPurchaseDialog(view: View, product: Product) {
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            (view as AppCompatTextView).alpha = 0.5F
            (view as AppCompatTextView).isEnabled = false
            dialog.cancel()
            product.isPurchased = true
            (view as AppCompatTextView).text = "Purchased"
            orderList?.let { orderList!!.add(product) }
            Toast.makeText(
                view.context,
                view.context.getString(R.string.purchase_success), Toast.LENGTH_SHORT
            ).show()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            product.isPurchased = false
            dialog.cancel()
        }
        CommonUtils.showAlertDialog(
            view,
            view.context.getString(R.string.purchase_message),
            positiveButtonClick,
            negativeButtonClick
        )
    }
}
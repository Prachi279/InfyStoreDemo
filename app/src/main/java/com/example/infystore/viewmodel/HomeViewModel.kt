package com.example.infystore.viewmodel

import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.*
import com.example.infystore.R
import com.example.infystore.model.Product
import com.example.infystore.repository.ProductDBRepository
import com.example.infystore.repository.ProductRepository
import com.example.infystore.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.log
import javax.inject.Inject

/**
 * The HomeViewModel class, to setup business logic
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: ProductRepository,
    private val prefimpl: PrefImpl,
    private val productDBRepository: ProductDBRepository
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
    var orderList: ArrayList<Product>? = ArrayList<Product>()


    var prodList: ArrayList<Product>? = ArrayList<Product>()

    init {
        doOnInit(application)
        Log.d("CallSequence", "Init block")
    }

    /**
     * The doOnInit method, to call API on class load
     */
    private fun doOnInit(application: Application) {
        if (CommonUtils.isOnline(application.applicationContext)) getAllProducts() else Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.internet_error), Toast.LENGTH_SHORT
        ).show()

        val alList = prefimpl.getArrayListFromPref(Constants.ORDER_LIST)
        if (alList != null) {
            orderList = alList as ArrayList<Product>
        }
    }

    /**
     * The getAllProducts method, to call product API
     */
    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProductList().let { response ->
                if (response.isSuccessful) {
                    _productRes.postValue(response.body())
                    testHugeRecords(response.body())

                }
            }
        }
        Log.d("CurrentThread", Thread.currentThread().toString())
    }

    /**
     * The testHugeRecords method, this method is for to test huge records
     */
    private suspend fun testHugeRecords(alList: List<Product>?) {
        for (i in 1..100000) {
            if(i in 1..20000) {
                prodList?.add(Product(i, "10", "350", "abc", "this is test", false))
            }
            if(i in 20001..40000) {
                prodList?.add(Product(i, "15", "350", "abc", "this is test", false))
            }
            if(i in 40001..60000) {
                prodList?.add(Product(i, "13", "350", "abc", "this is test", false))
            }
            if(i in 60001..80000) {
                prodList?.add(Product(i, "11", "350", "abc", "this is test", false))
            }
            if(i in 80001..100000) {
                prodList?.add(Product(i, "9", "350", "abc", "this is test", false))
            }
        }
        //val list = productDBRepository.insertAllData(response.body() as List<Product>)
        val list = productDBRepository.insertAllData(prodList!!.toList())
        Log.d(
            "inserted IDs",
            "DatabaseIds = " + list.size.toString() + " API-IDs = " + alList?.size
        )
        Log.d("CurrentThread", Thread.currentThread().toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (CommonUtils.isOnline(getApplication<Application>().applicationContext)) {
                startMyForegroundService()
            }
        }

    }

    /**
     * The startMyForegroundService method, to start foreground service
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyForegroundService() {
        if (!CommonUtils.foregroundServiceRunning(getApplication<Application>().applicationContext)) {
            val serviceIntent = Intent(
                getApplication<Application>().applicationContext,
                MyForegrounndService2::class.java
            )
            getApplication<Application>().applicationContext.startForegroundService(serviceIntent)
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
            dialog.cancel()

            product.isPurchased = true
            (view as AppCompatTextView).alpha = 0.5F
            (view as AppCompatTextView).isEnabled = false
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
package com.example.infystore.repository

import com.example.infystore.db.ProductDao
import com.example.infystore.model.Product
import com.example.infystore.projectrepo.APIInterface
import javax.inject.Inject

class ProductDBRepository @Inject constructor(private val productDao: ProductDao) {
    fun getOfflineProductList() = productDao.getProducts()
    fun  getTotalCount()=productDao.getTotalCount()
    fun getProducts()=productDao.getProducts()
    suspend fun  getTotalCountOfSheet(name:String)=productDao.getTotalCountOfSheet(name)
    suspend fun insertAllData(list:List<Product>)=productDao.insertProduct(list)
    suspend fun getLimitedData()=productDao.getLimitedData()
    suspend fun deleteProduct(product: Product)=productDao.deleteProduct(product)
    suspend fun deleteProducts(idList: List<Int>)=productDao.deleteProducts(idList)
    suspend fun deleteAllRecords()=productDao.deleteAllRecords()
    suspend fun getNextRecords(id:Int)=productDao.getNextProductList(id)
    suspend fun getNextProductListByName(id:Int,name:String)=productDao.getNextProductListByName(id,name)
    suspend fun deleteRecorsInRange(firstId:Int,secondId:Int)=productDao.deleteRecordInRange(firstId, secondId)
}

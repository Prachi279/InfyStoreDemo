package com.example.infystore.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.infystore.model.Product
import com.example.infystore.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface ProductDao {
    /**
     * The getProducts method,to get all the products
     */
    @Query("SELECT * FROM Product")
    fun getProducts(): List<Product>

    /**
     * The getTotalCount method, to get the total count of product table
     */
    @Query("SELECT COUNT(*) FROM Product")
    fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM Product where name=:name")
    fun getTotalCountOfSheet(name:String): Int

    /**
     * The insertProduct method, to insert whole arraylist into the database
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertProduct(productList: List<Product>): List<Long>

    /**
     * The getLimitedData method, to get number of records based on limit
     */
    @Query("SELECT * FROM Product LIMIT " + Constants.LIMIT+";")
    suspend fun getLimitedData(): List<Product>
    //@Query("SELECT * FROM Product  ORDER BY name ASC LIMIT " + Constants.LIMIT+" ;")
    //suspend fun getLimitedData(): List<Product>

    /**
     * The deleteProduct method, to delete specific method
     */
    @Delete
    suspend fun deleteProduct(product: Product)

    /**
     * The deleteProducts method, to delete list of products
     */
    @Query("delete from Product where id in (:idList)")
    suspend fun deleteProducts(idList: List<Int>)

    /**
     * The deleteAllRecords method, to delete all the records of table
     */
    @Query("DELETE FROM Product")
    suspend fun deleteAllRecords()

    /**
     * The deleteRecordInRange method, to delete  records in a range
     */
    @Query("delete from Product where id BETWEEN (:firstId) AND (:secondId)")
    suspend fun deleteRecordInRange(firstId:Int,secondId:Int):Int

    /**
     * The getNextProductList method, to get next list of products specific after particular Id
     */
    @Query("SELECT * FROM Product WHERE id > :id LIMIT "+ Constants.LIMIT+";")
    suspend fun getNextProductList(id:Int) : List<Product>

    @Query("SELECT * FROM Product WHERE id > :id AND name=:name LIMIT "+ Constants.LIMIT+";")
    suspend fun getNextProductListByName(id:Int,name:String) : List<Product>
    /**
     * The getNextProductList method, to get next list of products specific after particular Id
     */
    //@Query("SELECT * FROM Product WHERE id > :id ORDER BY name ASC LIMIT "+ Constants.LIMIT+";")
    //suspend fun getNextProductList(id:Int) : List<Product>
}
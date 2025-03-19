package com.ksv.costmemories.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseCsv
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import com.ksv.costmemories.entity.mapper.toCsv
import com.ksv.costmemories.entity.mapper.toTuple
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder
import com.opencsv.bean.StatefulBeanToCsvBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.OutputStreamWriter

class Repository(private val context: Context, private val purchasesDao: PurchasesDao) {
    //private val purchasesDao = Dependencies.getPurchasesDao()

    suspend fun exportPurchasesToCsv(uri: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            val purchasesTuple = purchasesDao.getAllAsTuple()
            val purchasesCsv = purchasesTuple.map { it.toCsv() }

            var outputStream: OutputStream? = null
            var outputStreamWriter: OutputStreamWriter? = null

            try {
                outputStream = context.contentResolver.openOutputStream(uri, "w")
                outputStream?.let {
                    outputStreamWriter = outputStream.writer()
                    outputStreamWriter?.let {
                        val beanToCsv =
                            StatefulBeanToCsvBuilder<PurchaseCsv>(outputStreamWriter).build()
                        beanToCsv.write(purchasesCsv)
                        return@withContext true
                    }
                }
                return@withContext false
            } catch (ioException: IOException) {
                Log.d(
                    "ksvlog",
                    "Repository.exportPurchasesTuplesToCsv IOException: ${ioException.message}"
                )
                return@withContext false
            } catch (exception: Exception) {
                Log.d(
                    "ksvlog",
                    "Repository.exportPurchasesTuplesToCsv Exception: ${exception.message}"
                )
                return@withContext false
            } finally {
                outputStreamWriter?.close()
                outputStream?.close()
            }
        }
    }




    suspend fun getPurchasesListFromFile(uri: Uri): List<PurchaseTuple> {
        return withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var bufferedReader: BufferedReader? = null
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                inputStream.let {
                    bufferedReader = inputStream!!.bufferedReader()
                    val strategy = HeaderColumnNameMappingStrategyBuilder<PurchaseCsv>().build()
                    strategy.type = PurchaseCsv::class.java
                    val csvToBean = CsvToBeanBuilder<PurchaseCsv>(bufferedReader)
                        .withMappingStrategy(strategy)
                        .build()
                    val purchasesCsvList = csvToBean.parse()
                    val purchasesTupleList = purchasesCsvList.map { it.toTuple() }
                    return@withContext purchasesTupleList
                }
            } catch (exception: Exception) {
                Log.d(
                    "ksvlog",
                    "Repository.importPurchasesTuplesFromCsv exception: ${exception.message}"
                )
                return@withContext emptyList()
            } finally {
                inputStream?.close()
                bufferedReader?.close()
            }
        }
    }

    suspend fun purchasesToDb(purchases: List<PurchaseTuple>) {
        purchases.onEach { purchase ->
            val shopId = getShopId(shopName = purchase.shop)
            val titleId = getTitleId(titleText = purchase.title)
            val productId = getProductId(productName = purchase.product)
            val purchaseDB = Purchase(
                id = 0,
                milliseconds = purchase.milliseconds,
                productId = productId,
                titleId = titleId,
                shopId = shopId,
                cost = purchase.cost,
                comment = purchase.comment
            )
            purchasesDao.purchaseInsert(purchaseDB)
        }
    }



    private suspend fun getShopId(shopName: String): Long {
        val shop = purchasesDao.getShopByName(shopName)
        return shop?.id ?: purchasesDao.shopInsert(Shop(shop = shopName))
    }

    private suspend fun getTitleId(titleText: String): Long {
        val title = purchasesDao.getTitleByName(titleText)
        return title?.id ?: purchasesDao.titleInsert(Title(title = titleText))
    }

    private suspend fun getProductId(productName: String): Long {
        val product = purchasesDao.getProductByName(productName)
        return product?.id ?: purchasesDao.productInsert(Group(product = productName))
    }
}
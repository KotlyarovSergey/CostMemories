package com.ksv.costmemories.data

import android.content.Context
import android.util.Log
import com.ksv.costmemories.Dependencies
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Repository {
    private val purchasesDao = Dependencies.getPurchasesDao()

    suspend fun exportPurchasesTuplesToCsv(context: Context, fileName: String) {
        withContext(Dispatchers.IO) {
            //val purchasesDao = Dependencies.getPurchasesDao()
            val purchasesTuple = purchasesDao.getAllAsTuple()
            val purchasesCsv = purchasesTuple.map { it.toCsv() }

            try {
                val fullFileName = File(context.getExternalFilesDir(null), fileName)
                Log.d("ksvlog", "export to: $fullFileName")
                val writer = FileWriter(fullFileName)

                val beanToCsv = StatefulBeanToCsvBuilder<PurchaseCsv>(writer).build()
                beanToCsv.write(purchasesCsv)
                writer.close()
                if (fullFileName.exists())
                    Log.d("ksvlog", "export successfully")
                else
                    Log.d("ksvlog", "Export Failure!! file not exist")
            } catch (exception: Exception) {
                Log.d("ksvlog", exception.stackTrace.toString())
            }
        }
    }

    suspend fun importPurchasesTuplesFromCsv(
        context: Context,
        fileName: String
    ) {

        val purchases = getPurchasesListFromFile(context, fileName)
        Log.d("ksvlog", "Repository. purchases imported: ${purchases.size}")
        if(purchases.isNotEmpty()) {
            purchasesToDb(purchases)
        }

//        delay(3000)
        //return listOf(PurchaseTuple(1, "25.01.25", "Сыр", "Российский", "Магнит", 540, "вакуум"))
    }

    private suspend fun purchasesToDb(purchases: List<PurchaseTuple>){
        purchases.onEach { purchase ->
            val shopId = getShopId(shopName = purchase.shop)
            val titleId = getTitleId(titleText = purchase.title)
            val productId = getProductId(productName = purchase.product)
            val purchaseDB = Purchase(
                id = 0,
                date = purchase.date,
                productId = productId,
                titleId = titleId,
                shopId = shopId,
                cost = purchase.cost,
                comment = purchase.comment
            )
            purchasesDao.purchaseInsert(purchaseDB)
            Log.d("ksvlog", "purchase insert: ${purchase.product} ${purchase.title}")
        }

    }

    private suspend fun getShopId(shopName: String): Long{
        val shop = purchasesDao.getShopByName(shopName)
        return shop?.id ?: purchasesDao.shopInsert(Shop(shop = shopName))
    }

    private suspend fun getTitleId(titleText: String): Long{
        val title = purchasesDao.getTitleByName(titleText)
        return title?.id ?: purchasesDao.titleInsert(Title(title = titleText))
    }

    private suspend fun getProductId(productName: String): Long{
        val product = purchasesDao.getProductByName(productName)
        return product?.id ?: purchasesDao.productInsert(Group(product = productName))
    }

    private suspend fun getPurchasesListFromFile(
        context: Context,
        fileName: String
    ): List<PurchaseTuple>{
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.getExternalFilesDir(null), fileName)
                val reader = FileReader(file)

                val purchasesTupleList = tupleFromCsv(reader)
                reader.close()

                delay(2000)
                //            return@withContext listOf(PurchaseTuple(2, "05.02.13", "Пиво", "Чешское", "Пиволюлок", 98, "бфрожми"))
                return@withContext purchasesTupleList
            } catch (exception: Exception){
                Log.d("ksvlog", "Repository.importPurchasesTuplesFromCsv exception: ${exception.message}")
                return@withContext emptyList()
            }
        }
    }

    private fun tupleFromCsv(reader: FileReader) : List<PurchaseTuple>{
        val strategy = HeaderColumnNameMappingStrategyBuilder<PurchaseCsv>().build()
        strategy.type = PurchaseCsv::class.java

        val csvToBean = CsvToBeanBuilder<PurchaseCsv>(reader)
            .withMappingStrategy(strategy)
            .build()

        val purchasesCsvList = csvToBean.parse()

        val purchasesTupleList = purchasesCsvList.map { it.toTuple() }
        return purchasesTupleList
    }
}
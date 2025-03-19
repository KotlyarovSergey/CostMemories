package com.ksv.costmemories.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "purchases",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = Shop::class,
            parentColumns = ["id"],
            childColumns = ["shop_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Title::class,
            parentColumns = ["id"],
            childColumns = ["title_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]

)
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val milliseconds: Long,
    @ColumnInfo(name = "product_id") val productId: Long,
    @ColumnInfo(name = "title_id") val titleId: Long,
    @ColumnInfo(name = "shop_id") val shopId: Long,
    val cost: Int,
    val comment: String?
)
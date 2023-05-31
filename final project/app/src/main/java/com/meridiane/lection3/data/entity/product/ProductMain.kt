package com.meridiane.lection3.data.entity.product

import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail

data class ProductMain(
    val badge: List<Badge> ?= null,
    val department: String ?= null,
    val description: String ?= null,
    val details: List<String> ?= null,
    val id: String ?= null,
    val images: List<String> ?= null,
    val preview: String ?= null,
    val price: Int ?= null,
    val sizes: List<Size> ?= null,
    val title: String ?= null
) {
    fun toProduct(): Product = Product(
        id = id,
        title = title,
        category = department,
        preview = preview
    )

    fun toProductDetail(): ProductDetail =
        ProductDetail(
            badge = badge?.map{ badge ->
                badge.toBadge()
            },
            department = department,
            description = description,
            details = details,
            id = id,
            images = images,
            preview = preview,
            price = price,
            sizes = sizes?.map{ sizes ->
                sizes.toSize()
            },
            title = title
        )
}
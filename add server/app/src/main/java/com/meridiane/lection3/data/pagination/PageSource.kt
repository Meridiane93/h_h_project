package com.meridiane.lection3.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.domain.entity.product.Product

// абстрактный загрузчик возвращает список продуктов
typealias ProductPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<Product>

class PageSource(
    private val pageSize: Int,
    private val loader: ProductPageLoader
    // в PagingSource необходимо указать источник данных (бд, сеть и тд )
    // в PagingSource первый параметр, какие аргументы пагинации используем, второй с какими данными работаем
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        val pageIndex = params.key ?: 1

        return try {

            val product = loader.invoke(pageIndex,params.loadSize)
            Log.d("MyTag", "list $product")

            return LoadResult.Page(
                data = product,
                prevKey = if (pageIndex == 1 ) null else pageIndex - 1,
                nextKey = if(product.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Throwable) {
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}

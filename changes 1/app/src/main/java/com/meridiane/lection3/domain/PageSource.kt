package com.meridiane.lection3.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.data.Product

typealias ProductLoader =
        suspend (pageIndex: Int, pageSize: Int) -> List<Product>

class PageSource(
    private val repository: ProductLoader,
    private val pageSize: Int
) : PagingSource<Int, Product>() {


    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        val pageIndex = params.key ?: 0

        return try {

            val product = repository.invoke(pageIndex, params.loadSize)

            return LoadResult.Page(
                data = product, // загруженные данные
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (product.size == params.loadSize)
                    pageIndex + (params.loadSize / pageSize) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }

    }
}
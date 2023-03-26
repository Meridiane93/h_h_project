package com.meridiane.lection3.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.domain.repository.MockRepository

class PageSource(
    private val repository: MockRepository
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        return try {
            val pageNumber = params.key ?: 0
            val product = repository.getProducts2()
            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (product.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(data = product, prevKey = prevKey, nextKey = nextKey)
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

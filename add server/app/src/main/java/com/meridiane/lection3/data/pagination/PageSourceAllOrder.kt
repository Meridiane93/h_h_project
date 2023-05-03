package com.meridiane.lection3.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.data.repository.MockRepository
import com.meridiane.lection3.domain.entity.Order

class PageSourceAllOrder(
    private val repository: MockRepository,
) : PagingSource<Int, Order>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {

        return try {
//            val pageNumber = params.key ?: 0
            val product = repository.getAllOrder()
//            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
//            val nextKey = if (product.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(data = product, prevKey = null, nextKey = null)
        } catch (e: Throwable) {
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}

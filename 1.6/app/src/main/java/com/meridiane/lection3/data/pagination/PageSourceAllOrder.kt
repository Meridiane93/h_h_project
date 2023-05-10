package com.meridiane.lection3.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.domain.entity.Order

typealias OrdersPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<Order>
class PageSourceAllOrder(
    private val pageSize: Int,
    private val loader:OrdersPageLoader
) : PagingSource<Int, Order>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {

        val pageIndex = params.key ?: 1

        return try {

            val orders = loader.invoke(pageIndex,params.loadSize)

            return LoadResult.Page(
                data = orders,
                prevKey = if (pageIndex == 1 ) null else pageIndex - 1,
                nextKey = if(orders.size == params.loadSize) pageIndex + (params.loadSize / pageSize) else null
            )
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

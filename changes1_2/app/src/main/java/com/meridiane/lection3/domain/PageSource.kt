package com.meridiane.lection3.domain

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.domain.repository.MockRepository

//typealias ProductLoader =
 //       suspend (pageIndex: Int, pageSize: Int) -> List<Product>

class PageSource(
    private val repository: MockRepository
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        val pageNumber = params.key ?: 0

        val result: Result<List<Product>> = repository.getProducts2()

        val product: LoadResult<Int, Product> = result.fold(
            onSuccess = { data ->
                LoadResult.Page(
                    data = data,
                    prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                    nextKey = if(data.isNotEmpty()) pageNumber + 1 else null
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
        return product
    }

    override fun getRefreshKey(state: PagingState<Int, Product>):  Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}

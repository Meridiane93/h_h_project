package com.meridiane.lection3.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.domain.repository.MockRepository

typealias ProductLoader =
        suspend (pageIndex: Int, pageSize: Int) -> List<Product>

class PageSource(
    private val repository: ProductLoader,
    private val pageSize: Int
) : PagingSource<Int, Product>() {

    // когда данные обновились или пользователь запросил перезагрузить данные
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    // загрузка данных и вычислить начальный эл загрузки и следующий эл загрузки
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {

        val pageIndex = params.key ?: 0 // индекс с которого начинаем загрузку данных

        return try {

            val product = repository.invoke(pageIndex, params.loadSize) // обращение к загрузке и загрузка продуктов с pageIndex до params.loadSize

            return LoadResult.Page(
                data = product, // загруженные данные
                prevKey = if (pageIndex == 0) null else pageIndex - 1, // аргумент пагинации предыдущим
                nextKey = if (product.size == params.loadSize)  // аргумент пагинации следующим
                    pageIndex + (params.loadSize / pageSize) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }


    }
}
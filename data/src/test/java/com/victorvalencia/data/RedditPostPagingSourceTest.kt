package com.victorvalencia.data

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.model.domain.TopPostPaging
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class RedditPostPagingSourceTest {

    private val postsResults: List<RedditPost> = mock()

    @Test
    fun givenPagingSourceLoad_whenIsTheFirstFetch_thenLoadTopPosts() = runTest {
        val redditRepository: RedditPostsRepository = mock {
            onBlocking { getNextTopPosts(null, 0, 75) } doReturn ApiResult.Success(
                TopPostPaging(
                    "nextId_1", null, null, posts = postsResults
                )
            )
        }

        val sut = RedditPostPagingSource(redditRepository)

        assertThat(
            sut.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 75,
                    placeholdersEnabled = false
                )
            )
        ).isEqualTo(
            PagingSource.LoadResult.Page(
                data = postsResults,
                prevKey = null,
                nextKey = "nextId_1"
            )
        )
    }


    @Test
    fun givenPagingSourceLoad_whenUserIsScrollingToSeeNextPosts_thenAppendNextPosts() = runTest {
        val redditRepository: RedditPostsRepository = mock {
            onBlocking { getNextTopPosts("nextId_2", null, 25) } doReturn ApiResult.Success(
                TopPostPaging(
                    "nextId_3", "nextId_1", 25, posts = postsResults
                )
            )
        }

        val sut = RedditPostPagingSource(redditRepository)

        assertThat(
            sut.load(
                PagingSource.LoadParams.Append(
                    key = "nextId_2",
                    loadSize = 25,
                    placeholdersEnabled = false
                )
            )
        ).isEqualTo(
            PagingSource.LoadResult.Page(
                data = postsResults,
                prevKey = "nextId_1",
                nextKey = "nextId_3"
            )
        )
    }


    @Test
    fun givenPagingSourceLoad_whenOnListInvalidationCalled_thenPrependPreviousPosts() = runTest {
        val redditRepository: RedditPostsRepository = mock {
            onBlocking { getPrevTopPosts("nextId_2.1", 0, 25) } doReturn ApiResult.Success(
                TopPostPaging(
                    "nextId_3.1", "nextId_1.1", 25, posts = postsResults
                )
            )
        }

        val sut = RedditPostPagingSource(redditRepository)

        assertThat(
            sut.load(
                PagingSource.LoadParams.Prepend(
                    key = "nextId_2.1",
                    loadSize = 25,
                    placeholdersEnabled = false
                )
            )
        ).isEqualTo(
            PagingSource.LoadResult.Page(
                data = postsResults,
                prevKey = "nextId_1.1",
                nextKey = "nextId_3.1"
            )
        )
    }
}
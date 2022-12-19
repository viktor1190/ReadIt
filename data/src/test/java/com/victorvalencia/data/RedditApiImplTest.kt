package com.victorvalencia.data

import com.google.common.truth.Truth.assertThat
import com.victorvalencia.data.mapper.ResponseToApiResultMapper
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.model.domain.TopPostPaging
import com.victorvalencia.data.model.response.RedditTopPostsResponse
import com.victorvalencia.data.network.NetworkHandler
import com.victorvalencia.data.network.RedditApiImpl
import com.victorvalencia.data.network.RedditService
import com.victorvalencia.data.util.FileUtil
import com.victorvalencia.data.util.asRedditTopPostsResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RedditApiImplTest {

    private val networkHandlerMock: NetworkHandler = mock {
        on { isConnected } doReturn true
    }

    @Test
    fun givenCallGetNextTopList_whenSuccessWithAllData_thenReturnsValidResponse() = runTest {
        val redditService: RedditService = mock {
            onBlocking { getTopListAfter(null, null, 3) } doReturn mock()
        }
        val responseToApiResultMapper: ResponseToApiResultMapper = mock {
            on { toResult(any<Response<RedditTopPostsResponse>>()) } doReturn
                    ApiResult.Success(FileUtil.readFileWithoutNewLineFromResources("top_limit3.json").asRedditTopPostsResponse())
        }

        val sut = RedditApiImpl(redditService, responseToApiResultMapper, networkHandlerMock)

        val result =
            assertThat(sut.getNextTopList(null, null, 3))
                .isEqualTo(
                    ApiResult.Success(
                        TopPostPaging(
                            nextId = "t3_zokrxl",
                            prevId = null,
                            lastCount = 3,
                            listOf(
                                RedditPost(
                                    "zopbdu", "She's not wrong.", "wholesomememes", "2004_Oldies",
                                    "https://b.thumbs.redditmedia.com/nHWSpENRgDQgzkgpBGBG2jSQbxhYrACpQUNp0n70fmM.jpg",
                                    "/r/wholesomememes/comments/zopbdu/shes_not_wrong/", "https://i.redd.it/n47nagtgfm6a1.jpg",
                                    false, "t3_zopbdu"
                                ),
                                RedditPost(
                                    id = "zopjh4", title = "Sheep stuck in forest for 5 years gets around 35kg (80 pounds) of wool removed",
                                    subreddit = "interestingasfuck", author = "WilliamDavidHarrison",
                                    thumbnailUrl = "https://b.thumbs.redditmedia.com/9lohJbkU0I8-v4Z1zMnGYt8KVdJxvSW60kRpZreAMhQ.jpg",
                                    permalink = "/r/interestingasfuck/comments/zopjh4/sheep_stuck_in_forest_for_5_years_gets_around/",
                                    url = "https://v.redd.it/utb6vlcxzk6a1", isVideo = false, pagingId = "t3_zopjh4"
                                ),
                                RedditPost(
                                    id = "zokrxl",
                                    title = "Please don’t overanalyze this",
                                    subreddit = "tumblr",
                                    author = "reds2032",
                                    thumbnailUrl = "https://b.thumbs.redditmedia.com/GlJVF52wdj2EF0sTPdTnZV6AIbunHavp8Ozel6leztM.jpg",
                                    permalink = "/r/tumblr/comments/zokrxl/please_dont_overanalyze_this/",
                                    url = "https://i.redd.it/frbbjq8ncl6a1.jpg",
                                    isVideo = false,
                                    pagingId = "t3_zokrxl"
                                )
                            )
                        )
                    )
                )
    }
}


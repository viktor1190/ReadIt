package com.victorvalencia.data.mapper

import com.google.common.truth.Truth.assertThat
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.asSuccess
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.model.domain.TopPostPaging
import com.victorvalencia.data.util.FileUtil
import com.victorvalencia.data.util.asRedditTopPostsResponse
import org.junit.Test

class RedditTopListResponseMapperKtTest {

    @Test
    fun mapFromResponseToDomainModel_shouldCompleteSuccessfully() {
        val redditTopListResponse = FileUtil.readFileWithoutNewLineFromResources("top_limit1.json").asRedditTopPostsResponse()

        val result: ApiResult<TopPostPaging> = redditTopListResponse.mapToTopPostPaging()

        assertThat(result)
            .isEqualTo(
                TopPostPaging(
                    "t3_zopbdu",
                    null,
                    1,
                    listOf(
                        RedditPost(
                            id = "zopbdu",
                            title = "She's not wrong.",
                            subreddit = "wholesomememes",
                            author = "2004_Oldies",
                            thumbnailUrl = "https://b.thumbs.redditmedia.com/nHWSpENRgDQgzkgpBGBG2jSQbxhYrACpQUNp0n70fmM.jpg",
                            permalink = "/r/wholesomememes/comments/zopbdu/shes_not_wrong/",
                            url = "https://i.redd.it/n47nagtgfm6a1.jpg",
                            isVideo = false,
                            pagingId = "t3_zopbdu"
                        )
                    )
                ).asSuccess()
            )
    }
}

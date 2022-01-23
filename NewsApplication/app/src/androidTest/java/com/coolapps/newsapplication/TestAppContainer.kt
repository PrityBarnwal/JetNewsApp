package com.coolapps.newsapplication

import android.content.Context
import com.coolapps.newsapplication.data.AppContainer
import com.coolapps.newsapplication.data.interests.InterestsRepository
import com.coolapps.newsapplication.data.interests.impl.FakeInterestsRepository
import com.coolapps.newsapplication.data.posts.PostsRepository
import com.coolapps.newsapplication.data.posts.impl.BlockingFakePostsRepository

class TestAppContainer (private val context: Context) : AppContainer {

    override val postsRepository: PostsRepository by lazy {
        BlockingFakePostsRepository()
    }

    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }
}
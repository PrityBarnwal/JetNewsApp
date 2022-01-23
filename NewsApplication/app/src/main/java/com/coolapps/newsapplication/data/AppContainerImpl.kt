package com.coolapps.newsapplication.data

import android.content.Context
import com.coolapps.newsapplication.data.interests.InterestsRepository
import com.coolapps.newsapplication.data.interests.impl.FakeInterestsRepository
import com.coolapps.newsapplication.data.posts.PostsRepository
import com.coolapps.newsapplication.data.posts.impl.FakePostsRepository

interface AppContainer {
    val postsRepository: PostsRepository
    val interestsRepository: InterestsRepository
}
class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val postsRepository: PostsRepository by lazy {
        FakePostsRepository()
    }

    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }
}
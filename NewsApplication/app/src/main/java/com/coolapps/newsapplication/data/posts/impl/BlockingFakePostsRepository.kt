package com.coolapps.newsapplication.data.posts.impl

import com.coolapps.newsapplication.data.posts.PostsRepository
import com.coolapps.newsapplication.model.Post
import com.coolapps.newsapplication.model.PostsFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import  com.coolapps.newsapplication.data.Result
import com.coolapps.newsapplication.utils.addOrRemove
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

@OptIn(ExperimentalCoroutinesApi::class)
class BlockingFakePostsRepository : PostsRepository {

    private val favorites = MutableStateFlow<Set<String>>(setOf())

    override suspend fun getPost(postId: String?): Result<Post> {
        return withContext(Dispatchers.IO){
            val post = posts.allPosts.find { it.id == postId }
            if (post == null){
                Result.Error(IllegalArgumentException("Unable to find"))
            }else {
                Result.Success(post)
            }
        }
    }

    override suspend fun getPostsFeed(): Result<PostsFeed> {
        return Result.Success(posts)
    }

    override fun observeFavorites(): Flow<Set<String>> = favorites

    override suspend fun toggleFavorite(postId: String) {
        val set = favorites.value.toMutableSet()
        set.addOrRemove(postId)
        favorites.value = set
    }
}
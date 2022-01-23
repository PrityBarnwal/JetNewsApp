package com.coolapps.newsapplication.data.posts

import com.coolapps.newsapplication.data.Result
import com.coolapps.newsapplication.model.Post
import com.coolapps.newsapplication.model.PostsFeed
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    suspend fun getPost(postId: String?): Result<Post>
    suspend fun getPostsFeed(): Result<PostsFeed>
    fun observeFavorites(): Flow<Set<String>>
    suspend fun toggleFavorite(postId: String)
}
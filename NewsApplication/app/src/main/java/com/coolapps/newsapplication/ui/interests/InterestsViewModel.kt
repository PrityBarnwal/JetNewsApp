package com.coolapps.newsapplication.ui.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coolapps.newsapplication.data.interests.InterestSection
import com.coolapps.newsapplication.data.interests.InterestsRepository
import com.coolapps.newsapplication.data.interests.TopicSelection
import com.coolapps.newsapplication.data.successOr
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class InterestsUiState(
        val topics : List<InterestSection> = emptyList(),
        val people : List<String> = emptyList(),
        val publication: List<String> = emptyList(),
        val loading : Boolean = false,
)
class InterestsViewModel (
    private val interestsRepository: InterestsRepository
        ) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestsUiState(loading = true))
    val uiState : StateFlow<InterestsUiState> = _uiState.asStateFlow()

    val selectedTopics =
        interestsRepository.observeTopicsSelected().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )
    val selectedPeople =
        interestsRepository.observePeopleSelected().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )
    val selectedPublication =
        interestsRepository.observePublicationSelected().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )
    init {
        refreshAll()
    }
    fun toggleTopicSelection(topic: TopicSelection) {
        viewModelScope.launch {
            interestsRepository.toggleTopicSelection(topic)
        }
    }

    fun togglePersonSelected(person: String) {
        viewModelScope.launch {
            interestsRepository.togglePersonSelected(person)
        }
    }

    fun togglePublicationSelected(publication: String) {
        viewModelScope.launch {
            interestsRepository.togglePublicationSelected(publication)
        }
    }

    private fun refreshAll(){
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val topicsDeferred = async { interestsRepository.getTopics() }
            val peopleDeferred = async { interestsRepository.getPeople() }
            val publicationDeferred =async { interestsRepository.getPublications() }

            val topics = topicsDeferred.await().successOr(emptyList())
            val people = peopleDeferred.await().successOr(emptyList())
            val publication = publicationDeferred.await().successOr(emptyList())

            _uiState.update {
                it.copy(
                    loading = false,
                    topics = topics,
                    people = people,
                    publication = publication
                )
            }

        }
    }

    companion object {
        fun provideFactory(
            interestsRepository: InterestsRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InterestsViewModel(interestsRepository) as T
            }
        }
    }
}
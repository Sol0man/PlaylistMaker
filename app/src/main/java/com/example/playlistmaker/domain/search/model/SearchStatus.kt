package com.example.playlistmaker.domain.search.model

enum class SearchStatus {
    DEFAULT,
    RESPONSE_RECEIVED,
    LIST_IS_EMPTY,
    NETWORK_ERROR,
    LOADING
}
package com.example.playlistmaker.di

import com.example.playlistmaker.data.converters.TrackDbConbertor
import com.example.playlistmaker.data.db.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.player.repositoryImpl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.network.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    factory<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named(SEARCH_HISTORY)))
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(named(SETTINGS_PREFERENCES)))
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

    factory { TrackDbConbertor() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
}
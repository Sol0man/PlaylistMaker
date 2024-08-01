package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository,
) : SharingInteractor {
    override fun shareApp() {
        repository.shareApp()
    }

    override fun openTerms() {
        repository.openTerms()
    }

    override fun openSupport() {
        repository.openSupport()
    }

}
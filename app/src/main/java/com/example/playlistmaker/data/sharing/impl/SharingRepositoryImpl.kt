package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository  {
    override fun shareApp() {
        val link = context.getString(R.string.url_share)
        val type = context.getString(R.string.share_type)
        val message = context.getString(R.string.share_app)
        val intentLink = Intent(Intent.ACTION_SEND)
        intentLink.type = type
        intentLink.putExtra(Intent.EXTRA_TEXT, link)
        val intentMessage = Intent.createChooser(intentLink, message)
        intentMessage.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intentMessage)
    }

    override fun openTerms() {
        val link = context.getString(R.string.url_agreement)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun openSupport() {
        val message = context.getString(R.string.write_to_supp_message)
        val theme = context.getString(R.string.write_to_supp_theme)
        val mailTo = context.getString(R.string.write_to_supp_mailto)
        val email = context.getString(R.string.write_to_supp_email)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(mailTo)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.putExtra(Intent.EXTRA_SUBJECT, theme)
        context.startActivity(intent)
    }
}
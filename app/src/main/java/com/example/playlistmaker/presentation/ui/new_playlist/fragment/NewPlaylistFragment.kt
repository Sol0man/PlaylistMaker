package com.example.playlistmaker.presentation.ui.new_playlist.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistCreateFragmentBinding
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.new_playlist.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : BindingFragment<NewPlaylistCreateFragmentBinding>() {

    private val viewModel by viewModel<NewPlaylistViewModel>()
    private lateinit var dialog: MaterialAlertDialogBuilder
    private var albumImageUri: Uri? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NewPlaylistCreateFragmentBinding {
        return NewPlaylistCreateFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.complete_create_playlist))
            setMessage(getString(R.string.lose_data))
            setPositiveButton(getString(R.string.complete)) { _, _ ->
                findNavController().navigateUp()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonCreatePlaylist.isEnabled = !p0.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        binding.tietPlaylistName.addTextChangedListener(simpleTextWatcher)

        binding.ivButtonBack.setOnClickListener {
            if (albumImageUri != null || !binding.tietPlaylistName.text.isNullOrEmpty() ||
                !binding.tietPlaylistDescription.text.isNullOrEmpty()) {
                dialog.show()
            } else {
                findNavController().navigateUp()
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri->
                if (uri != null) {
                    binding.ivAlbum.setImageURI(uri)
                    albumImageUri = uri
                } else {

                }
            }

        binding.ivAlbum.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreatePlaylist.setOnClickListener {
            val albumName = binding.tietPlaylistName.text.toString()
            val albumDescription = binding.tietPlaylistDescription.text.toString()
            var albumImageName: String? = null
            if (albumImageUri != null) {
                albumImageName = albumName + Calendar.getInstance().time.toString() + ".jpg"
                saveAlbumImageToPrivateStorage(albumImageName)
            }
            if (albumImageName != null) {
                viewModel.createNewPlaylist(albumName, albumDescription, albumImageName)
            }

            Toast.makeText(
                requireContext(),
                String.format("Плейлист $albumName создан"),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }

    }

    private fun saveAlbumImageToPrivateStorage(albumImageName: String) {
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                DIRECTORY
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, albumImageName)

        val inputStream = requireActivity().contentResolver.openInputStream(albumImageUri!!)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)
        inputStream!!.close()
        outputStream.close()
    }

    companion object {
        private const val DIRECTORY = "album_images"
        private const val QUALITY = 100
    }

}
package ru.yellowshark.surfandroidschool.ui.main.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.db.entity.MemeEntity
import ru.yellowshark.surfandroidschool.databinding.FragmentCreateMemeBinding
import ru.yellowshark.surfandroidschool.utils.BASE_MEME_PIC

class CreateMemeFragment : Fragment(R.layout.fragment_create_meme) {

    private val viewModel: CreateMemeViewModel by viewModel()

    private var _binding: FragmentCreateMemeBinding? = null
    private val binding get() = _binding!!

    private var _itemCreate: MenuItem? = null
    private val itemCreate get() = _itemCreate!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_meme, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListeners()
    }

    private fun initView() {
        _itemCreate = binding.toolbar.menu.findItem(R.id.action_create)
        _itemCreate?.isEnabled = false
        //by default
        binding.imageSrc = BASE_MEME_PIC
    }

    private fun initListeners() {
        with(binding) {

            itemCreate.setOnMenuItemClickListener {
                viewModel.addMeme(
                    MemeEntity(
                        createdDate = System.currentTimeMillis().toInt(),
                        description = memeTextEt.text.toString(),
                        isFavorite = false,
                        photoUrl = imageSrc.toString(),
                        title = memeHeaderEt.text.toString()
                    )
                )
                Toast.makeText(activity, "Ваш мем успешно создан!", Toast.LENGTH_SHORT).show()
                this@CreateMemeFragment.fragmentManager?.popBackStack()
                return@setOnMenuItemClickListener true
            }

            memeHeaderEt.addTextChangedListener { text ->
                _itemCreate?.isEnabled = text.toString().isNotEmpty()
            }
            fabAddImage.setOnClickListener {
                showDialog()
            }
        }

    }

    private fun showDialog() {
        fragmentManager?.let {
            AddPictureDialogFragment().show(
                it,
                AddPictureDialogFragment::class.java.simpleName
            )
        }
    }
}
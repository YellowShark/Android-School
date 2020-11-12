package ru.yellowshark.surfandroidschool.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentProfileBinding
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.ui.auth.AuthActivity
import ru.yellowshark.surfandroidschool.ui.main.popular.main.MemesAdapter

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val gson by lazy { Gson() }
    private val memesAdapter by lazy { MemesAdapter() }
    private val viewModel: ProfileViewModel by viewModel()
    private val viewStateObserver = Observer<ViewState> { state ->
        when (state) {
            is ViewState.Loading -> showLoading()
            is ViewState.Success -> showContent()
            is ViewState.Error -> showError()
            is ViewState.Destroy -> openLoginView()
        }
    }

    private fun showError() {
        with(binding) {
            Snackbar.make(root, getString(R.string.error_msg), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red))
                .show()
        }
    }

    private fun openLoginView() {
        val intent = Intent(context?.applicationContext, AuthActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun showContent() {
        with(binding) {
            recyclerView.visibility = View.VISIBLE
            progressBar.root.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            recyclerView.visibility = View.GONE
            progressBar.root.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initUi()
        bindData()
    }

    private fun observeViewModel() {
        with(viewModel) {
            viewState.observe(viewLifecycleOwner, viewStateObserver)
            memesLiveData.observe(viewLifecycleOwner) {
                memesAdapter.data = it
            }
        }
    }

    private fun initUi() {
        initRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        binding.toolbar.menu.findItem(R.id.action_log_out).setOnMenuItemClickListener {
            //viewModel.logout()
            showDialog()
            return@setOnMenuItemClickListener true
        }
    }

    private fun showDialog() {
        fragmentManager?.let { it ->
            LogoutDialog { msg ->
                viewModel.logout()
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
            }.show(
                it,
                LogoutDialog::class.simpleName
            )
        }
    }

    private fun bindData() {
        with(binding) {
            user = viewModel.userInfo
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            val gridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            memesAdapter.onItemClick = { meme ->
                val user = viewModel.userInfo
                val action =
                    ProfileFragmentDirections.actionOpenDetails(
                        gson.toJson(meme),
                        gson.toJson(user)
                    )
                view?.let { Navigation.findNavController(it).navigate(action) }
            }
            memesAdapter.onLikeClick = {
                Toast.makeText(activity, "${it.title} was liked", Toast.LENGTH_SHORT).show()
            }
            recyclerView.apply {
                layoutManager = gridLayoutManager
                adapter = memesAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
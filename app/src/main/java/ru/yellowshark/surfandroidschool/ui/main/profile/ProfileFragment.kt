package ru.yellowshark.surfandroidschool.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentProfileBinding
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.ui.auth.AuthActivity
import ru.yellowshark.surfandroidschool.ui.main.popular.main.MemesAdapter
import ru.yellowshark.surfandroidschool.utils.SingleGson
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.showErrorSnackbar
import ru.yellowshark.surfandroidschool.utils.viewBinding

class ProfileFragment : Fragment(R.layout.fragment_profile), MenuItem.OnMenuItemClickListener {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val gson by lazy { SingleGson.getInstance() }
    private val memesAdapter = MemesAdapter()
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
            context?.applicationContext?.showErrorSnackbar(root, getString(R.string.error_msg))
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
        binding.toolbar.menu.findItem(R.id.action_log_out).setOnMenuItemClickListener(this)
    }

    private fun showDialog() {
        fragmentManager?.let { it ->
            LogoutDialog {
                viewModel.logout()
            }.show(
                it,
                LogoutDialog::class.simpleName
            )
        }
    }

    private fun bindData() {
        binding.user = viewModel.userInfo
    }

    private fun initRecyclerView() {
        with(binding) {
            val gridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            memesAdapter.apply {
                onItemClick = { meme, itemView ->
                    val extras = FragmentNavigatorExtras(itemView to meme.photoUrl)
                    val user = viewModel.userInfo
                    val action =
                        ProfileFragmentDirections.actionOpenDetails(
                            gson.toJson(meme),
                            gson.toJson(user)
                        )
                    view?.let {
                        Navigation.findNavController(it)
                            .navigate(R.id.destination_meme_detail, action.arguments, null, extras)
                    }
                }
                onLikeClick = { viewModel.updateLike(it) }
                onShareClick = { meme -> activity?.shareMeme(meme) }
            }
            recyclerView.apply {
                layoutManager = gridLayoutManager
                adapter = memesAdapter
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_log_out)
            showDialog()
        return true
    }
}
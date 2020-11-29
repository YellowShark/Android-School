package ru.yellowshark.surfandroidschool.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentProfileBinding
import ru.yellowshark.surfandroidschool.domain.ResponseError
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.ui.auth.AuthActivity
import ru.yellowshark.surfandroidschool.ui.base.BaseFragment
import ru.yellowshark.surfandroidschool.ui.main.popular.main.AdapterFactory
import ru.yellowshark.surfandroidschool.ui.main.popular.main.MemesAdapter
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.showErrorSnackbar
import ru.yellowshark.surfandroidschool.utils.viewBinding

class ProfileFragment :
    BaseFragment(R.layout.fragment_profile),
    MenuItem.OnMenuItemClickListener {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private lateinit var memesAdapter: MemesAdapter
    private val viewModel: ProfileViewModel by viewModel()

    override fun showError(error: ResponseError) {
        with(binding) {
            context?.applicationContext?.showErrorSnackbar(root, getString(R.string.error_msg))
        }
    }

    override fun destroyView() {
        val intent = Intent(context?.applicationContext, AuthActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun showContent(data: List<Meme>?) {
        with(binding) {
            data?.let { memesAdapter.data = it }
            recyclerView.visibility = View.VISIBLE
            progressBar.root.visibility = View.GONE
        }
    }

    override fun showLoading() {
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
        binding.user = viewModel.getUserInfo()
    }

    private fun initRecyclerView() {
        with(binding) {
            val gridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            memesAdapter = AdapterFactory.getMemesAdapter(
                callingView = view,
                onLike = { meme -> viewModel.updateLike(meme) },
                onShare = { meme -> activity?.shareMeme(meme) }
            )
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
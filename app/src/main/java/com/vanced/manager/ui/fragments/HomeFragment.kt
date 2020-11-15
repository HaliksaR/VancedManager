package com.vanced.manager.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.crowdin.platform.util.inflateWithCrowdin
import com.github.florent37.viewtooltip.ViewTooltip
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.vanced.manager.R
import com.vanced.manager.adapter.AppListAdapter
import com.vanced.manager.adapter.LinkAdapter
import com.vanced.manager.adapter.SponsorAdapter
import com.vanced.manager.databinding.FragmentGrantRootBinding
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.core.BindingFragment
import com.vanced.manager.ui.dialogs.DialogContainer.installAlertBuilder
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.ui.viewmodels.HomeViewModelFactory

open class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    companion object {
        const val INSTALL_FAILED = "install_failed"
        const val REFRESH_HOME = "refresh_home"
    }

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireActivity())
    }

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(requireActivity()) }
    private lateinit var tooltip: ViewTooltip

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)
        with(binding) {
            homeRefresh.setOnRefreshListener { viewModel.fetchData() }
            tooltip = ViewTooltip
                .on(recyclerAppList)
                .position(ViewTooltip.Position.TOP)
                .autoHide(false, 0)
                .color(ResourcesCompat.getColor(requireActivity().resources, R.color.Twitter, null))
                .withShadow(false)
                .corner(25)
                .onHide {
                    prefs.edit { putBoolean("show_changelog_tooltip", false) }
                }
                .text(requireActivity().getString(R.string.app_changelog_tooltip))

            if (prefs.getBoolean("show_changelog_tooltip", true)) {
                tooltip.show()
            }

            recyclerAppList.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = AppListAdapter(requireActivity(), this@HomeFragment.viewModel, tooltip)
                setHasFixedSize(true)
            }

            recyclerSponsors.apply {
                val lm = FlexboxLayoutManager(requireActivity())
                lm.justifyContent = JustifyContent.FLEX_START
                layoutManager = lm
                setHasFixedSize(true)
                adapter = SponsorAdapter(requireActivity(), this@HomeFragment.viewModel)
            }

            recyclerLinks.apply {
                val lm = FlexboxLayoutManager(requireActivity())
                lm.justifyContent = JustifyContent.FLEX_START
                layoutManager = lm
                setHasFixedSize(true)
                adapter = LinkAdapter(requireActivity(), this@HomeFragment.viewModel)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflateWithCrowdin(R.menu.toolbar_menu, menu, resources)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPause() {
        super.onPause()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
        tooltip.close()
        //binding.mainTablayout.removeOnTabSelectedListener(tabListener)
    }

    override fun onResume() {
        super.onResume()
        registerReceivers()
       // binding.mainTablayout.addOnTabSelectedListener(tabListener)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                INSTALL_FAILED -> installAlertBuilder(intent.getStringExtra("errorMsg").toString(), intent.getStringExtra("fullErrorMsg"), requireActivity())
                REFRESH_HOME -> viewModel.fetchData()
            }
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(INSTALL_FAILED)
        intentFilter.addAction(REFRESH_HOME)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }
}


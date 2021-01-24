package org.android.application.presentation.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.android.application.R
import org.android.application.databinding.FragmentHelpBinding
import org.android.application.presentation.core.BaseFragment

class HelpFragment : BaseFragment() {

    lateinit var binding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        loadData()
    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(getString(R.string.help))
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun loadData() {
        val websettings = binding.webView.settings
        websettings.javaScriptEnabled = true
        binding.webView.loadUrl("file:///android_asset/.html")
    }


}
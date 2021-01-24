package org.android.application.presentation.home.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.android.application.R
import org.android.application.data.models.CityItem
import org.android.application.data.models.Main
import org.android.application.databinding.FragmentCityListBinding
import org.android.application.presentation.core.BaseActivity
import org.android.application.presentation.core.BaseFragment
import org.android.application.presentation.home.HomeViewModel
import org.android.application.presentation.home.MainActivity
import org.android.application.presentation.home.adapter.CityItemListAdapter
import org.android.application.presentation.utility.gone
import org.android.application.presentation.utility.showDialog
import org.android.application.presentation.utility.visible
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CityListFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by sharedViewModel()

    lateinit var binding: FragmentCityListBinding

    private var adapter: CityItemListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {

        setupToolbar()

        attachObserver()

        setupAdapter()

        setupListeners()

        homeViewModel.getCityListLocal()
    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(getString(R.string.app_name))
    }

    private fun attachObserver() {
        homeViewModel.cityWeatherRSLiveData.observe(viewLifecycleOwner, Observer {
            it?.apply {
                (activity as BaseActivity).hideProgress()
                (activity as MainActivity).addCityWiseWeatherFragment()
            }
        })

        homeViewModel.cityListLiveData.observe(viewLifecycleOwner, Observer {
            it?.apply {
                this.observe(viewLifecycleOwner, Observer {
                    this.apply {
                        adapter?.setData(this.value.orEmpty())
                    }
                    manageNoDataFound()
                })
            }
        })
    }

    private fun setupListeners() {

        binding.ivMore.setOnClickListener {
            showMoreOptionsDialog()
        }

        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.editSearch.text.toString().isNotEmpty()) {
                    homeViewModel.keyWord = binding.editSearch.text.toString()
                    binding.editSearch.setText("")
                    searchCityWeather()
                }
            }
            return@setOnEditorActionListener false
        }

        binding.ivCancel.setOnClickListener {
            binding.editSearch.setText("")
        }

        binding.fabAdd.setOnClickListener {
            (activity as MainActivity).addSearchCityByMapFragment()
        }
    }

    private fun setupAdapter() {
        adapter = CityItemListAdapter(
            {
                homeViewModel.keyWord = it.name
                searchCityWeather()
            }, {
                showRemoveBookmarkDialog(it)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun searchCityWeather() {
        (activity as BaseActivity).showProgress()
        homeViewModel.getTodayWeatherByCity(
            homeViewModel.keyWord.orEmpty(),
            getString(R.string.open_weather_map_api)
        )
    }

    private fun manageNoDataFound() {
        if (adapter?.itemCount ?: 0 > 0) {
            binding.llNoDataFound.gone()
            binding.recyclerView.visible()
        } else {
            binding.llNoDataFound.visible()
            binding.recyclerView.gone()
        }
    }

    private fun showRemoveBookmarkDialog(cityItem: CityItem) {
        activity?.showDialog(
            getString(R.string.app_name),
            getString(R.string.bookmark_warning),
            getString(R.string.yes), { dialog, which ->
                dialog.dismiss()
                homeViewModel.removeFromBookmarkCity(
                    cityItem
                )
            },
            getString(R.string.no), { dialog, which ->
                dialog.dismiss()
            }
        )
    }

    private fun showMoreOptionsDialog() {
        val arr = arrayOf("Help", "Setting")
        val builder = AlertDialog.Builder(activity!!)
            .setItems(arr, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        (activity as MainActivity).addHelpFragment()
                    }
                    1 -> {
                        (activity as MainActivity).addSettingFragment()
                    }
                }
            })
        builder.create().show()
    }
}
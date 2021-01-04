package org.codejudge.application.presentation.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.codejudge.application.R
import org.codejudge.application.databinding.ActivityMainBinding
import org.codejudge.application.helper.ConfigHelper
import org.codejudge.application.presentation.core.BaseActivity
import org.codejudge.application.presentation.home.adapter.RestaurantListAdapter
import org.codejudge.application.presentation.utility.AppConstant
import org.codejudge.application.presentation.utility.gone
import org.codejudge.application.presentation.utility.visible
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel

    private lateinit var binding: ActivityMainBinding

    private var adapter: RestaurantListAdapter? = null

    private var keyword: String? = ""
    private var pageToken: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        val apiUrl: String = ConfigHelper.getConfigValue(this, "api_url")
        Log.i("onClick ", apiUrl)
    }

    private fun init() {
        attachObserver()

        setupAdapter()

        getRestaurantList()

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                keyword = s.toString()
                manageCancel()
                getRestaurantList()
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        binding.ivCancel.setOnClickListener {
            binding.editSearch.setText("")
        }
    }

    private fun attachObserver() {
        homeViewModel.restaurantListRSLiveData.observe(this, Observer {
            it?.apply {
                this@MainActivity.pageToken = this.nextPageToken
                homeViewModel.hasMore =
                    if (this.nextPageToken?.isNotEmpty() == true) AppConstant.Yes else AppConstant.No
                if (homeViewModel.page == AppConstant.PageStart) {
                    adapter?.setData(this.results.orEmpty())
                } else {
                    adapter?.addData(this.results.orEmpty())
                }

                if (homeViewModel.page == AppConstant.PageStart) {
                    binding.recyclerView.hideShimmerAdapter()
                } else {
                    binding.llPagination.gone()
                }

                homeViewModel.isLoading = false
            }
        })
    }

    private fun setupAdapter() {
        adapter = RestaurantListAdapter {

        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        /*binding.recyclerView.addOnScrollListener(object : EndlessPaginationScrollListener() {
            override fun requestNewPage() {
                super.requestNewPage()
                if (!homeViewModel.isLoading
                    && homeViewModel.hasMore == AppConstant.Yes
                    && this@MainActivity.pageToken?.isNotEmpty() == true){
                    getRestaurantList()
                }
            }
        })*/
    }

    private fun getRestaurantList() {
        if(homeViewModel.page == AppConstant.PageStart) {
            binding.recyclerView.showShimmerAdapter()
        } else {
            binding.llPagination.visible()
        }
        homeViewModel.getRestaurantList(
            getString(R.string.map_key),
            keyword,
            pageToken.orEmpty()
        )
    }

    private fun manageCancel(){
        if(binding.editSearch.text.toString().isEmpty()){
            binding.ivCancel.gone()
        } else {
            binding.ivCancel.visible()
        }
    }
}

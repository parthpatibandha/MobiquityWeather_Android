package org.android.application.presentation.home.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import org.android.application.R
import org.android.application.databinding.FragmentSearchCityOnMapBinding
import org.android.application.presentation.core.BaseActivity
import org.android.application.presentation.core.BaseFragment
import org.android.application.presentation.core.location.MyLocationListener
import org.android.application.presentation.core.location.MyLocationManager
import org.android.application.presentation.home.HomeViewModel
import org.android.application.presentation.home.MainActivity
import org.android.application.presentation.utility.AppConstant
import org.android.application.presentation.utility.Logger
import org.android.application.presentation.utility.showDialog
import org.android.application.presentation.utility.showPermissionSettingDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class SearchCityByMapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private val homeViewModel: HomeViewModel by sharedViewModel()

    lateinit var binding: FragmentSearchCityOnMapBinding

    var mapFragment: SupportMapFragment? = null
    var googleMap: GoogleMap? = null

    private var latLng: LatLng? = null
    private var latitude: String = ""
    private var longitude: String = ""
    private var locationManager: MyLocationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCityOnMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        attachObserver()

        setupToolbar()

        checkForLocationPermission()

        setupMap()

        binding.tvConfirm.setOnClickListener {
            val city = binding.tvAddress.text.toString()
            if(city.isEmpty()){
                activity?.showDialog(
                    getString(R.string.app_name),
                    getString(R.string.cityname_empty),
                    getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    }
                )
                return@setOnClickListener
            }
            homeViewModel.keyWord = city
            searchCityWeather()
            //(activity as MainActivity).addCityWiseWeatherFragment()
        }
    }

    private fun attachObserver() {
        homeViewModel.cityWeatherRSLiveData.observe(viewLifecycleOwner, Observer {
            it?.apply {
                (activity as BaseActivity).hideProgress()
                (activity as MainActivity).addCityWiseWeatherFragment()
            }
        })
    }

    private fun searchCityWeather() {
        (activity as BaseActivity).showProgress()
        homeViewModel.getTodayWeatherByCity(
            homeViewModel.keyWord.orEmpty(),
            getString(R.string.open_weather_map_api)
        )
    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(getString(R.string.add_location))
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun setMarker(isZoom: Boolean) {
        if (latLng != null && googleMap != null) {
            googleMap?.clear()
            googleMap?.addMarker(
                MarkerOptions().position(latLng!!)
            )
            if (isZoom)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

        }
    }

    private fun getAddressFromLatLong(latLng: LatLng) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses?.isNotEmpty() == true) {
            addresses.get(0)?.apply {
                val address = this.getAddressLine(0)
                val city = this.locality
                val state = this.adminArea
                val country = this.countryName

                Logger.d("country : " + country)
                Logger.d("state : " + state)
                Logger.d("city : " + city)
                Logger.d("address : " + address)

                binding.tvAddress.text = city
                setMarker(false)
            }
        } else {
            Logger.d("Enable to get address from geo coder")
        }
    }

    //#region - map

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        setMarker(true)
        Handler().postDelayed(Runnable {
            googleMap?.setOnCameraIdleListener(this)
        }, 3000)
    }

    override fun onCameraIdle() {
        latLng = googleMap?.getCameraPosition()?.target
        latLng?.apply {
            getAddressFromLatLong(this)
        }
    }

    //endregion


    //#region - location permission

    private fun checkForLocationPermission() {
        MayI.withActivity(activity!!)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .onRationale(this::permissionRationaleMulti)
            .onResult(this::permissionResultMulti)
            .onErrorListener(this::inCaseOfError)
            .check()
    }

    private fun permissionRationaleMulti(
        permissions: Array<PermissionBean>,
        token: PermissionToken
    ) {
        activity?.showDialog(getString(R.string.app_name),
            getString(R.string.permission_location),
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                token.continuePermissionRequest()
            })
    }

    private fun inCaseOfError(e: Exception) {
        activity?.showDialog(getString(R.string.app_name),
            "Error for permission : " + e.message,
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun permissionResultMulti(
        permissions: Array<PermissionBean>
    ) {
        var isAllPermissionGranted = true
        permissions.forEach {
            if (!it.isGranted) {
                isAllPermissionGranted = false
                return@forEach
            }
        }
        if (isAllPermissionGranted) {
            getCurrentLocation()
        } else {
            activity?.showPermissionSettingDialog()
        }
    }

    private fun getCurrentLocation() {
        locationManager = MyLocationManager(activity!!)
        locationManager?.myLocationManager = locationListener
        locationManager?.startLocation()
    }

    private var locationListener = object : MyLocationListener {

        override fun onLocationReceived(location: Location) {
            val strLocation = String.format("%f,%f", location.latitude, location.longitude)
            Logger.d("Location received : $strLocation")
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
            latLng = LatLng(location.latitude, location.longitude)
            locationManager?.stopLocation()
            setMarker(true)
        }

        override fun onLocationError() {
            activity?.showDialog(getString(R.string.app_name),
                getString(R.string.location_error),
                getString(R.string.ok), { dialog, which ->
                    dialog.dismiss()
                }, getString(R.string.cancel), { dialog, which ->
                    dialog.dismiss()
                })
        }
    }


    //endregion


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.INTENT_PERMISSION_SETTING) {
            checkForLocationPermission()
        }
    }

}
package com.example.weatherlogger.ui.main

import android.Manifest
import android.app.Activity
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherlogger.R
import com.example.weatherlogger.data.network.model.Coordinates
import com.example.weatherlogger.domain.model.Weather
import com.example.weatherlogger.ui.gone
import com.example.weatherlogger.ui.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_enter_city_name.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainContract.View, HasActivityInjector {

    companion object {
        private const val RC_LOCATION = 1001
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mainPresenter: MainContract.Presenter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var weatherDetailsAdapter = WeatherDetailsAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        mainPresenter.apply {
            attachView(this@MainActivity)
            getStoredWeatherDetails()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initUI() {
        logWeatherFab.setOnClickListener {
            showWeatherByLocation()
        }

        logWeatherByName.setOnClickListener {
            showEnterCityNameDialog()
        }
        weatherDetailsRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = weatherDetailsAdapter
        }
    }


    @AfterPermissionGranted(RC_LOCATION)
    private fun showWeatherByLocation() {
        val requiredPerm = Manifest.permission.ACCESS_COARSE_LOCATION
        if (EasyPermissions.hasPermissions(this, requiredPerm)) {
            fusedLocationClient.lastLocation
                .addOnFailureListener {
                    showError(it.message!!)
                }
                .addOnSuccessListener { location: Location ->
                    val coordinates = Coordinates(location.longitude, location.latitude)
                    mainPresenter.getWeatherDetailsByCoordinates(coordinates)
                }
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.location_rationale),
                RC_LOCATION, requiredPerm
            );
        }

    }

    private fun showEnterCityNameDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_enter_city_name, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        view.apply {
            logWeatherBtn.setOnClickListener {
                dialog.dismiss()
                if (cityNameEt.text.isNotEmpty()) {
                    mainPresenter.getWeatherDetailsByCityName(cityNameEt.text.toString())
                } else showError(getString(R.string.please_enter_required_field))
            }
        }
        dialog.show()
    }

    override fun showWeatherDetails(weatherList: List<Weather>) {
        weatherDetailsAdapter.setData(weatherList.asReversed())
    }

    override fun showNoConnectivityError() {
        showError(getString(R.string.no_network_connectivity))
    }

    override fun showError(errorMessage: String) {
        Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressLayout.visible()
    }

    override fun hideLoading() {
        progressLayout.gone()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

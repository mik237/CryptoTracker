package com.ibrahim.cryptotracker.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ibrahim.cryptotracker.CurrenciesAdapter
import com.ibrahim.cryptotracker.R
import com.ibrahim.cryptotracker.databinding.ActivityMainBinding
import com.ibrahim.cryptotracker.databinding.DialogEditLimitsBinding
import com.ibrahim.cryptotracker.network.Status
import com.ibrahim.cryptotracker.service.TrackingService
import com.ibrahim.cryptotracker.utils.Constants
import com.ibrahim.cryptotracker.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var currenciesAdapter: CurrenciesAdapter

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
        initObservers()
        getLimits()
        startStopTracking(Constants.ACTION_START_TRACKING_SERVICE)
    }

    private fun getLimits() {
        binding.tvMaxLimitValue.text = viewModel.getLimit(Constants.MAX_LIMIT_KEY)
        binding.tvMinLimitValue.text = viewModel.getLimit(Constants.MIN_LIMIT_KEY)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_stop_tracking -> {
                startStopTracking(Constants.ACTION_STOP_TRACKING_SERVICE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        binding.rvCurrencies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = currenciesAdapter
        }

        binding.btnEditLimits.setOnClickListener {
            showEditLimitsDialog()
        }
    }

    private fun initObservers() {
        //observing the status of network api call
        viewModel.fetchFromNetworkLiveData.observe(this) {
            when (it) {
                is Status.Success -> binding.pbNetwork.isVisible = false
                is Status.Error -> Snackbar.make(binding.root, it.error, Snackbar.LENGTH_LONG)
                    .show()
                is Status.Loading -> binding.pbNetwork.isVisible = it.loading
            }
        }


        //observing the status of database
        viewModel.fetchFromDbLiveData.observe(this) {
            binding.tvDisclaimer.text = getString(R.string.disclaimer, it.crypto.disclaimer)
            binding.updatedValue.text = Utils.formatDateTime(it.crypto.updatedISO)
            if (it.currencies.isNotEmpty()) {
                currenciesAdapter.submitList(it.currencies)
            }
        }
    }

    private fun startStopTracking(action: String) {
        val trackingServiceIntent = Intent(applicationContext, TrackingService::class.java)
        trackingServiceIntent.action = action
        startService(trackingServiceIntent)
    }

    @SuppressLint("InflateParams")
    private fun showEditLimitsDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogEditLimitsBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Save") { d, _ ->
            d?.cancel()
            val maxLimit = dialogBinding.etMaxLimitValue.text.toString()
            val minLimit = dialogBinding.etMinLimitValue.text.toString()

            if(maxLimit.isNotEmpty())
                viewModel.saveLimit(maxLimit, Constants.MAX_LIMIT_KEY)

            if(minLimit.isNotEmpty())
                viewModel.saveLimit(minLimit, Constants.MIN_LIMIT_KEY)
            getLimits()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { d, _ -> d?.cancel() }
        val dialog = builder.create()
        dialog.show()
    }
}
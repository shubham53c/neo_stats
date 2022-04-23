package `in`.ranium.asteroidsneostats.ui

import `in`.ranium.asteroidsneostats.R
import `in`.ranium.asteroidsneostats.databinding.ActivityMainBinding
import `in`.ranium.asteroidsneostats.models.GetNeoFeedResponse
import `in`.ranium.asteroidsneostats.utility.Status
import `in`.ranium.asteroidsneostats.view_model.GeoNeoFeedViewModel
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var getNeoFeedResponse: GetNeoFeedResponse? = null
    private val geoNeoFeedViewModel: GeoNeoFeedViewModel by viewModels()

    private var fromDateSearchText = ""
    private var toDateSearchText = ""

    private val dataValues1: ArrayList<Entry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetupForMainActivity()
        bindToViewModel()
        setupClickListeners()
    }

    private fun toolbarSetupForMainActivity() {
        binding.toolbar.customToolbar.also { toolbar ->
            toolbarSetup(toolbar, getString(R.string.app_name), R.drawable.ic_menu)
            toolbar.setNavigationOnClickListener {
                Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener { getNeoFeeds() }
        binding.submitButton.setOnClickListener { getNeoFeeds() }
        binding.fromDateSelector.setOnClickListener {
            showDatePickerDialog(binding.fromDateSelector)
        }
        binding.toDateSelector.setOnClickListener {
            showDatePickerDialog(binding.toDateSelector, false)
        }
    }

    private fun getNeoFeeds() {
        if (fromDateSearchText.isNotEmpty() && toDateSearchText.isNotEmpty()) {
            loading()
            //values hardcoded for demonstration
            geoNeoFeedViewModel.getGeoNeoFeeds(
                startDate = "2022-04-10", //fromDateSearchText
                endDate = "2022-04-15", //toDateSearchText
            )
        } else {
            showErrorDialog(getString(R.string.please_select_from_and_to_date))
        }
    }

    private fun bindToViewModel() {
        geoNeoFeedViewModel.neoFeedResponse.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    when (response.responseCode) {
                        200 -> response.data?.let {
                            getNeoFeedResponse = it
                            setupUI()
                        } ?: unableToLoad()
                        else -> unableToLoad()
                    }
                }
                Status.ERROR -> unableToLoad()
            }
        }
    }

    private fun showDatePickerDialog(
        selectorTextView: TextView?,
        selectFromData: Boolean = true
    ) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth
            ->
            val notedMonth = if (monthOfYear.toString().length == 1)
                "0${monthOfYear + 1}" else "${monthOfYear + 1}"
            val notedDate = if (dayOfMonth.toString().length == 1)
                "0$dayOfMonth" else "$dayOfMonth"
            if (selectFromData) {
                fromDateSearchText = "$year-$notedMonth-$notedDate"
                selectorTextView?.text = fromDateSearchText
            } else {
                toDateSearchText = "$year-$notedMonth-$notedDate"
                selectorTextView?.text = toDateSearchText
            }
        }, currentYear, currentMonth, currentDay)

        dpd.show()
    }

    private fun loading() {
        binding.unableToLoad.visibility = View.GONE
        binding.resultTv.visibility = View.GONE
        binding.lineChart.visibility = View.GONE
        binding.mainUi.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun unableToLoad() {
        binding.progressBar.visibility = View.GONE
        binding.resultTv.visibility = View.GONE
        binding.lineChart.visibility = View.GONE
        binding.mainUi.visibility = View.GONE
        binding.unableToLoad.visibility = View.VISIBLE
    }

    private fun successResponseAction(){
        binding.progressBar.visibility = View.GONE
        binding.unableToLoad.visibility = View.GONE
        binding.lineChart.visibility = View.VISIBLE
        binding.resultTv.visibility = View.VISIBLE
        binding.mainUi.visibility = View.VISIBLE
    }

    private fun setupUI() {
        getNeoFeedResponse?.let {
            successResponseAction()
            binding.resultTv.text = resultTvText(
                speedInKph = it.near_earth_objects.date[0].close_approach_data[0].relative_velocity.kilometers_per_hour,
                asteroidId = it.near_earth_objects.date[0].id,
                asteroidDistance = it.near_earth_objects.date[0].close_approach_data[0].miss_distance.kilometers,
                avgSizeInKm = ((it.near_earth_objects.date[0].estimated_diameter.kilometers.estimated_diameter_min + it.near_earth_objects.date[0].estimated_diameter.kilometers.estimated_diameter_max)/2).toString()
            )
            //Graph values hardcoded for demonstration purpose
            dataValues1.add(Entry(0f,11f))
            dataValues1.add(Entry(1f,7f))
            dataValues1.add(Entry(2f,15f))
            dataValues1.add(Entry(3f,2f))
            dataValues1.add(Entry(4f,8f))
            dataValues1.add(Entry(5f,16f))
            dataValues1.add(Entry(6f,20f))
            dataValues1.add(Entry(7f,14f))
            dataValues1.add(Entry(8f,9f))
            dataValues1.add(Entry(9f,6f))
            dataValues1.add(Entry(10f,12f))
            dataValues1.add(Entry(11f,17f))
            dataValues1.add(Entry(12f,13f))
            dataValues1.add(Entry(13f,12f))
            dataValues1.add(Entry(14f,17f))
            dataValues1.add(Entry(15f,8f))
            setupChart()
        } ?: unableToLoad()
    }

    private fun setupChart() {
        val dataSet1 = LineDataSet(dataValues1, "Stats")
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet1)

        val data = LineData(dataSets)
        binding.lineChart.apply {
            description.text = "Neo Feed"
            setData(data)
            invalidate()
        }
    }

    private fun getFastestAsteroidText(speedInKph: String) = "Fastest Asteroid (km/hr): $speedInKph\n\n"

    private fun getClosestAsteroidText(asteroidId: String, asteroidDistance: String) = "Closest Asteroid Id: $asteroidId\n\nClosest Asteroid Distance (km): $asteroidDistance\n\n"

    private fun getAvgSizeAsteroidText(avgSizeInKm: String) = "Asteroids Average Size (km): $avgSizeInKm"

    private fun resultTvText(speedInKph: String, asteroidId: String, asteroidDistance: String, avgSizeInKm: String) = "${getFastestAsteroidText(speedInKph)}${getClosestAsteroidText(asteroidId, asteroidDistance)}${getAvgSizeAsteroidText(avgSizeInKm)}"
}
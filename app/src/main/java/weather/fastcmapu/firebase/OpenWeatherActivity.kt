package weather.fastcmapu.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_open_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherActivity : AppCompatActivity(), LocationListener {


    private val PERMISSION_REQUEST_CODE = 2000
    private val APP_ID = "85fbd2481de779c6cc65c43ad4d3d1f5"
    private val UNITS = "metric"
    private val LANGUAGE = "kr"
    private lateinit var backPressHolder: OnBackPressHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_weather)


        backPressHolder = OnBackPressHolder()
        getLocationInfo()

        setting.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))

        }
    }

    override fun onBackPressed() {
        backPressHolder.onBackPressed()
    }

    private fun drawCurrentWeather(currentWeather: TotalWeather) {
        with(currentWeather) {

            this.weatherList?.getOrNull(0)?.let {
                it.icon?.let {
                    val glide = Glide.with(this@OpenWeatherActivity)
                    glide
                        .load(Uri.parse("https://openweathermap.org/img/w/" + it + ".png"))
                        .into(current_icon)
                }
                it.main?.let { current_main.text = it }
                it.description?.let { current_description.text = it }
            }

            this.main?.temp?.let { current_now.text = String.format("%.1f", it) }
            this.main?.tempMax?.let { current_max.text = String.format("%.1f", it) }
            this.main?.tempMin?.let { current_min.text = String.format("%.1f", it) }

            loading_view.visibility = View.GONE
            weateher_view.visibility = View.VISIBLE
        }
    }

    private fun getLocationInfo() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this@OpenWeatherActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@OpenWeatherActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                reqeustWeatherInfoOfLocation(latitude = latitude, logitude = longitude)
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0F,
                    this
                )
                locationManager.removeUpdates(this)
            }
        }
    }


    private fun reqeustWeatherInfoOfLocation(latitude: Double, logitude: Double) {
        (application as WeatherApplication)
            .requestService()
            ?.getWeatherInfoOfCoordinates(
                latitude = latitude,
                logitude = logitude,
                appID = APP_ID,
                units = UNITS,
                language = LANGUAGE
            )
            ?.enqueue(object : Callback<TotalWeather> {

                override fun onFailure(call: Call<TotalWeather>, t: Throwable) {
                    loading_text.text = "로딩 실패"
                }

                override fun onResponse(
                    call: Call<TotalWeather>,
                    response: Response<TotalWeather>
                ) {
                    if (response.isSuccessful) {
                        val totalWeather = response.body()
                        totalWeather?.let {
                            drawCurrentWeather(it)
                        }
                    } else loading_text.text = "로딩 실패"
                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) getLocationInfo()
        }
    }

    override fun onLocationChanged(location: Location?) {
        val latitude = location?.latitude
        val longitude = location?.longitude
        if (latitude != null && longitude != null) {
            reqeustWeatherInfoOfLocation(latitude = latitude, logitude = longitude)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    inner class OnBackPressHolder() {

        private var backPressHolder: Long = 0

        fun onBackPressed() {
            if (System.currentTimeMillis() > backPressHolder + 2000) {
                backPressHolder = System.currentTimeMillis()
                showBackToast()
                return
            }
            if (System.currentTimeMillis() <= backPressHolder + 2000) {
                finishAffinity()
            }
        }

        fun showBackToast() {
            Toast.makeText(this@OpenWeatherActivity, "한번더 누르시면 종료합니다", Toast.LENGTH_SHORT).show()
        }
    }

}

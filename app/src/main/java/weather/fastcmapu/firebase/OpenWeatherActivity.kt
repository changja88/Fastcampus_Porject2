package weather.fastcmapu.firebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_open_weather.*

class OpenWeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_weather)


        setting.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))
        }
    }
}
package weather.fastcmapu.firebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    private val RC_SIGN_IN = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("startt", "MainActivity onCreate")


        checkPreviousLogin()
    }

    private fun checkPreviousLogin() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) showLoginWindow()
        else moveToOpenWeatherActivity()
    }

    private fun moveToOpenWeatherActivity() {
        startActivity(Intent(this, OpenWeatherActivity::class.java))
    }

    private fun showLoginWindow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.main_icon)
                .setTheme(R.style.GreenTheme)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                moveToOpenWeatherActivity()
            } else {
                Toast.makeText(this, "로그인 실패, 로그인을 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
        }
    }


}

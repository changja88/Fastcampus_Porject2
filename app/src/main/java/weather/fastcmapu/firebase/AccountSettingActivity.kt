package weather.fastcmapu.firebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        setupListener()
    }

    private fun setupListener() {
        account_setting_back.setOnClickListener { onBackPressed() }
        account_setting_logout.setOnClickListener { signoutAccount() }
        account_setting_delete.setOnClickListener { showDeleteDialog() }
    }


    private fun signoutAccount() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "로그아웃 하셨습니다", Toast.LENGTH_SHORT).show()
                moveToMainActivity()
            }

    }

    private fun deleteAccount() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                Toast.makeText(this, "탈퇴 하셨습니다", Toast.LENGTH_SHORT).show()
                moveToMainActivity()
            }
    }

    private fun showDeleteDialog() {
        AccontDeleteDialog().apply {
            addAccountDeleDilaogInterface(object : AccontDeleteDialog.AccountDeleDilaogInterface {
                override fun delete() {
                    deleteAccount()
                }

                override fun cancelDelete() {
                }
            })
        }.show(supportFragmentManager, "")
1    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}

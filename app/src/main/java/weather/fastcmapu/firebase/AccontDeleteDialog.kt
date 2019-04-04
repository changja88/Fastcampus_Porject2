package weather.fastcmapu.firebase

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.account_delete_dialog.*

class AccontDeleteDialog : DialogFragment() {

    interface AccountDeleDilaogInterface {
        fun delete()
        fun cancelDelete()
    }

    private var accountDeleteDilaogInterface: AccountDeleDilaogInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_delete_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    fun addAccountDeleDilaogInterface(listener: AccountDeleDilaogInterface) {
        accountDeleteDilaogInterface = listener
    }

    private fun setupListener() {
        delete_no.setOnClickListener {
            accountDeleteDilaogInterface?.cancelDelete()
            dismiss()
        }
        delete_yes.setOnClickListener {
            accountDeleteDilaogInterface?.delete()
            dismiss()
        }
    }


}
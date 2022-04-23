package `in`.ranium.asteroidsneostats.ui

import `in`.ranium.asteroidsneostats.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private var customProgressDialog: Dialog? = null


    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.snackbar_error_color
            )
        )
        snackBar.show()
    }


    fun showProgressDialog(ctx: Context = this, progressText: String = getString(R.string.loading)) {
        customProgressDialog = Dialog(ctx)
        customProgressDialog?.apply {
            setContentView(R.layout.custom_progress_dialog)
            val tvProgressText: TextView = findViewById(R.id.progressText)
            tvProgressText.text = progressText
            setCancelable(false)
            show()
        }
    }

    fun hideProgressDialog() {
        customProgressDialog?.dismiss()
    }

    fun showErrorDialog(msg: String, ctx: Context = this) {
        val alertDialog = AlertDialog.Builder(ctx)
        alertDialog.apply {
            setMessage(msg)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    fun showActionDialog(buttonText: String, msg: String, action: () -> Unit) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setMessage(msg)
            setCancelable(false)
            setPositiveButton(buttonText) { dialog, _ ->
                action()
                dialog.dismiss()
            }
            show()
        }
    }

    fun toolbarSetup(toolbar: Toolbar, titleText: String, icon: Int = R.drawable.ic_baseline_arrow_back_24) {
        toolbar.apply {
            title = titleText

        }
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(icon)
        }
    }
}
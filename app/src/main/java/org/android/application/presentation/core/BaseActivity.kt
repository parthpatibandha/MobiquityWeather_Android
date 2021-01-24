package org.android.application.presentation.core

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.keffys.android.domain.exceptions.MyException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.android.application.R
import org.android.application.presentation.utility.Logger
import org.android.application.presentation.utility.showDialog
import kotlin.coroutines.CoroutineContext


abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    lateinit var job: Job
    private var progress: CustomProgressDialog? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    abstract fun getBaseViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        progress = CustomProgressDialog(this)

        attachBaseObserver()

    }

    private fun attachBaseObserver() {
        getBaseViewModel()?.exceptionLiveData?.observe(this, Observer {
            hideProgress()
            it?.apply {
                when (this) {
                    is MyException.InternetConnectionException -> showErrorDialog(getString(R.string.exception_error_network))
                    is MyException.JsonException -> showErrorDialog(getString(R.string.exception_error_unparceble))
                    is MyException.ServerNotAvailableException -> showErrorDialog(getString(R.string.city_not_found))
                    is MyException.DatabaseException -> showErrorDialog(getString(R.string.exception_error_database))
                    is MyException.NetworkCallCancelException -> {
                    }
                    else -> {
                        var message = it.message
                        if (message?.isEmpty() == true) {
                            message = it.localizedMessage
                        }
                        showErrorDialog("Unknown error : " + message)
                    }
                }
            }
        })
    }

    private fun showErrorDialog(message: String) {
        showDialog(
            getString(R.string.app_name),
            message,
            getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    protected fun addFragment(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String
    ) {
        supportFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()

        Logger.d("New backStackEntryCount: " + supportFragmentManager.backStackEntryCount)
    }

    protected fun replaceFragment(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean? = false
    ) {
        supportFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment, fragmentTag)
            .addToBackStack(fragmentTag)
            .commit()

        Logger.d("New backStackEntryCount: " + supportFragmentManager.backStackEntryCount)
    }

    protected fun replaceFragmentWithPop(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean? = false
    ) {
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()
    }


    fun showProgress() {
        if (!this.isFinishing) {
            progress?.show()
        }
    }

    fun hideProgress() {
        if (!this.isFinishing && progress?.isShowing == true) {
            progress?.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}
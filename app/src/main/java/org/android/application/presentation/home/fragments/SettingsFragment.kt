package org.android.application.presentation.home.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pixplicity.easyprefs.library.Prefs
import org.android.application.R
import org.android.application.databinding.FragmentHelpBinding
import org.android.application.databinding.FragmentSettingsBinding
import org.android.application.presentation.core.BaseFragment
import org.android.application.presentation.home.HomeViewModel
import org.android.application.presentation.utility.AppConstant
import org.android.application.presentation.utility.PrefKey
import org.android.application.presentation.utility.showDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by sharedViewModel()

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        binding.btnSave.setOnClickListener {
            saveUnits()
        }

        val unit = Prefs.getString(PrefKey.PREF_UNIT, AppConstant.UnitMetric)
        if (unit == AppConstant.UnitMetric) {
            binding.radioMetric.isChecked = true
        } else {
            binding.radioImperial.isChecked = true
        }

    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(getString(R.string.settings))
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun saveUnits() {
        if (binding.radioMetric.isChecked) {
            Prefs.putString(PrefKey.PREF_UNIT, AppConstant.UnitMetric)
        } else {
            Prefs.putString(PrefKey.PREF_UNIT, AppConstant.UniImperial)
        }

        activity?.showDialog(
            getString(R.string.app_name),
            getString(R.string.save_successfully),
            getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                activity?.onBackPressed()
            }
        )

    }


}
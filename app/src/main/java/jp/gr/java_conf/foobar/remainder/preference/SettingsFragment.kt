package jp.gr.java_conf.foobar.remainder.preference

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import jp.gr.java_conf.foobar.remainder.BuildConfig
import jp.gr.java_conf.foobar.remainder.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val licensePreference = findPreference<Preference>("license")
        licensePreference?.apply {
            setOnPreferenceClickListener {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                true
            }
        }

        val versionPreference = findPreference<Preference>("version")
        versionPreference?.apply {
            summaryProvider = Preference.SummaryProvider<Preference> {
                BuildConfig.VERSION_NAME
            }
        }
    }
}
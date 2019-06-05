package io.legado.app.ui.config

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceFragmentCompat
import io.legado.app.App
import io.legado.app.R
import io.legado.app.lib.theme.ColorUtils
import io.legado.app.utils.getCompatColor
import io.legado.app.utils.getPrefBoolean
import io.legado.app.utils.putPrefInt
import io.legado.app.utils.upTint


class ThemeConfigFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_config_theme)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        when (key) {
            "colorPrimary", "colorAccent", "colorBackground" -> if (!ColorUtils.isColorLight(
                    sharedPreferences.getInt(
                        "colorBackground",
                        App.INSTANCE.getCompatColor(R.color.md_grey_100)
                    )
                )
            ) {
                AlertDialog.Builder(App.INSTANCE)
                    .setTitle("白天背景太暗")
                    .setMessage("将会恢复默认背景？")
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        App.INSTANCE.putPrefInt("colorBackground", App.INSTANCE.getCompatColor(R.color.md_grey_100))
                        upTheme(false)
                    }
                    .setNegativeButton(R.string.cancel) { dialogInterface, i -> upTheme(false) }
                    .show().upTint
            } else {
                upTheme(false)
            }
            "colorPrimaryNight", "colorAccentNight", "colorBackgroundNight" -> if (ColorUtils.isColorLight(
                    sharedPreferences.getInt(
                        "colorBackgroundNight",
                        App.INSTANCE.getCompatColor(R.color.md_grey_800)
                    )
                )
            ) {
                AlertDialog.Builder(App.INSTANCE)
                    .setTitle("夜间背景太亮")
                    .setMessage("将会恢复默认背景？")
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        App.INSTANCE.putPrefInt(
                            "colorBackgroundNight",
                            App.INSTANCE.getCompatColor(R.color.md_grey_800)
                        )
                        upTheme(true)
                    }
                    .setNegativeButton(R.string.cancel) { dialogInterface, i -> upTheme(true) }
                    .show()
            } else {
                upTheme(true)
            }
        }

    }

    private fun upTheme(isNightTheme: Boolean) {
        if (App.INSTANCE.getPrefBoolean("isNightTheme", false) == isNightTheme) {
            App.INSTANCE.upThemeStore()
        }
    }

}
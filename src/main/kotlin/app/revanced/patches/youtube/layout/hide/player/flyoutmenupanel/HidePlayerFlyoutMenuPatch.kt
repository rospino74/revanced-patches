package app.revanced.patches.youtube.layout.hide.player.flyoutmenupanel

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.shared.misc.settings.preference.impl.PreferenceScreen
import app.revanced.util.resource.StringResource
import app.revanced.patches.shared.misc.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.misc.litho.filter.LithoFilterPatch
import app.revanced.patches.youtube.misc.playertype.PlayerTypeHookPatch
import app.revanced.patches.youtube.misc.settings.SettingsPatch

@Patch(
    name = "Player flyout menu",
    description = "Adds options to hide menu items that appear when pressing the gear icon in the video player.",
    dependencies = [
        LithoFilterPatch::class,
        PlayerTypeHookPatch::class,
        SettingsPatch::class
    ],
    compatiblePackages = [
        CompatiblePackage(
            "com.google.android.youtube", [
                "18.32.39",
                "18.37.36",
                "18.38.44",
                "18.43.45",
                "18.44.41",
                "18.45.43",
                "18.48.39",
                "18.49.37",
                "19.01.34",
                "19.02.34"
            ]
        )
    ]
)
@Suppress("unused")
object HidePlayerFlyoutMenuPatch : ResourcePatch() {
    private const val KEY = "revanced_hide_player_flyout"

    private const val FILTER_CLASS_DESCRIPTOR =
        "Lapp/revanced/integrations/youtube/patches/components/PlayerFlyoutMenuItemsFilter;"

    override fun execute(context: ResourceContext) {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            PreferenceScreen(
                titleKey = KEY,
                summaryKey = "${KEY}_summary",
                preferences = setOf(
                    SwitchPreference("${KEY}_captions"),
                    SwitchPreference("${KEY}_additional_settings"),
                    SwitchPreference("${KEY}_loop_video"),
                    SwitchPreference("${KEY}_ambient_mode"),
                    SwitchPreference("${KEY}_report"),
                    SwitchPreference("${KEY}_help"),
                    SwitchPreference("${KEY}_speed"),
                    SwitchPreference("${KEY}_more_info"),
                    SwitchPreference("${KEY}_audio_track"),
                    SwitchPreference("${KEY}_watch_in_vr"),
                ),
            )
        )

        LithoFilterPatch.addFilter(FILTER_CLASS_DESCRIPTOR)
    }
}

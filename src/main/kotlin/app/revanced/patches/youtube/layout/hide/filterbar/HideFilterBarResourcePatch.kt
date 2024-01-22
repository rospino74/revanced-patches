package app.revanced.patches.youtube.layout.hide.filterbar

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.shared.misc.mapping.ResourceMappingPatch
import app.revanced.patches.shared.misc.settings.preference.impl.PreferenceScreen
import app.revanced.util.resource.StringResource
import app.revanced.patches.shared.misc.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.misc.settings.SettingsPatch

@Patch(dependencies = [SettingsPatch::class, ResourceMappingPatch::class])
internal object HideFilterBarResourcePatch : ResourcePatch() {
    internal var filterBarHeightId = -1L
    internal var relatedChipCloudMarginId = -1L
    internal var barContainerHeightId = -1L

    override fun execute(context: ResourceContext) {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            PreferenceScreen(
                titleKey =  "revanced_hide_filter_bar_preference",
                summaryKey = "revanced_hide_filter_bar_preference_summary",
                preferences =  setOf(
                    SwitchPreference("revanced_hide_filter_bar_feed_in_feed"),
                    SwitchPreference("revanced_hide_filter_bar_feed_in_search"),
                    SwitchPreference("revanced_hide_filter_bar_feed_in_related_videos"),
                )
            )
        )

        relatedChipCloudMarginId = "related_chip_cloud_reduced_margins".layoutResourceId("layout")
        filterBarHeightId = "filter_bar_height".layoutResourceId()
        barContainerHeightId = "bar_container_height".layoutResourceId()
    }

        private fun String.layoutResourceId(type: String = "dimen") =
            ResourceMappingPatch.resourceMappings.single { it.type == type && it.name == this }.id
}
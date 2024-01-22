package app.revanced.patches.youtube.layout.hide.floatingmicrophone

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.shared.misc.mapping.ResourceMappingPatch
import app.revanced.util.resource.StringResource
import app.revanced.patches.shared.misc.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.misc.settings.SettingsPatch

@Patch(
    dependencies = [
        SettingsPatch::class,
        ResourceMappingPatch::class
    ]
)
internal object HideFloatingMicrophoneButtonResourcePatch : ResourcePatch() {
    internal var fabButtonId: Long = -1

    override fun execute(context: ResourceContext) {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(SwitchPreference("revanced_hide_floating_microphone_button"))

        fabButtonId = ResourceMappingPatch.resourceMappings.find { it.type == "id" && it.name == "fab" }?.id
            ?: throw PatchException("Can not find required fab button resource id")
    }
}

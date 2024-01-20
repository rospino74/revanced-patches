package app.revanced.patches.shared.misc.settings.util

import app.revanced.patches.shared.misc.settings.preference.BasePreference
import app.revanced.patches.shared.misc.settings.preference.impl.PreferenceCategory
import app.revanced.patches.shared.misc.settings.preference.impl.PreferenceScreen
import java.io.Closeable

abstract class BasePreferenceScreen(
    private val root: MutableSet<Screen> = mutableSetOf()
) : Closeable {

    override fun close() {
        if (root.isEmpty()) return

        root.forEach { preference ->
            commit(preference.transform())
        }
    }

    /**
     * Finalize and insert root preference into resource patch
     */
    abstract fun commit(screen: PreferenceScreen)

    open inner class Screen(
        key: String,
        titleKey: String,
        private val summaryKey: String? = null,
        preferences: MutableSet<BasePreference> = mutableSetOf(),
        val categories: MutableSet<Category> = mutableSetOf()
    ) : BasePreferenceCollection(key, titleKey, preferences) {

        /**
         * Initialize using title and summary keys with suffix "_title" and "_summary".
         */
        constructor(
            key: String,
            preferences: MutableSet<BasePreference> = mutableSetOf(),
            categories: MutableSet<Category> = mutableSetOf()
        ) : this(key, key + "_title", key + "_summary", preferences, categories)

        override fun transform(): PreferenceScreen {
            return PreferenceScreen(
                key,
                titleKey,
                summaryKey,
                // Screens and preferences are sorted at runtime by integrations code,
                // so they appear in alphabetical order for the localized language in use.
                preferences + categories.map { it.transform() }
            )
        }

        private fun ensureScreenInserted() {
            // Add to screens if not yet done
            if (!root.contains(this))
                root.add(this)
        }

        fun addPreferences(vararg preferences: BasePreference) {
            ensureScreenInserted()
            this.preferences.addAll(preferences)
        }

        open inner class Category(
            key: String,
            titleKey: String,
            preferences: MutableSet<BasePreference> = mutableSetOf()
        ) : BasePreferenceCollection(key, titleKey, preferences) {
            override fun transform(): PreferenceCategory {
                return PreferenceCategory(
                    key,
                    titleKey,
                    preferences
                )
            }

            fun addPreferences(vararg preferences: BasePreference) {
                ensureScreenInserted()

                // Add to the categories if not done yet.
                if (!categories.contains(this))
                    categories.add(this)

                this.preferences.addAll(preferences)
            }
        }
    }

    abstract class BasePreferenceCollection(
        val key: String,
        val titleKey: String,
        val preferences: MutableSet<BasePreference> = mutableSetOf()
    ) {
        abstract fun transform(): BasePreference
    }
}
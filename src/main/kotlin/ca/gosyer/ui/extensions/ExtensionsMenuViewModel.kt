/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.ui.extensions

import ca.gosyer.data.extension.ExtensionPreferences
import ca.gosyer.data.models.Extension
import ca.gosyer.data.server.ServerPreferences
import ca.gosyer.data.server.interactions.ExtensionInteractionHandler
import ca.gosyer.ui.base.vm.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mu.KotlinLogging
import javax.inject.Inject

class ExtensionsMenuViewModel @Inject constructor(
    private val extensionHandler: ExtensionInteractionHandler,
    serverPreferences: ServerPreferences,
    private val extensionPreferences: ExtensionPreferences
) : ViewModel() {
    private val logger = KotlinLogging.logger {}

    val serverUrl = serverPreferences.server().stateIn(scope)

    private val _extensions = MutableStateFlow(emptyList<Extension>())
    val extensions = _extensions.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        scope.launch {
            getExtensions()
        }
    }

    private suspend fun getExtensions() {
        try {
            _isLoading.value = true
            val enabledLangs = extensionPreferences.languages().get()
            val extensions = extensionHandler.getExtensionList()
            _extensions.value = extensions.filter { it.lang in enabledLangs }.sortedWith(compareBy({ it.lang }, { it.pkgName }))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
        } finally {
            _isLoading.value = false
        }
    }

    fun install(extension: Extension) {
        logger.info { "Install clicked" }
        scope.launch {
            try {
                extensionHandler.installExtension(extension)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
            getExtensions()
        }
    }

    fun uninstall(extension: Extension) {
        logger.info { "Uninstall clicked" }
        scope.launch {
            try {
                extensionHandler.uninstallExtension(extension)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
            getExtensions()
        }
    }
}

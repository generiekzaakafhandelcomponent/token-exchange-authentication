/*
 * Copyright 2015-2026 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ritense.valtimoplugins.tokenexchangeauth

import com.ritense.plugin.annotation.PluginCategory
import javax.net.ssl.SSLContext

/**
 * Generic authentication contract for plugins that need a JWT obtained via OAuth2/Keycloak
 * token-exchange. Deliberately minimal and free of any HTTP client type (RestClient, WebClient,
 * ...), so any plugin can consume it regardless of which HTTP client it uses internally.
 *
 * [getSslContext] is an optional extension point: some gateways (e.g. the ZGW wsgateway) require
 * a client certificate on top of the JWT. It defaults to null, so implementations that don't need
 * mTLS aren't forced to deal with it.
 */
@PluginCategory("token-exchange-authentication")
interface TokenExchangeAuthentication {
    fun getAccessToken(): String

    fun getSslContext(): SSLContext? = null
}

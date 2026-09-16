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

package com.ritense.valtimoplugins.tokenexchangeauth.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.valtimoplugins.tokenexchangeauth.TokenExchangeAuthentication
import com.ritense.valtimoplugins.tokenexchangeauth.client.CachedAccessToken
import com.ritense.valtimoplugins.tokenexchangeauth.client.MtlsContextFactory
import com.ritense.valtimoplugins.tokenexchangeauth.client.TokenExchangeClient
import com.ritense.valtimoplugins.tokenexchangeauth.client.TokenExchangeConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import java.net.URI
import javax.net.ssl.SSLContext

/**
 * Authenticates with a JWT obtained via a Keycloak client_credentials + token-exchange flow.
 *
 * Standalone plugin: it has no dependency on any other plugin's authentication interface, so it
 * can be selected wherever a `TokenExchangeAuthentication` configuration is required.
 */
@Plugin(
    key = "token-exchange-authentication",
    title = "Token Exchange Authentication Plugin",
    description = "Authenticates using a Keycloak client_credentials + token-exchange JWT",
)
@Suppress("UNUSED")
class TokenExchangeAuthenticationPlugin(
    private val tokenExchangeClient: TokenExchangeClient,
) : TokenExchangeAuthentication {

    @PluginProperty(key = "tokenEndpoint", secret = false, required = true)
    lateinit var tokenEndpoint: URI

    @PluginProperty(key = "clientId", secret = false, required = true)
    lateinit var clientId: String

    @PluginProperty(key = "clientSecret", secret = true, required = true)
    lateinit var clientSecret: String

    @PluginProperty(key = "audience", secret = false, required = true)
    lateinit var audience: String

    @PluginProperty(key = "scope", secret = false, required = false)
    var scope: String? = null

    @PluginProperty(key = "keystorePath", secret = false, required = false)
    var keystorePath: String? = null

    @PluginProperty(key = "keystoreSecret", secret = true, required = false)
    var keystoreSecret: String? = null

    @PluginProperty(key = "truststorePath", secret = false, required = false)
    var truststorePath: String? = null

    @PluginProperty(key = "truststoreSecret", secret = true, required = false)
    var truststoreSecret: String? = null

    @Volatile
    private var cachedAccessToken: CachedAccessToken? = null
    private val resolvedSslContext: SSLContext? by lazy {
        val ksPath = keystorePath
        val ksSecret = keystoreSecret
        if (ksPath.isNullOrBlank() || ksSecret.isNullOrBlank()) {
            null
        } else {
            MtlsContextFactory.createFromKeystore(
                keystorePath = ksPath,
                keystoreSecret = ksSecret,
                truststorePath = truststorePath?.takeIf { it.isNotBlank() },
                truststoreSecret = truststoreSecret,
            )
        }
    }

    override fun getAccessToken(): String {
        cachedAccessToken?.takeUnless { it.isExpired() }?.let { return it.accessToken }

        synchronized(this) {
            cachedAccessToken?.takeUnless { it.isExpired() }?.let { return it.accessToken }

            logger.debug { "Exchanging a new JWT via Keycloak token-exchange for audience '$audience'" }

            val response = tokenExchangeClient.exchangeToken(
                TokenExchangeConfig(
                    tokenEndpoint = tokenEndpoint,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    audience = audience,
                    scope = scope,
                )
            )

            return CachedAccessToken(response.accessToken!!, response.expiresIn)
                .also { cachedAccessToken = it }
                .accessToken
        }
    }

    override fun getSslContext(): SSLContext? = resolvedSslContext

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}

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

import {PluginSpecification} from '@valtimo/plugin';
import {TOKEN_EXCHANGE_AUTHENTICATION_PLUGIN_LOGO_BASE64} from './assets';
import {
    TokenExchangeAuthenticationPluginConfigurationComponent
} from './components/token-exchange-authentication-plugin-configuration.component';

const tokenExchangeAuthenticationPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-end
     */
    pluginId: 'token-exchange-authentication',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: TokenExchangeAuthenticationPluginConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: TOKEN_EXCHANGE_AUTHENTICATION_PLUGIN_LOGO_BASE64,
    /*
    For each language key an implementation supports, translation keys with a translation are provided below.
    These can then be used in configuration components using the pluginTranslate pipe or the PluginTranslationService.
    At a minimum, the keys 'title' and 'description' need to be defined.
     */
    pluginTranslations: {
        nl: {
            configurationTitle: 'Configuratienaam',
            configurationTitleTooltip: 'Token Exchange Authenticatie Plugin',
            title: 'Token Exchange Authenticatie Plugin',
            description: 'Authenticeert via een Keycloak client_credentials + token-exchange flow',
            tokenEndpoint: 'Token endpoint URL',
            clientId: 'Client ID',
            clientSecret: 'Client secret',
            audience: 'Audience',
            scope: 'Scope',
            keystorePath: 'Keystore pad (JKS)',
            keystoreSecret: 'Keystore wachtwoord',
            truststorePath: 'Truststore pad (JKS, optioneel)',
            truststoreSecret: 'Truststore wachtwoord',
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip: 'Token Exchange Authentication Plugin',
            title: 'Token Exchange Authentication Plugin',
            description: 'Authenticates via a Keycloak client_credentials + token-exchange flow',
            tokenEndpoint: 'Token endpoint URL',
            clientId: 'Client ID',
            clientSecret: 'Client secret',
            audience: 'Audience',
            scope: 'Scope',
            keystorePath: 'Keystore path (JKS)',
            keystoreSecret: 'Keystore password',
            truststorePath: 'Truststore path (JKS, optional)',
            truststoreSecret: 'Truststore password',
        }
    }
};

export {tokenExchangeAuthenticationPluginSpecification};

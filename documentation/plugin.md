# Plugin Documentation

## Overview

Generic, standalone Keycloak/OAuth2 token-exchange authentication plugin.

It performs a two-step exchange against a Keycloak (or other OIDC) token endpoint:

1. `grant_type=client_credentials` to obtain a subject access token for the configured client.
2. `grant_type=urn:ietf:params:oauth:grant-type:token-exchange` with that subject token to obtain
   a JWT scoped to the configured `audience`.

The resulting JWT is exposed via `getAccessToken()`. Tokens are cached in memory until shortly
before they expire, to avoid exchanging a new token on every call.

It implements its own minimal, HTTP-client-agnostic interface:

```kotlin
@PluginCategory("token-exchange-authentication")
interface TokenExchangeAuthentication {
    fun getAccessToken(): String
    fun getSslContext(): SSLContext? = null
}
```

This plugin has no dependency on any other plugin's authentication interface or category. Any
plugin that needs Keycloak token-exchange authentication can take a `TokenExchangeAuthentication`
typed `@PluginProperty` and call `getAccessToken()` to obtain the bearer token, applying it
however its own HTTP client works (`RestClient`, `WebClient`, ...).

Some gateways (e.g. the ZGW wsgateway) also require a client certificate (mTLS) on top of the
JWT. Rather than depending on a separate SSL-context plugin, this plugin can build its own
`SSLContext` directly from `keystorePath` + `keystoreSecret` (+ optional
`truststorePath`/`truststoreSecret`), loading a JKS keystore file from disk. `getSslContext()`
stays `null` when no keystore is configured, so plugins that don't need mTLS are unaffected.

## Dependencies

Published separately per supported Valtimo major version — see
[Supported Valtimo versions](../README.md#supported-valtimo-versions) in the README for the full
version matrix and branch mapping.

### Backend

```kotlin
dependencies {
    // Valtimo 13.x
    implementation("com.ritense.valtimoplugins:token-exchange-authentication:0.0.1")
    // Valtimo 12.x
    implementation("com.ritense.valtimoplugins:token-exchange-authentication:0.0.1-V12")
}
```

### Frontend

```json
{
  "dependencies": {
    "@valtimo-plugins/token-exchange-authentication": "0.0.1"
  }
}
```

In your `app.module.ts`:

```typescript
import {
    TokenExchangeAuthenticationPluginModule, tokenExchangeAuthenticationPluginSpecification,
} from '@valtimo-plugins/token-exchange-authentication';

@NgModule({
    imports: [
        TokenExchangeAuthenticationPluginModule,
    ],
    providers: [
        {
            provide: PLUGINS_TOKEN,
            useValue: [
                tokenExchangeAuthenticationPluginSpecification,
            ]
        }
    ]
})
```

## Configuration

| Property         | Type   | Required | Secret | Description                                                             |
|------------------|--------|----------|--------|-------------------------------------------------------------------------|
| tokenEndpoint    | string | Yes      | No     | The Keycloak (or other OIDC) token endpoint URL                         |
| clientId         | string | Yes      | No     | The client id used for both the client_credentials and exchange step    |
| clientSecret     | string | Yes      | Yes    | The client secret                                                       |
| audience         | string | Yes      | No     | The audience the exchanged JWT should be scoped to                      |
| scope            | string | No       | No     | An optional OAuth2 scope to request                                     |
| keystorePath     | string | No       | No     | Path to a JKS keystore file on disk, used to build an mTLS `SSLContext` |
| keystoreSecret   | string | No       | Yes    | The keystore password                                                   |
| truststorePath   | string | No       | No     | Path to a JKS truststore file on disk (optional)                        |
| truststoreSecret | string | No       | Yes    | The truststore password                                                 |

## Actions

This plugin does not expose any process actions. It is consumed by other plugins that take a
`TokenExchangeAuthentication` typed `@PluginProperty` and call `getAccessToken()` (and optionally
`getSslContext()`) to authenticate their own outgoing requests.

## Usage

Configure an instance of the plugin with your Keycloak token endpoint, client credentials and
target audience. Any other plugin that declares a `TokenExchangeAuthentication` typed
`@PluginProperty` can then select this configuration to authenticate its requests.

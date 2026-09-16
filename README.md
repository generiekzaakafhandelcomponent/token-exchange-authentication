# Token Exchange Authentication Plugin

Generic, standalone Keycloak/OAuth2 token-exchange authentication plugin for GZAC/Valtimo. See
[Plugin Documentation](documentation/plugin.md) for the full description, configuration
properties, and usage.

## Supported Valtimo versions

This plugin is published separately for each supported Valtimo major version, from a dedicated
branch per version:

| Branch  | Valtimo version | Process engine | Backend artifact version | Frontend package version |
|---------|------------------|-----------------|---------------------------|---------------------------|
| `main`  | 13.x (Operaton)  | Operaton        | `0.0.1`                   | `0.0.1`                   |
| `v12`   | 12.x (Camunda 7) | Camunda 7       | `0.0.1-V12`                | `0.0.1-V12`               |

Both branches build the exact same plugin sources — the plugin has no direct dependency on the
process engine (it has no `@PluginAction`), so only the Valtimo/Spring Boot version pins and a
couple of Valtimo test-module coordinates differ between branches. Pushes to either branch
publish independently via [`publish-backend.yaml`](.github/workflows/publish-backend.yaml) and
[`publish-frontend.yaml`](.github/workflows/publish-frontend.yaml).

Depend on whichever matches your Valtimo version, e.g.:

```kotlin
// Valtimo 13.x
implementation("com.ritense.valtimoplugins:token-exchange-authentication:0.0.1")
// Valtimo 12.x
implementation("com.ritense.valtimoplugins:token-exchange-authentication:0.0.1-V12")
```

```json
// Valtimo 13.x
{ "dependencies": { "@valtimo-plugins/token-exchange-authentication": "0.0.1" } }
// Valtimo 12.x
{ "dependencies": { "@valtimo-plugins/token-exchange-authentication": "0.0.1-V12" } }
```

## Documentation

- [Getting Started](documentation/getting-started.md) — setup and development instructions
- [Example Application](documentation/example-application.md) — running the example app locally
- [Plugin](documentation/plugin.md) — description, configuration and usage of this plugin
- [Release notes](documentation/release-notes.md) — versiegeschiedenis en wijzigingen

## Contact

-- Ayub Abdulkader (Ritense)

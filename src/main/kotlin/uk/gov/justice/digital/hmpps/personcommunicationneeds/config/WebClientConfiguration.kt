package uk.gov.justice.digital.hmpps.personcommunicationneeds.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.Builder
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.ReferenceDataClient
import uk.gov.justice.hmpps.kotlin.auth.authorisedWebClient
import uk.gov.justice.hmpps.kotlin.auth.healthWebClient
import java.time.Duration

@Configuration
class WebClientConfiguration(
  @param:Value("\${hmpps-auth.url}") val hmppsAuthBaseUri: String,
  @param:Value("\${hmpps-auth.health-timeout:20s}") private val hmppsAuthHealthTimeout: Duration,

  @param:Value("\${prison-api.base-url}") private val prisonApiBaseUri: String,
  @param:Value("\${prison-api.timeout:30s}") private val prisonApiTimeout: Duration,
  @param:Value("\${prison-api.health-timeout:20s}") private val prisonApiHealthTimeout: Duration,
) {
  @Bean
  fun hmppsAuthHealthWebClient(builder: Builder): WebClient = builder.healthWebClient(hmppsAuthBaseUri, hmppsAuthHealthTimeout)

  @Bean
  fun prisonApiHealthWebClient(builder: Builder): WebClient = builder.healthWebClient(prisonApiBaseUri, prisonApiHealthTimeout)

  @Bean
  @RequestScope
  fun prisonApiWebClient(
    clientRegistrationRepository: ClientRegistrationRepository,
    oAuth2AuthorizedClientService: OAuth2AuthorizedClientService,
    builder: WebClient.Builder,
  ): WebClient = builder.authorisedWebClient(
    authorizedClientManagerUserEnhanced(clientRegistrationRepository, oAuth2AuthorizedClientService),
    "hmpps-person-communication-needs-api",
    prisonApiBaseUri,
    prisonApiTimeout,
  )

  @Bean
  @DependsOn("prisonApiWebClient")
  fun prisonApiClient(prisonApiWebClient: WebClient): PrisonApiClient {
    val factory =
      HttpServiceProxyFactory.builderFor(WebClientAdapter.create(prisonApiWebClient)).build()
    val client = factory.createClient(PrisonApiClient::class.java)

    return client
  }

  @Bean
  @DependsOn("prisonApiWebClient")
  fun referenceDataClient(prisonApiWebClient: WebClient): ReferenceDataClient {
    val factory =
      HttpServiceProxyFactory.builderFor(WebClientAdapter.create(prisonApiWebClient)).build()
    val client = factory.createClient(ReferenceDataClient::class.java)

    return client
  }

  private fun authorizedClientManagerUserEnhanced(clients: ClientRegistrationRepository?, clientService: OAuth2AuthorizedClientService): OAuth2AuthorizedClientManager {
    val manager = AuthorizedClientServiceOAuth2AuthorizedClientManager(clients, clientService)

    val defaultClientCredentialsTokenResponseClient = DefaultClientCredentialsTokenResponseClient()
    val authentication = SecurityContextHolder.getContext().authentication
    defaultClientCredentialsTokenResponseClient.setRequestEntityConverter { grantRequest: OAuth2ClientCredentialsGrantRequest ->
      val converter = UserEnhancedOAuth2ClientCredentialGrantRequestConverter()
      converter.enhanceWithUsername(grantRequest, authentication.name)
    }

    val authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
      .clientCredentials { clientCredentialsGrantBuilder: OAuth2AuthorizedClientProviderBuilder.ClientCredentialsGrantBuilder ->
        clientCredentialsGrantBuilder.accessTokenResponseClient(
          defaultClientCredentialsTokenResponseClient,
        )
      }
      .build()

    manager.setAuthorizedClientProvider(authorizedClientProvider)
    return manager
  }
}

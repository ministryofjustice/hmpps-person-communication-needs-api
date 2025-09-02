package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.personcommunicationneeds.health.HealthPingCheck
import uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.wiremock.HmppsAuthApiExtension

class LanguageInformationResourceTest : IntegrationTestBase() {
  @Autowired
  private lateinit var hmppsAuth: HealthPingCheck

  @DisplayName("PUT v1/language-preferences")
  @Nested
  inner class UpdateLanguagePreferences {

    @Nested
    inner class Security {
      @Test
      fun `access forbidden when no authority`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/language-preferences")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(UPDATE_LANGUAGE_PREFERENCES_JSON)
          .exchange()
          .expectStatus().isUnauthorized
      }

      @Test
      fun `access forbidden with wrong role`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/language-preferences")
          .contentType(MediaType.APPLICATION_JSON)
          .headers(setAuthorisation(roles = listOf("ROLE_IS_WRONG")))
          .bodyValue(UPDATE_LANGUAGE_PREFERENCES_JSON)
          .exchange()
          .expectStatus().isForbidden
      }
    }

    @Nested
    inner class HappyPath {

      @Test
      fun `update language preferences`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/language-preferences")
          .contentType(MediaType.APPLICATION_JSON)
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .bodyValue(UPDATE_LANGUAGE_PREFERENCES_JSON)
          .exchange()
          .expectStatus().isNoContent
      }
    }

    @Nested
    inner class NotFound {

      @Test
      fun `handles a 404 not found response from downstream api`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER_NOT_FOUND/language-preferences")
          .contentType(MediaType.APPLICATION_JSON)
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .bodyValue(UPDATE_LANGUAGE_PREFERENCES_JSON)
          .exchange()
          .expectStatus().isNotFound
      }
    }
  }

  @DisplayName("PUT v1/secondary-language")
  @Nested
  inner class UpdateSecondaryLanguage {

    @Nested
    inner class Security {
      @Test
      fun `access forbidden when no authority`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/secondary-language")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(UPDATE_SECONDARY_LANGUAGE_JSON)
          .exchange()
          .expectStatus().isUnauthorized
      }

      @Test
      fun `access forbidden with wrong role`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/secondary-language")
          .contentType(MediaType.APPLICATION_JSON)
          .headers(setAuthorisation(roles = listOf("ROLE_IS_WRONG")))
          .bodyValue(UPDATE_SECONDARY_LANGUAGE_JSON)
          .exchange()
          .expectStatus().isForbidden
      }
    }

    @Nested
    inner class HappyPath {

      @Test
      fun `update language preferences`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/secondary-language")
          .contentType(MediaType.APPLICATION_JSON)
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .bodyValue(UPDATE_SECONDARY_LANGUAGE_JSON)
          .exchange()
          .expectStatus().isNoContent
      }
    }

    @Nested
    inner class NotFound {

      @Test
      fun `handles a 404 not found response from downstream api`() {
        webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER_NOT_FOUND/secondary-language")
          .contentType(MediaType.APPLICATION_JSON)
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .bodyValue(UPDATE_SECONDARY_LANGUAGE_JSON)
          .exchange()
          .expectStatus().isNotFound
      }
    }
  }

  @DisplayName("DELETE v1/secondary-language/:language")
  @Nested
  inner class DeleteSecondaryLanguage {

    @Nested
    inner class Security {
      @Test
      fun `access forbidden when no authority`() {
        webTestClient.delete().uri("/v1/prisoner/$PRISONER_NUMBER/secondary-language/ENG")
          .exchange()
          .expectStatus().isUnauthorized
      }

      @Test
      fun `access forbidden with wrong role`() {
        webTestClient.delete().uri("/v1/prisoner/$PRISONER_NUMBER/secondary-language/ENG")
          .headers(setAuthorisation(roles = listOf("ROLE_IS_WRONG")))
          .exchange()
          .expectStatus().isForbidden
      }
    }

    @Nested
    inner class HappyPath {

      @Test
      fun `update language preferences`() {
        webTestClient.delete().uri("/v1/prisoner/$PRISONER_NUMBER/secondary-language/ENG")
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .exchange()
          .expectStatus().isNoContent
      }
    }

    @Nested
    inner class NotFound {

      @Test
      fun `handles a 404 not found response from downstream api`() {
        webTestClient.delete().uri("/v1/prisoner/$PRISONER_NUMBER_NOT_FOUND/secondary-language/ENG")
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .exchange()
          .expectStatus().isNotFound
      }
    }


  }

  @Nested
  inner class PassUsernameInContextToPrisonApi {

    @BeforeEach
    fun setup() {
      HmppsAuthApiExtension.hmppsAuth.resetAll()
    }

    @Test
    fun `username is added to the oauth token and cached per user when updating language preferences`() {
      val listOfTestUsers = listOf("user1", "user2", "user3", "user4")
      val numberOfRepeatRequestsPerUser = 2

      for (i in 1..numberOfRepeatRequestsPerUser) {
        for (user in listOfTestUsers) {
          // The HMPPS Auth Token Endpoint stub will only match a request containing the provided
          // username in the request body.
          HmppsAuthApiExtension.hmppsAuth.stubUsernameEnhancedGrantToken(user)

          webTestClient.put().uri("/v1/prisoner/$PRISONER_NUMBER/language-preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(setAuthorisation(username = user, roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
            .bodyValue(UPDATE_LANGUAGE_PREFERENCES_JSON)
            .exchange()
            .expectStatus().isNoContent
        }
      }

      // There should be one request to the token endpoint for each unique user.
      HmppsAuthApiExtension.hmppsAuth.assertNumberStubGrantTokenCalls(listOfTestUsers.size)
    }
  }

  private companion object {
    const val PRISONER_NUMBER = "A1234AA"
    const val PRISONER_NUMBER_NOT_FOUND = "NOTFOUND"

    val UPDATE_LANGUAGE_PREFERENCES_JSON = """
      {
          "preferredSpokenLanguageCode": "SPA",
          "preferredWrittenLanguageCode": "ITA",
          "interpreterRequired": true
      }
    """.trimIndent()

    val UPDATE_SECONDARY_LANGUAGE_JSON = """
      {
          "language": "ITA",
          "canRead": true,
          "canWrite": true,
          "canSpeak": false
      }
    """.trimIndent()
  }
}

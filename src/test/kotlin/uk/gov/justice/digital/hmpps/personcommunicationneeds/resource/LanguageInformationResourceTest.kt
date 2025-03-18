package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.IntegrationTestBase

class LanguageInformationResourceTest : IntegrationTestBase() {
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

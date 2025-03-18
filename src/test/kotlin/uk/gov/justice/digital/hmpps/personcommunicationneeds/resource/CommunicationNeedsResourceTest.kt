package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.LanguagePreferencesDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.PersonCommunicationNeedsDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataValue
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.SecondaryLanguageDto

class CommunicationNeedsResourceTest : IntegrationTestBase() {
  @DisplayName("GET /v1/prisoner/:prisonerNumber/communication-needs")
  @Nested
  inner class GetCommunicationNeeds {

    @Nested
    inner class Security {
      @Test
      fun `access forbidden when no authority`() {
        webTestClient.get().uri("/v1/prisoner/$PRISONER_NUMBER/communication-needs")
          .exchange()
          .expectStatus().isUnauthorized
      }

      @Test
      fun `access forbidden with wrong role`() {
        webTestClient.get().uri("/v1/prisoner/$PRISONER_NUMBER/communication-needs")
          .headers(setAuthorisation(roles = listOf("ROLE_IS_WRONG")))
          .exchange()
          .expectStatus().isForbidden
      }
    }

    @Nested
    inner class HappyPath {

      @Test
      fun `can get communication needs for prisonerNumber`() {
        val response =
          webTestClient.get().uri("/v1/prisoner/$PRISONER_NUMBER/communication-needs")
            .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
            .exchange()
            .expectStatus().isOk
            .expectBody(PersonCommunicationNeedsDto::class.java)
            .returnResult().responseBody

        assertThat(response).isEqualTo(
          PersonCommunicationNeedsDto(
            prisonerNumber = "A1234AA",
            languagePreferences = LanguagePreferencesDto(
              preferredSpokenLanguage = ReferenceDataValue(id = "LANG_SPA", code = "SPA", description = "Spanish; Castilian"),
              preferredWrittenLanguage = ReferenceDataValue(id = "LANG_ITA", code = "ITA", description = "Italian"),
              interpreterRequired = true,
            ),
            secondaryLanguages = listOf(
              SecondaryLanguageDto(
                language = ReferenceDataValue(id = "LANG_ITA", code = "ITA", description = "Italian"),
                canRead = true,
                canWrite = true,
                canSpeak = false,
              ),
            ),
          ),
        )
      }
    }

    @Nested
    inner class NotFound {

      @Test
      fun `handles a 404 not found response from downstream api`() {
        webTestClient.get().uri("/v1/prisoner/$PRISONER_NUMBER_NOT_FOUND/communication-needs")
          .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
          .exchange()
          .expectStatus().isNotFound
      }
    }
  }

  private companion object {
    const val PRISONER_NUMBER = "A1234AA"
    const val PRISONER_NUMBER_NOT_FOUND = "NOTFOUND"
  }
}

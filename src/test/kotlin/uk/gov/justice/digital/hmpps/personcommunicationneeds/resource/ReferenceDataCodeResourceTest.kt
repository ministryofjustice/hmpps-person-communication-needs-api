package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataCodeDto

class ReferenceDataCodeResourceTest : IntegrationTestBase() {

  @DisplayName("GET /reference-data/domains/{domain}/codes")
  @Nested
  inner class GetReferenceDataCodes {

    @Nested
    inner class Security {
      @Test
      fun `access forbidden when no authority`() {
        webTestClient.get().uri("/reference-data/domains/communication-needs/codes")
          .exchange()
          .expectStatus().isUnauthorized
      }

      @Test
      fun `access forbidden with wrong role`() {
        webTestClient.get().uri("/reference-data/domains/communication-needs/codes")
          .headers(setAuthorisation(roles = listOf("ROLE_IS_WRONG")))
          .exchange()
          .expectStatus().isForbidden
      }
    }

    @Nested
    inner class HappyPath {

      @Test
      fun `can get reference data codes`() {
        val response =
          webTestClient.get().uri("/reference-data/domains/LANG/codes")
            .headers(setAuthorisation(roles = listOf("ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW", "ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO")))
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ReferenceDataCodeDto::class.java)
            .returnResult().responseBody

        assertThat(response).isEqualTo(
          listOf(
            ReferenceDataCodeDto("LANG_ENG", "ENG", "English", 99, true),
            ReferenceDataCodeDto("LANG_ITA", "ITA", "Italian", 99, true),
          ),
        )
      }
    }
  }
}

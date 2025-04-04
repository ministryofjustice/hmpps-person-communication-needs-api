package uk.gov.justice.digital.hmpps.personcommunicationneeds.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response.CorePersonCommunicationNeeds
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response.CorePersonLanguagePreferences
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response.CorePersonSecondaryLanguage
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response.ReferenceCode
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.LanguagePreferencesDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.PersonCommunicationNeedsDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataValue
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.SecondaryLanguageDto

@ExtendWith(MockitoExtension::class)
class CommunicationNeedsServiceTest {

  @Mock
  lateinit var prisonApiClient: PrisonApiClient

  @InjectMocks
  lateinit var underTest: CommunicationNeedsService

  @AfterEach
  fun afterEach() {
    reset(prisonApiClient)
  }

  @Nested
  inner class GetPersonCommunicationNeedsByPrisonerNumber {
    private val prisonerNumber = "A1234AA"
    private val corePersonCommunicationNeeds = CorePersonCommunicationNeeds(
      prisonerNumber = prisonerNumber,
      languagePreferences = CorePersonLanguagePreferences(
        preferredSpokenLanguage = ReferenceCode("LANG", "ENG", "English", true, 99),
        preferredWrittenLanguage = ReferenceCode("LANG", "ITA", "Italian", true, 99),
        interpreterRequired = true,
      ),
      secondaryLanguages = listOf(
        CorePersonSecondaryLanguage(
          language = ReferenceCode("LANG", "ITA", "Italian", true, 99),
          canSpeak = false,
          canRead = true,
          canWrite = true,
        ),
      ),
    )
    private val communicationNeedsResponse = PersonCommunicationNeedsDto(
      prisonerNumber = prisonerNumber,
      languagePreferences = LanguagePreferencesDto(
        preferredSpokenLanguage = ReferenceDataValue("LANG_ENG", "ENG", "English"),
        preferredWrittenLanguage = ReferenceDataValue("LANG_ITA", "ITA", "Italian"),
        interpreterRequired = true,
      ),
      secondaryLanguages = listOf(
        SecondaryLanguageDto(
          language = ReferenceDataValue("LANG_ITA", "ITA", "Italian"),
          canSpeak = false,
          canRead = true,
          canWrite = true,
        ),
      ),
    )

    @BeforeEach
    fun beforeEach() {
      whenever(prisonApiClient.getCommunicationNeeds(prisonerNumber))
        .thenReturn(ResponseEntity.ok(corePersonCommunicationNeeds))
    }

    @Test
    fun `can retrieve communication needs`() {
      val response = underTest.getPersonCommunicationNeedsByPrisonerNumber(prisonerNumber)
      assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(response.body).isEqualTo(communicationNeedsResponse)
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [400, 401, 403, 404, 422, 500])
    fun `propagates non-2xx status codes`(status: Int) {
      whenever(prisonApiClient.getCommunicationNeeds(prisonerNumber)).thenReturn(
        ResponseEntity.status(status).build(),
      )

      val response = underTest.getPersonCommunicationNeedsByPrisonerNumber(prisonerNumber)
      assertThat(response.statusCode.value()).isEqualTo(status)
    }
  }
}

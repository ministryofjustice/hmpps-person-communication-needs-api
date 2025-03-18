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
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.request.LanguagePreferencesRequest
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.request.SecondaryLanguageRequest

@ExtendWith(MockitoExtension::class)
class LanguageInformationServiceTest {

  @Mock
  lateinit var prisonApiClient: PrisonApiClient

  @InjectMocks
  lateinit var underTest: LanguageInformationService

  @AfterEach
  fun afterEach() {
    reset(prisonApiClient)
  }

  @Nested
  inner class CreateOrUpdateLanguagePreferencesByPrisonerNumber {
    private val prisonerNumber = "A1234AA"
    private val languagePreferencesRequest = LanguagePreferencesRequest(
      preferredSpokenLanguageCode = "ENG",
      preferredWrittenLanguageCode = "ITA",
      interpreterRequired = true,
    )

    @BeforeEach
    fun beforeEach() {
      whenever(prisonApiClient.updateLanguagePreferences(prisonerNumber, languagePreferencesRequest.toCorePersonLanguagePreferencesRequest()))
        .thenReturn(ResponseEntity.noContent().build())
    }

    @Test
    fun `can create or update language preferences`() {
      val response = underTest.createOrUpdateLanguagePreferencesByPrisonerNumber(prisonerNumber, languagePreferencesRequest)
      assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [400, 401, 403, 404, 422, 500])
    fun `propagates non-2xx status codes`(status: Int) {
      whenever(prisonApiClient.updateLanguagePreferences(prisonerNumber, languagePreferencesRequest.toCorePersonLanguagePreferencesRequest()))
        .thenReturn(ResponseEntity.status(status).build())

      val response = underTest.createOrUpdateLanguagePreferencesByPrisonerNumber(prisonerNumber, languagePreferencesRequest)
      assertThat(response.statusCode.value()).isEqualTo(status)
    }
  }

  @Nested
  inner class AddOrUpdateSecondaryLanguageByPrisonerNumber {
    private val prisonerNumber = "A1234AA"
    private val secondaryLanguageRequest = SecondaryLanguageRequest(
      language = "ITA",
      canSpeak = false,
      canRead = true,
      canWrite = true,
    )

    @BeforeEach
    fun beforeEach() {
      whenever(prisonApiClient.addOrUpdateSecondaryLanguage(prisonerNumber, secondaryLanguageRequest.toCorePersonSecondaryLanguageRequest()))
        .thenReturn(ResponseEntity.noContent().build())
    }

    @Test
    fun `can add or update secondary language`() {
      val response = underTest.addOrUpdateSecondaryLanguageByPrisonerNumber(prisonerNumber, secondaryLanguageRequest)
      assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [400, 401, 403, 404, 422, 500])
    fun `propagates non-2xx status codes`(status: Int) {
      whenever(prisonApiClient.addOrUpdateSecondaryLanguage(prisonerNumber, secondaryLanguageRequest.toCorePersonSecondaryLanguageRequest()))
        .thenReturn(ResponseEntity.status(status).build())

      val response = underTest.addOrUpdateSecondaryLanguageByPrisonerNumber(prisonerNumber, secondaryLanguageRequest)
      assertThat(response.statusCode.value()).isEqualTo(status)
    }
  }

  @Nested
  inner class DeleteSecondaryLanguageByLanguageCodeAndPrisonerNumber {
    private val prisonerNumber = "A1234AA"
    private val languageCode = "ITA"

    @BeforeEach
    fun beforeEach() {
      whenever(prisonApiClient.deleteSecondaryLanguage(prisonerNumber, languageCode))
        .thenReturn(ResponseEntity.noContent().build())
    }

    @Test
    fun `can delete secondary language`() {
      val response = underTest.deleteSecondaryLanguageByLanguageCodeAndPrisonerNumber(prisonerNumber, languageCode)
      assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [400, 401, 403, 404, 422, 500])
    fun `propagates non-2xx status codes`(status: Int) {
      whenever(prisonApiClient.deleteSecondaryLanguage(prisonerNumber, languageCode))
        .thenReturn(ResponseEntity.status(status).build())

      val response = underTest.deleteSecondaryLanguageByLanguageCodeAndPrisonerNumber(prisonerNumber, languageCode)
      assertThat(response.statusCode.value()).isEqualTo(status)
    }
  }
}

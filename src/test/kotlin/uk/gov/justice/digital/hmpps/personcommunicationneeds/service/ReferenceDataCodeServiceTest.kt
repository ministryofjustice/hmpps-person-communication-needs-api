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
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.ReferenceDataClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response.ReferenceDataCode
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataCodeDto

@ExtendWith(MockitoExtension::class)
class ReferenceDataCodeServiceTest {

  @Mock
  lateinit var referenceDataClient: ReferenceDataClient

  @InjectMocks
  lateinit var underTest: ReferenceDataCodeService

  @AfterEach
  fun afterEach() {
    reset(referenceDataClient)
  }

  @Nested
  inner class GetReferenceDataCodes {
    private val domain = "LANG"
    private val referenceDataCodes = listOf(
      ReferenceDataCode("LANG", "ENG", "English", true, 1),
      ReferenceDataCode("LANG", "ITA", "Italian", true, 2),
    )
    private val referenceDataCodeDtos = listOf(
      ReferenceDataCodeDto("LANG_ENG", "ENG", "English", 1, true),
      ReferenceDataCodeDto("LANG_ITA", "ITA", "Italian", 2, true),
    )

    @BeforeEach
    fun beforeEach() {
      whenever(referenceDataClient.getReferenceDataByDomain(domain))
        .thenReturn(ResponseEntity.ok(referenceDataCodes))
    }

    @Test
    fun `can retrieve reference data codes`() {
      val response = underTest.getReferenceDataCodes(domain)
      assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(response.body).isEqualTo(referenceDataCodeDtos)
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [400, 401, 403, 404, 422, 500])
    fun `propagates non-2xx status codes`(status: Int) {
      whenever(referenceDataClient.getReferenceDataByDomain(domain)).thenReturn(
        ResponseEntity.status(status).build(),
      )

      val response = underTest.getReferenceDataCodes(domain)
      assertThat(response.statusCode.value()).isEqualTo(status)
    }
  }
}

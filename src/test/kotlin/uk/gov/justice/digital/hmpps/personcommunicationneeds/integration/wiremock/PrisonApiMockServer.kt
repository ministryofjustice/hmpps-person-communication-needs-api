package uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.put
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.http.HttpStatus

internal const val PRISONER_NUMBER = "A1234AA"
internal const val PRISONER_NUMBER_THROW_EXCEPTION = "THROW"
internal const val PRISONER_NUMBER_NOT_FOUND = "NOTFOUND"
internal const val PRISON_API_NOT_FOUND_RESPONSE = """
              {
                "status": 404,
                "errorCode": "12345",
                "userMessage": "Prisoner not found",
                "developerMessage": "Prisoner not found"
              }
            """
internal const val PRISON_API_REFERENCE_CODES = """
              [
                {
                  "domain": "TEST",
                  "code": "ONE",
                  "description": "Code One",
                  "activeFlag": "Y",
                  "listSeq": 99,
                  "subCodes": []
                },
                {
                  "domain": "TEST",
                  "code": "TWO",
                  "description": "Code Two",
                  "activeFlag": "Y",
                  "listSeq": 99,
                  "subCodes": []
                }
              ]
            """

class PrisonApiMockServer : WireMockServer(8082) {
  fun stubHealthPing(status: Int) {
    stubFor(
      get("/health/ping").willReturn(
        aResponse().withHeader("Content-Type", "application/json")
          .withBody("""{"status":"${if (status == 200) "UP" else "DOWN"}"}""").withStatus(status),
      ),
    )
  }

  fun stubReferenceDataCodes(domain: String = "TEST", body: String = PRISON_API_REFERENCE_CODES) {
    stubFor(
      get(urlPathMatching("/api/reference-domains/domains/$domain/all-codes")).willReturn(
        aResponse().withHeader("Content-Type", "application/json")
          .withStatus(HttpStatus.OK.value())
          .withBody(body),
      ),
    )
  }

  // TODO prisonApiClient stubs for communication needs

  private fun stubOffenderGetEndpoint(
    endpoint: String,
    status: HttpStatus,
    prisonerNumber: String,
    body: String? = null,
  ) {
    stubFor(
      get(urlPathMatching("/api/offenders/$prisonerNumber/$endpoint")).willReturn(
        aResponse().withHeader("Content-Type", "application/json")
          .withStatus(status.value())
          .withBody(body),
      ),
    )
  }

  private fun stubOffenderPutEndpoint(
    endpoint: String,
    status: HttpStatus,
    prisonerNumber: String,
    body: String? = null,
  ) {
    stubFor(
      put(urlPathMatching("/api/offenders/$prisonerNumber/$endpoint")).willReturn(
        aResponse().withHeader("Content-Type", "application/json")
          .withStatus(status.value())
          .withBody(body),
      ),
    )
  }

  private fun stubOffenderPostEndpoint(
    endpoint: String,
    status: HttpStatus,
    prisonerNumber: String,
    body: String? = null,
  ) {
    stubFor(
      post(urlPathMatching("/api/offenders/$prisonerNumber/$endpoint")).willReturn(
        aResponse().withHeader("Content-Type", "application/json")
          .withStatus(status.value())
          .withBody(body),
      ),
    )
  }
}

class PrisonApiExtension :
  BeforeAllCallback,
  AfterAllCallback,
  BeforeEachCallback {
  companion object {
    @JvmField
    val prisonApi = PrisonApiMockServer()
  }

  override fun beforeAll(context: ExtensionContext): Unit = prisonApi.start()
  override fun beforeEach(context: ExtensionContext) {
    prisonApi.resetAll()

    prisonApi.stubReferenceDataCodes()

    // TODO prisonApi.stubs
  }

  override fun afterAll(context: ExtensionContext): Unit = prisonApi.stop()
}

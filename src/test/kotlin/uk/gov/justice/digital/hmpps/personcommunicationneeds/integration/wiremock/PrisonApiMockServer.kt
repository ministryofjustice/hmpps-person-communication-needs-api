package uk.gov.justice.digital.hmpps.personcommunicationneeds.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.get
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
                  "domain": "LANG",
                  "code": "ENG",
                  "description": "English",
                  "activeFlag": "Y",
                  "listSeq": 99
                },
                {
                  "domain": "LANG",
                  "code": "ITA",
                  "description": "Italian",
                  "activeFlag": "Y",
                  "listSeq": 99
                }
              ]
            """

internal const val PRISON_API_COMMUNICATION_NEEDS = """
              {
                  "prisonerNumber": "$PRISONER_NUMBER",
                  "languagePreferences": {
                      "preferredSpokenLanguage": {
                          "domain": "LANG",
                          "code": "SPA",
                          "description": "Spanish; Castilian",
                          "listSequence": 4,
                          "active": true,
                          "primaryKey": {
                              "domain": "LANG",
                              "code": "SPA"
                          }
                      },
                      "preferredWrittenLanguage": {
                          "domain": "LANG",
                          "code": "ITA",
                          "description": "Italian",
                          "listSequence": 4,
                          "active": true,
                          "primaryKey": {
                              "domain": "LANG",
                              "code": "ITA"
                          }
                      },
                      "interpreterRequired": true
                  },
                  "secondaryLanguages": [
                      {
                          "language": {
                              "domain": "LANG",
                              "code": "ITA",
                              "description": "Italian",
                              "listSequence": 4,
                              "active": true,
                              "primaryKey": {
                                  "domain": "LANG",
                                  "code": "ITA"
                              }
                          },
                          "canRead": true,
                          "canWrite": true,
                          "canSpeak": false
                      }
                  ]
              }
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

  fun stubReferenceDataCodes(domain: String = "LANG", body: String = PRISON_API_REFERENCE_CODES) {
    stubFor(
      get(urlPathMatching("/api/reference-domains/domains/$domain/all-codes")).willReturn(
        aResponse().withHeader("Content-Type", "application/json")
          .withStatus(HttpStatus.OK.value())
          .withBody(body),
      ),
    )
  }

  fun stubGetCommunicationNeeds() {
    val endpoint = "core-person-record/communication-needs"
    stubOffenderGetEndpoint(endpoint, HttpStatus.OK, PRISONER_NUMBER, PRISON_API_COMMUNICATION_NEEDS.trimIndent())
    stubOffenderGetEndpoint(endpoint, HttpStatus.INTERNAL_SERVER_ERROR, PRISONER_NUMBER_THROW_EXCEPTION)
    stubOffenderGetEndpoint(
      endpoint,
      HttpStatus.NOT_FOUND,
      PRISONER_NUMBER_NOT_FOUND,
      PRISON_API_NOT_FOUND_RESPONSE.trimIndent(),
    )
  }

  fun stubUpdateLanguagePreferences() {
    val endpoint = "core-person-record/language-preferences"
    stubOffenderPutEndpoint(endpoint, HttpStatus.NO_CONTENT, PRISONER_NUMBER)
    stubOffenderPutEndpoint(endpoint, HttpStatus.INTERNAL_SERVER_ERROR, PRISONER_NUMBER_THROW_EXCEPTION)
    stubOffenderPutEndpoint(
      endpoint,
      HttpStatus.NOT_FOUND,
      PRISONER_NUMBER_NOT_FOUND,
      PRISON_API_NOT_FOUND_RESPONSE.trimIndent(),
    )
  }

  fun stubUpdateSecondaryLanguage() {
    val endpoint = "core-person-record/secondary-language"
    stubOffenderPutEndpoint(endpoint, HttpStatus.NO_CONTENT, PRISONER_NUMBER)
    stubOffenderPutEndpoint(endpoint, HttpStatus.INTERNAL_SERVER_ERROR, PRISONER_NUMBER_THROW_EXCEPTION)
    stubOffenderPutEndpoint(
      endpoint,
      HttpStatus.NOT_FOUND,
      PRISONER_NUMBER_NOT_FOUND,
      PRISON_API_NOT_FOUND_RESPONSE.trimIndent(),
    )
  }

  fun stubDeleteSecondaryLanguage() {
    val endpoint = "core-person-record/secondary-language/ENG"
    stubOffenderDeleteEndpoint(endpoint, HttpStatus.NO_CONTENT, PRISONER_NUMBER)
    stubOffenderDeleteEndpoint(endpoint, HttpStatus.INTERNAL_SERVER_ERROR, PRISONER_NUMBER_THROW_EXCEPTION)
    stubOffenderDeleteEndpoint(
      endpoint,
      HttpStatus.NOT_FOUND,
      PRISONER_NUMBER_NOT_FOUND,
      PRISON_API_NOT_FOUND_RESPONSE.trimIndent(),
    )
  }

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

  private fun stubOffenderDeleteEndpoint(
    endpoint: String,
    status: HttpStatus,
    prisonerNumber: String,
    body: String? = null,
  ) {
    stubFor(
      delete(urlPathMatching("/api/offenders/$prisonerNumber/$endpoint")).willReturn(
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

    prisonApi.stubGetCommunicationNeeds()
    prisonApi.stubUpdateLanguagePreferences()
    prisonApi.stubUpdateSecondaryLanguage()
    prisonApi.stubDeleteSecondaryLanguage()
  }

  override fun afterAll(context: ExtensionContext): Unit = prisonApi.stop()
}

package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PutExchange
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.request.CorePersonLanguagePreferencesRequest
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.request.CorePersonSecondaryLanguageRequest
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response.CorePersonCommunicationNeeds

@HttpExchange("/api")
interface PrisonApiClient {
  @GetExchange("/offenders/{offenderNo}/core-person-record/communication-needs")
  fun getCommunicationNeeds(
    @PathVariable offenderNo: String,
  ): ResponseEntity<CorePersonCommunicationNeeds>

  @PutExchange("/offenders/{offenderNo}/core-person-record/language-preferences")
  fun updateLanguagePreferences(
    @PathVariable offenderNo: String,
    @RequestBody request: CorePersonLanguagePreferencesRequest,
  ): ResponseEntity<Void>

  @PutExchange("/offenders/{offenderNo}/core-person-record/secondary-language")
  fun addOrUpdateSecondaryLanguage(
    @PathVariable offenderNo: String,
    @RequestBody request: CorePersonSecondaryLanguageRequest,
  ): ResponseEntity<Void>

  @DeleteExchange("/offenders/{offenderNo}/core-person-record/secondary-language/{languageCode}")
  fun deleteSecondaryLanguage(
    @PathVariable offenderNo: String,
    @PathVariable languageCode: String,
  ): ResponseEntity<Void>
}

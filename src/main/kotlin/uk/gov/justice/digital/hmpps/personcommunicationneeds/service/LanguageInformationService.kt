package uk.gov.justice.digital.hmpps.personcommunicationneeds.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.request.LanguagePreferencesRequest
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.request.SecondaryLanguageRequest

@Service
class LanguageInformationService(
  private val prisonApiClient: PrisonApiClient,
) {
  fun createOrUpdateLanguagePreferencesByPrisonerNumber(
    prisonerNumber: String,
    languagePreferencesRequest: LanguagePreferencesRequest,
  ): ResponseEntity<Void> = prisonApiClient.updateLanguagePreferences(
    prisonerNumber,
    languagePreferencesRequest.toCorePersonLanguagePreferencesRequest(),
  )

  fun addOrUpdateSecondaryLanguageByPrisonerNumber(
    prisonerNumber: String,
    secondaryLanguageRequest: SecondaryLanguageRequest,
  ): ResponseEntity<Void> = prisonApiClient.addOrUpdateSecondaryLanguage(
    prisonerNumber,
    secondaryLanguageRequest.toCorePersonSecondaryLanguageRequest(),
  )

  fun deleteSecondaryLanguageByLanguageCodeAndPrisonerNumber(
    prisonerNumber: String,
    languageCode: String,
  ): ResponseEntity<Void> = prisonApiClient.deleteSecondaryLanguage(prisonerNumber, languageCode)
}

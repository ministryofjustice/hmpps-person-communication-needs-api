package uk.gov.justice.digital.hmpps.personcommunicationneeds.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.LanguagePreferencesDto

@Service
class LanguageInformationService(
  private val prisonApiClient: PrisonApiClient,
) {
  fun addSecondaryLanguageByPrisonerNumber(prisonerNumber: String, languagePreferencesDto: LanguagePreferencesDto): ResponseEntity<Void> {
    // TODO Implementation here
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }

  fun createOrUpdateLanguagePreferencesByPrisonerNumber(prisonerNumber: String, languagePreferencesDto: LanguagePreferencesDto): ResponseEntity<Void> {
    // TODO Implementation here
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }

  fun deleteSecondaryLanguageByLanguageCodeAndPrisonerNumber(prisonerNumber: String, languageCode: String): ResponseEntity<Void> {
    // TODO Implementation here
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }
}

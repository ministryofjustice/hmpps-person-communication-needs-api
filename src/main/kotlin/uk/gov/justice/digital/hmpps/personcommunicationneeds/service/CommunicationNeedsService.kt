package uk.gov.justice.digital.hmpps.personcommunicationneeds.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.LanguagePreferencesDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.PersonCommunicationNeedsDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.SecondaryLanguageDto

@Service
class CommunicationNeedsService(
  private val prisonApiClient: PrisonApiClient,
) {
  fun getPersonCommunicationNeedsByPrisonerNumber(prisonerNumber: String): ResponseEntity<PersonCommunicationNeedsDto> {
    val response = prisonApiClient.getCommunicationNeeds(prisonerNumber)

    if (response.statusCode.is2xxSuccessful) {
      return ResponseEntity.ok(
        PersonCommunicationNeedsDto(
          prisonerNumber = response.body!!.prisonerNumber,
          languagePreferences = response.body!!.languagePreferences?.let {
            LanguagePreferencesDto(
              preferredSpokenLanguage = it.preferredSpokenLanguage?.toReferenceDataValue(),
              preferredWrittenLanguage = it.preferredWrittenLanguage?.toReferenceDataValue(),
              interpreterRequired = it.interpreterRequired,
            )
          },
          secondaryLanguages = response.body!!.secondaryLanguages.map {
            SecondaryLanguageDto(
              language = it.language.toReferenceDataValue(),
              canSpeak = it.canSpeak,
              canRead = it.canRead,
              canWrite = it.canWrite,
            )
          },
        ),
      )
    } else {
      return ResponseEntity.status(response.statusCode).build()
    }
  }
}

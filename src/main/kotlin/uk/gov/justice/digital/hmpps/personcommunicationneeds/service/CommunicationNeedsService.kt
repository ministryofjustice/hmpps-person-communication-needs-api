package uk.gov.justice.digital.hmpps.personcommunicationneeds.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.PersonCommunicationNeedsDto

@Service
class CommunicationNeedsService(
  private val prisonApiClient: PrisonApiClient,
) {
  fun getPersonCommunicationNeedsByPrisonerNumber(prisonerNumber: String): ResponseEntity<PersonCommunicationNeedsDto> {
    // TODO Implementation here
    return ResponseEntity.ok(PersonCommunicationNeedsDto(prisonerNumber, null, listOf()))
  }
}

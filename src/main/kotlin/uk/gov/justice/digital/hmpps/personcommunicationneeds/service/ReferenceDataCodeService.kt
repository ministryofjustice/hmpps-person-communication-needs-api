package uk.gov.justice.digital.hmpps.personcommunicationneeds.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.ReferenceDataClient
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataCodeDto

@Service
class ReferenceDataCodeService(
  private val referenceDataClient: ReferenceDataClient,
) {
  fun getReferenceDataCodes(domain: String): ResponseEntity<List<ReferenceDataCodeDto>> {
    val response = referenceDataClient.getReferenceDataByDomain(domain)

    if (response.statusCode.is2xxSuccessful) {
      val mappedResponse = response.body
        ?.map {
          ReferenceDataCodeDto(
            "${domain}_${it.code}",
            it.code,
            it.description,
            it.listSequence,
            it.active,
          )
        }
        ?.sortedBy { it.listSequence }
      return ResponseEntity.ok(mappedResponse)
    } else {
      return ResponseEntity.status(response.statusCode).build()
    }
  }
}

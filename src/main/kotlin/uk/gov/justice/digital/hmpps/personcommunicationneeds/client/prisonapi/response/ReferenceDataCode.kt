package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response

import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataValue

data class ReferenceDataCode(
  val domain: String,
  val code: String,
  val description: String,
  val activeFlag: String,
  val listSeq: Int,
  val parentCode: String? = null,
  val parentDomain: String? = null,
) {
  fun toReferenceDataValue(): ReferenceDataValue = ReferenceDataValue(
    id = "${domain}_$code",
    code = code,
    description = description,
  )
}

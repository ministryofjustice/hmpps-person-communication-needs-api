package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response

import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataValue

data class ReferenceCode(
  val domain: String,
  val code: String,
  val description: String,
  val active: Boolean,
  val listSequence: Int,
) {
  fun toReferenceDataValue(): ReferenceDataValue = ReferenceDataValue(
    id = "${domain}_$code",
    code = code,
    description = description,
  )
}

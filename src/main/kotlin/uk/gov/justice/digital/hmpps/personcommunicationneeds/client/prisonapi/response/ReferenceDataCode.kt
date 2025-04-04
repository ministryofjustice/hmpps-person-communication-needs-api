package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response

data class ReferenceDataCode(
  val domain: String,
  val code: String,
  val description: String,
  val activeFlag: String,
  val listSeq: Int,
  val parentCode: String? = null,
  val parentDomain: String? = null,
)

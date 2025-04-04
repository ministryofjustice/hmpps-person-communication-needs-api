package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Secondary language information")
data class CorePersonSecondaryLanguage(
  @Schema(description = "Language reference code")
  val language: ReferenceCode,

  @Schema(description = "Reading proficiency")
  val canRead: Boolean,

  @Schema(description = "Writing proficiency")
  val canWrite: Boolean,

  @Schema(description = "Speaking proficiency")
  val canSpeak: Boolean,
)

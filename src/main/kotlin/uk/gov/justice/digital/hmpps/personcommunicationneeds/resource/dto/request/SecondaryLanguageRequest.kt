package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.request.CorePersonSecondaryLanguageRequest

@Schema(description = "Secondary language request. Used to add or update secondary language information.")
data class SecondaryLanguageRequest(
  @Schema(description = "Language reference code. Uses the `LANG` reference data domain.", example = "ENG")
  val language: String,

  @Schema(description = "Reading proficiency", example = "true")
  val canRead: Boolean,

  @Schema(description = "Writing proficiency", example = "true")
  val canWrite: Boolean,

  @Schema(description = "Speaking proficiency", example = "true")
  val canSpeak: Boolean,
) {
  fun toCorePersonSecondaryLanguageRequest(): CorePersonSecondaryLanguageRequest = CorePersonSecondaryLanguageRequest(
    language = this.language,
    canRead = this.canRead,
    canWrite = this.canWrite,
    canSpeak = this.canSpeak,
  )
}

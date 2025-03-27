package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.request.CorePersonLanguagePreferencesRequest

@Schema(description = "Language Preferences Request. Used to create or update language preferences.")
data class LanguagePreferencesRequest(
  @Schema(description = "Preferred spoken language code. Uses the `LANG` reference data domain.", example = "ENG")
  val preferredSpokenLanguageCode: String? = null,

  @Schema(description = "Preferred written language code. Uses the `LANG` reference data domain.", example = "ENG")
  val preferredWrittenLanguageCode: String? = null,

  @Schema(description = "Is interpreter required", example = "true")
  val interpreterRequired: Boolean? = null,
) {
  fun toCorePersonLanguagePreferencesRequest(): CorePersonLanguagePreferencesRequest = CorePersonLanguagePreferencesRequest(
    preferredSpokenLanguageCode = this.preferredSpokenLanguageCode,
    preferredWrittenLanguageCode = this.preferredWrittenLanguageCode,
    interpreterRequired = this.interpreterRequired,
  )
}

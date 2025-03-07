package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Language Preferences")
@JsonInclude(NON_NULL)
data class LanguagePreferencesDto(
  @Schema(description = "Preferred spoken language code", example = "ENG")
  val preferredSpokenLanguageCode: String? = null,

  @Schema(description = "Preferred written language code", example = "ENG")
  val preferredWrittenLanguageCode: String? = null,

  @Schema(description = "Is interpreter required", example = "true")
  val interpreterRequired: Boolean,
)

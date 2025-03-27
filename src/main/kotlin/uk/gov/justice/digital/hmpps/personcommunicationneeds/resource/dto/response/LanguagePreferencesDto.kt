package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Language Preferences")
@JsonInclude(NON_NULL)
data class LanguagePreferencesDto(
  @Schema(description = "Preferred spoken language")
  val preferredSpokenLanguage: ReferenceDataValue? = null,

  @Schema(description = "Preferred written language")
  val preferredWrittenLanguage: ReferenceDataValue? = null,

  @Schema(description = "Is interpreter required", example = "true")
  val interpreterRequired: Boolean? = null,
)

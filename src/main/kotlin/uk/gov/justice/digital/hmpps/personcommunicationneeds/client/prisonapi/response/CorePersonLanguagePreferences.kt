package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(NON_NULL)
@Schema(description = "Core Person Record Language Preferences")
data class CorePersonLanguagePreferences(
  @Schema(description = "Preferred spoken language")
  val preferredSpokenLanguage: ReferenceDataCode? = null,

  @Schema(description = "Preferred written language")
  val preferredWrittenLanguage: ReferenceDataCode? = null,

  @Schema(description = "Is interpreter required", example = "true")
  val interpreterRequired: Boolean? = null,
)

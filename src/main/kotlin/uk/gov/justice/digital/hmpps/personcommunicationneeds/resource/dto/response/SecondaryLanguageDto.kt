package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Secondary Language")
@JsonInclude(NON_NULL)
data class SecondaryLanguageDto(
  @Schema(description = "Language")
  val language: ReferenceDataValue? = null,

  @Schema(description = "Can read", example = "true")
  val canRead: Boolean,

  @Schema(description = "Can write", example = "true")
  val canWrite: Boolean,

  @Schema(description = "Can speak", example = "true")
  val canSpeak: Boolean,
)

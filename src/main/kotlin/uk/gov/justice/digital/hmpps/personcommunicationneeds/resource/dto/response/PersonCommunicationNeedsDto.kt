package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Person Communication Needs")
@JsonInclude(NON_NULL)
data class PersonCommunicationNeedsDto(
  @Schema(description = "Prisoner number", example = "A1234BC")
  val prisonerNumber: String,

  @Schema(description = "Language preferences")
  val languagePreferences: LanguagePreferencesDto?,

  @Schema(description = "List of secondary languages")
  val secondaryLanguages: List<SecondaryLanguageDto>,
)

package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.LanguagePreferencesDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.service.LanguageInformationService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@Tag(name = "Language Information Controller V1")
@RequestMapping("/v1/prisoner/{prisonerNumber}")
class LanguageInformationResource(private val languageInformationService: LanguageInformationService) {

  @PutMapping("/language-preferences")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
    summary = "Creates or updates the language preferences data by prisoner number.",
    description = "Requires role `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW`",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Language preference information added successfully.",
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Missing required role. Requires ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "404",
        description = "Data not found",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
    ],
  )
  @PreAuthorize("hasRole('ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW')")
  fun createOrUpdateLanguagePreferencesByPrisonerNumber(
    @PathVariable prisonerNumber: String,
    @RequestBody languagePreferencesDto: LanguagePreferencesDto,
  ): ResponseEntity<Void> = languageInformationService.createOrUpdateLanguagePreferencesByPrisonerNumber(prisonerNumber, languagePreferencesDto)

  @PutMapping("/secondary-language")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
    summary = "Adds or replaces the secondary language data by prisoner number.",
    description = "Requires role `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW`",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Secondary language information added successfully",
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Missing required role. Requires ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "404",
        description = "Data not found",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
    ],
  )
  @PreAuthorize("hasRole('ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW')")
  fun addSecondaryLanguageByPrisonerNumber(
    @PathVariable prisonerNumber: String,
    @RequestBody languagePreferencesDto: LanguagePreferencesDto,
  ): ResponseEntity<Void> = languageInformationService.addSecondaryLanguageByPrisonerNumber(prisonerNumber, languagePreferencesDto)

  @DeleteMapping("/secondary-language/{languageCode}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
    summary = "Removes a secondary language by language code and prisoner number.",
    description = "Requires role `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW`",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Secondary language information deleted successfully.",
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Missing required role. Requires ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "404",
        description = "Data not found",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
    ],
  )
  @PreAuthorize("hasRole('ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW')")
  fun deleteSecondaryLanguageByLanguageCodeAndPrisonerNumber(
    @PathVariable prisonerNumber: String,
    @PathVariable languageCode: String,
  ): ResponseEntity<Void> = languageInformationService.deleteSecondaryLanguageByLanguageCodeAndPrisonerNumber(prisonerNumber, languageCode)
}

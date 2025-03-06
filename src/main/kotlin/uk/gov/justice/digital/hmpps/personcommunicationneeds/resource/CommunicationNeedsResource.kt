package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.PersonCommunicationNeedsDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.service.CommunicationNeedsService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@Tag(name = "Communication Needs Controller V1")
@RequestMapping("/v1/prisoner/{prisonerNumber}")
class CommunicationNeedsResource(private val communicationNeedsService: CommunicationNeedsService) {

  @GetMapping("/communication-needs")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Get a persons communication needs data by prisoner number.",
    description = "Requires role `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW` or `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO`",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Successful communication needs data request.",
        content = [Content(array = ArraySchema(schema = Schema(implementation = PersonCommunicationNeedsDto::class)))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorised, requires a valid Oauth2 token",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Missing required role. Requires `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW` or `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO`.",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "404",
        description = "Prisoner not found",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @PreAuthorize("hasAnyRole('ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW', 'ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO')")
  fun getPersonCommunicationNeedsByPrisonerNumber(
    @PathVariable prisonerNumber: String,
  ): ResponseEntity<PersonCommunicationNeedsDto> = communicationNeedsService.getPersonCommunicationNeedsByPrisonerNumber(prisonerNumber)
}

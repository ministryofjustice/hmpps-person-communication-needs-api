package uk.gov.justice.digital.hmpps.personcommunicationneeds.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
import uk.gov.justice.digital.hmpps.personcommunicationneeds.resource.dto.response.ReferenceDataCodeDto
import uk.gov.justice.digital.hmpps.personcommunicationneeds.service.ReferenceDataCodeService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@Tag(name = "Reference Data Controller V1", description = "Reference Data Codes for Person Communication Needs data")
@RequestMapping("/v1/reference-data/domains/{domain}/codes", produces = [MediaType.APPLICATION_JSON_VALUE])
class ReferenceDataCodeResource(
  private val referenceDataCodeService: ReferenceDataCodeService,
) {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Get all reference data codes for {domain}",
    description = "Returns the list of reference data codes within {domain}. " +
      "By default this endpoint only returns active reference data codes. " +
      "The `includeInactive` parameter can be used to return all reference data codes. " +
      "Requires role `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO` or `ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW`",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Reference data codes found",
        content = [Content(array = ArraySchema(schema = Schema(implementation = ReferenceDataCodeDto::class)))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorised, requires a valid Oauth2 token",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found, the reference data domain was not found",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @PreAuthorize("hasAnyRole('ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RO', 'ROLE_COMMUNICATION_NEEDS_API__COMMUNICATION_NEEDS_DATA__RW')")
  fun getReferenceDataCodes(
    @PathVariable domain: String,
    @Parameter(
      description = "Include inactive reference data codes. Defaults to false.",
    )
    includeInactive: Boolean = false,
  ): ResponseEntity<List<ReferenceDataCodeDto>> = referenceDataCodeService.getReferenceDataCodes(domain)
}

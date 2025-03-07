package uk.gov.justice.digital.hmpps.personcommunicationneeds.client.prisonapi

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class PrisonApiClient(@Qualifier("prisonApiWebClient") private val webClient: WebClient) {
// TODO fun getCommunicationNeeds(prisonerNumber: String) = try {
//    webClient
//      .get()
//      .uri("/api/offenders/{prisonerNumber}/smoker", prisonerNumber) // TODO
//      .retrieve()
//      .bodyToMono(PrisonerDto::class.java)
//      .block()
//  } catch (e: NotFound) {
//    null
//  } catch (e: Exception) {
//    throw DownstreamServiceException("Get prisoner request failed", e)
//  }
}

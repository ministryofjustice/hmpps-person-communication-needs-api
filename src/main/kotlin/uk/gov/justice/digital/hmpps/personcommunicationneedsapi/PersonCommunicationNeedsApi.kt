package uk.gov.justice.digital.hmpps.personcommunicationneedsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonCommunicationNeedsApi

fun main(args: Array<String>) {
  runApplication<PersonCommunicationNeedsApi>(*args)
}

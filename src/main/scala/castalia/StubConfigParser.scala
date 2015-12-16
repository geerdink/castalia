package castalia

import castalia.model.StubConfig


object StubConfigParser extends Protocol {

  def parseStubConfig(jsonFile: String): StubConfig = {
   JsonConverter.parseJson[StubConfig](jsonFile)
  }

  def parseConfigFile(configFile: String): JsonFilesConfig = {
    JsonConverter.parseJson[JsonFilesConfig](configFile)
  }

  def readAndParseStubConfigFiles(args: Array[String]): Map[Endpoint, ResponsesByRequest] = {
    // Get all json files from the config file
    val stubConfigs: Array[StubConfig] = for (
      stubs <- parseConfigFile(args(0)).stubs // iterate over all jsonFiles
    ) yield parseStubConfig(stubs)

    val stubsConfigsByEndpoint: Map[Endpoint, ResponsesByRequest] = stubConfigs.map({
      // Create an outer map by endpoint
      s => (
        s.endpoint, // Endpoint is the outer key
        s.responses.map({
          // Create an inner map by repsonse id
          r => (
            r.id, // Id is the inner key
            (r.httpStatusCode, r.response))
        }).toMap // this is the outer map value (a map of id -> responses)
        )
    }).toMap // this is the map of all stubs (a map of endpoint -> (a map of id -> responses) )

    if (stubConfigs.length == stubsConfigsByEndpoint.size) {
      stubsConfigsByEndpoint
    }
    else {
      throw new IllegalArgumentException("Duplicate endpoints have been defined")
    }

  }
}
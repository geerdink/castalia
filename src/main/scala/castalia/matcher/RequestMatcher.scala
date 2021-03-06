package castalia.matcher

import akka.http.scaladsl.model.Uri
import castalia.matcher.types.Segments

/**
  * Takes a list of endpoint matchers and determines which one matches.
  *
  * Created by Jean-Marc van Leerdam on 2016-01-09
  */
class RequestMatcher(myMatchers: List[Matcher]) {
  val uriParser = new UriParser()

  def matchRequest(uriString: String): Option[RequestMatch] = {
    val parsedUri = uriParser.parse(uriString)

    def findMatch( segments: Segments, matchers: List[Matcher]): Option[RequestMatch] = {
      if (matchers.isEmpty) return None

      val result = matchers.head.matchPath(segments)
      if (result.isDefined) return Some(new RequestMatch(uriString, parsedUri.path, result.get, parsedUri.queryParams, matchers.head.handler))

      // no match yet, look at the rest of the matchers
      return findMatch(segments, matchers.tail)
    }

    return findMatch(parsedUri.pathList, myMatchers)
  }
}

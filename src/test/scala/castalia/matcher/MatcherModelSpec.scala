package castalia.matcher

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path
import org.scalatest.{BeforeAndAfterEach, WordSpec}

/**
  * Created by Jean-Marc van Leerdam on 2016-01-10
  */
class MatcherModelSpec extends WordSpec with BeforeAndAfterEach {

  override def beforeEach() {

  }

  "MatcherModel PathUri class" should {

    "implement correct pathList method " in {
      val resList = List("a", "b", "c")
      val uri:Uri = "http://example.com/a/b/c?d=e&f-g"

      val parsedUri = new ParsedUri( uri.toString(), uri.path, uri.query().toList)

      assert( resList.equals(parsedUri.pathList))
    }

  }

  "MatcherModel Matcher class" should {
    "support {} as path parameter indication" in {
      val matcher = new Matcher(List("a", "{bparm}", "c"), "foo")

      val result = matcher.matchPath(List("a", "b", "c"))

      assert(result.get.contains(("bparm", "b")))

    }

    "support $ as path parameter indication" in {
      val matcher = new Matcher(List("a", "b", "$c"), "foo")

      val result = matcher.matchPath(List("a", "b", "cval"))

      assert(result.get.contains(("c", "cval")))


    }
    "support mixing {} and $ as path parameter" in {
      val matcher = new Matcher(List("a", "{bparm}", "$c"), "foo")

      val result = matcher.matchPath(List("a", "b", "cval"))

      assert(result.get.contains(("bparm", "b")))
      assert(result.get.contains(("c", "cval")))


    }
  }
}

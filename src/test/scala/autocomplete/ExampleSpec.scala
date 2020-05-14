package autocomplete

import org.scalatest.Inspectors
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class ExampleSpec
  extends AnyWordSpec
    with Matchers
    with Inspectors {

  val keywords = List(
    "project runway",
    "pinterest",
    "river",
    "kayak",
    "progenex",
    "progeria",
    "pg&e",
    "project free tv",
    "bank",
    "proactive",
    "progesterone",
    "press democrat",
    "priceline",
    "pandora",
    "reprobe",
    "paypal")

  val dictionary: Dictionary = Dictionary(keywords)

  "The dictionary" should {
    "autocomplete 'p' " in {
      dictionary.autocomplete("p") shouldEqual
        List(
          "pandora",
          "paypal",
          "pg&e",
          "pinterest")
    }
    "autocomplete 'pr' " in {
      dictionary.autocomplete("pr") shouldEqual
        List(
          "press democrat",
          "priceline",
          "proactive",
          "progenex")
    }
    "autocomplete 'pro' " in {
      dictionary.autocomplete("pro") shouldEqual
        List(
          "proactive",
          "progenex",
          "progeria",
          "progesterone")
    }

    "autocomplete 'prog' " in {
      dictionary.autocomplete("prog") shouldEqual
        List(
          "progenex",
          "progeria",
          "progesterone")
    }
  }
}

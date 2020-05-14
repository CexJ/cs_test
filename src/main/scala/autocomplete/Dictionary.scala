package autocomplete

import scala.annotation.tailrec

object Dictionary {
  val maxCompletions = 4
  def apply(inputs: List[String]): Dictionary = new Dictionary(inputs)
}

class Dictionary(input: List[String]) {
  import Dictionary.maxCompletions

  // we sort the input
  private val keywords: List[String] = input.sorted

  /**
   *
   * @param prefix: the string to autocomplete
   * @return the first (up to four) completion of the prefix
   */
  def autocomplete(prefix: String): List[String] = {
    keywords
      // we search the index and we take the next elements
      .slice(index(prefix), index(prefix) + maxCompletions)
      // we check they actually start with the prefix
      .filter(_.startsWith(prefix))
  }

  /**
   *
   * @param prefix the string we are searching the index
   * @return the smallest element bigger or equals to the prefix
   */
  def index(prefix: String): Int ={

    @tailrec
    def indexRec(keywords: List[String], tempIndex: Int)(implicit prefix: String): Int = {
      val middle = keywords.length/2
      val keywordsMiddle = keywords(middle)

      if(prefix <= keywords.head)
        tempIndex
      else if(prefix > keywords.last)
        tempIndex+keywords.length
      else if(prefix < keywordsMiddle)
        indexRec(keywords.slice(0, middle), tempIndex)
      else
        indexRec(keywords.slice(middle, keywords.length), tempIndex + middle)
    }

    indexRec(keywords, 0)(prefix)
  }

}

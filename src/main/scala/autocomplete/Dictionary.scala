package autocomplete

import scala.annotation.tailrec

object Dictionary {
  val maxCompletions = 4
  def apply(inputs: List[String]): Dictionary = new Dictionary(inputs)
}

class Dictionary(input: Seq[String]) {
  import Dictionary.maxCompletions

  // we sort the input
  private val sortedKeywords: Vector [String] = input.distinct.sorted.toVector

  /**
   *
   * @param prefix: the string to autocomplete
   * @return the first (up to four) completion of the prefix
   */
  def autocomplete(prefix: String): List[String] = {
    sortedKeywords
      // we search the index and we take the next elements
      .slice(index(prefix), index(prefix) + maxCompletions)
      // we check they actually start with the prefix
      .filter(_.startsWith(prefix))
      .toList
  }

  /**
   *
   * @param prefix the string we are searching the index
   * @return the smallest element bigger or equals to the prefix
   */
  def index(prefix: String): Int ={

    @tailrec
    def indexRec(sortedVector: Vector [String], tempIndex: Int)(implicit prefix: String): Int = {
      val middle = sortedVector.length/2

      if(sortedVector.isEmpty || prefix <= sortedVector.head)
        tempIndex
      else if(prefix > sortedVector.last)
        tempIndex+sortedVector.length
      else if(prefix < sortedVector(middle))
        indexRec(sortedVector.slice(0, middle), tempIndex)
      else
        indexRec(sortedVector.slice(middle, sortedVector.length), tempIndex + middle)
    }

    indexRec(sortedKeywords, 0)(prefix)
  }

}

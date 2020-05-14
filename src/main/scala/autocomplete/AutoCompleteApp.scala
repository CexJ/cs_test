package autocomplete

import scala.io.Source

object AutoCompleteApp {
  private val url = "src/main/resources/keywords"

  def main(args: Array[String]){
    val dictionary: Dictionary = loadDictionary()
    var exit = false
    do {
      Console.print("Type a string to autocomplete: ")
      val prefix = Console.in.readLine()
      dictionary.autocomplete(prefix).foreach(println(_))
      Console.print("To end type exit: ")
      exit = Console.in.readLine() != "exit"
    } while(exit)
  }

  private def loadDictionary(): Dictionary = {
    val bufferedSource = Source.fromFile(url)
    try{
      Dictionary(bufferedSource.getLines.toList)
    } finally{
      bufferedSource.close
    }
  }
}



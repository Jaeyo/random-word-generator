package org.jaeyo.random_word_generator.random_word

import java.io.File
import org.jaeyo.random_word_generator.common.Path
import scala.io.Source
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran
import kr.co.shineware.util.common.model.Pair
import scala.util.Random
import java.io.PrintWriter

object RandomWordPool {
  protected var randomWords = Source.fromFile(randomWordFile).getLines.toList
  
  protected def randomWordFile = {
    val file = new File(Path.packagePath, "random-word.txt")
    if(file.exists == false)
      file.createNewFile()
    file
  }
  
  def collectMoreWords = {
    val browser = JsoupBrowser()
    val doc = browser.get("https://namu.wiki/random")
    val collectedWords = extractNoun(doc.body.text)
    randomWords = (randomWords ++ collectedWords).distinct
    makeSnapshot
  }
  
  def collectedWordCount = randomWords.length
  
  def randomWord = {
    if(randomWords.length == 0) ""
    else randomWords(Random.nextInt(randomWords.length - 1))
  }
  
  protected def makeSnapshot = {
    val output = new PrintWriter(randomWordFile)
    randomWords foreach {
      (word: String) => {
        output.println(word)
      }
    }
    output.flush
    output.close
  }
  
  protected def extractNoun(src: String) = {
    val modelPath = new File(Path.packagePath, "models-full").getAbsolutePath
    val komoran = new Komoran(modelPath)
    komoran.analyze(src).toArray.toList flatMap {
      case wordList: java.util.ArrayList[Object] => {
        wordList.toArray.toList.map({
          case wordPair: Pair[String, String] => {
            wordPair.getSecond match {
              case "NNG" => wordPair.getFirst
              case _ => ""
            }
          }
        })
        .filter(_.equals("") == false)
        .filter(_.length > 1)
      }
    }
  }
}
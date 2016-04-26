package org.jaeyo.random_word_generator.random_word

import java.io.File
import java.io.PrintWriter

import scala.io.Source
import scala.util.Random

import org.jaeyo.random_word_generator.common.Path

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran
import kr.co.shineware.util.common.model.Pair
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.apache.log4j.Logger
import net.ruippeixotog.scalascraper.model.Document

object RandomWordPool {
  private val logger = Logger.getLogger(this.getClass)
  protected var randomWords = Source.fromFile(randomWordFile).getLines.toList
  
  protected def randomWordFile = {
    val file = new File(Path.packagePath, "random-word.txt")
    if(file.exists == false)
      file.createNewFile()
    file
  }
  
  def collectMoreWords = {
    randomWords = (randomWords ++ extractNoun(newDocumentText)).distinct
    makeSnapshot
  }
  
  def collectedWordCount = randomWords.length
  
  def randomWord(count: Int) = {
    (1 to count) map {
      (n: Int) => {
        if(randomWords.length == 0) ""
        else randomWords(Random.nextInt(randomWords.length - 1))
      }
    }
  }
  
  protected def newDocumentText = 
    JsoupBrowser().get("https://namu.wiki/random").body.text
  
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
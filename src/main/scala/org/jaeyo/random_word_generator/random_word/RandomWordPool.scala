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
import org.jaeyo.random_word_generator.random_word.anayzer.KoreanAnalyzer

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
    val collectedWords = KoreanAnalyzer.newInstance.extractNoun(newDocumentText)
    randomWords = (randomWords ++ collectedWords).distinct
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
}
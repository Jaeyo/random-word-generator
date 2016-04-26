package org.jaeyo.random_word_generator.random_word.anayzer

import java.io.File
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran
import kr.co.shineware.util.common.model.Pair
import org.jaeyo.random_word_generator.common.Path

abstract class KoreanAnalyzer {
  def extractNoun(src: String): List[String]
}

class KomoranKoreanAnayzer extends KoreanAnalyzer {
	private val modelPath = new File(Path.packagePath, "models-full").getAbsolutePath
	private val komoran = new Komoran(modelPath)
  
  def extractNoun(src: String): List[String] = {
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

object KoreanAnalyzer {
  def newInstance: KoreanAnalyzer = new KomoranKoreanAnayzer
}
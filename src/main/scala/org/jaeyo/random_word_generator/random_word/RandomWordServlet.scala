package org.jaeyo.random_word_generator.random_word

import javax.servlet.http.HttpServlet
import org.apache.log4j.Logger
import com.igloosec.scripter.util.UriParser
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import scala.util.parsing.json.JSONObject
import javax.servlet.http.HttpServletRequestWrapper

class RandomWordServlet extends HttpServlet {
  private val logger = Logger.getLogger(classOf[RandomWordServlet])
  
  protected val uriParsers4GET = 
    new UriParser()
    .mapping("/random-word/count", collectedWordCount)
    .mapping("/random-word", randomWord)
    .onMatchingFailed((req: HttpServletRequest, resp: HttpServletResponse) => {
    	JSONObject(Map("success" -> 0, "errmsg" -> "invalid path uri")).toString
    })
    
   protected val uriParsers4POST = 
    new UriParser()
    .mapping("/random-word/collect", collectMoreWords)
    .onMatchingFailed((req: HttpServletRequest, resp: HttpServletResponse) => {
    	JSONObject(Map("success" -> 0, "errmsg" -> "invalid path uri")).toString
    })

        
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    req.setCharacterEncoding("UTF-8")
    resp.setContentType("application/json) charset=UTF-8")
    
    val pathInfo = if(req.getPathInfo == null) "/" else req.getPathInfo
    
    try {
      val writer = resp.getWriter
      writer.print(uriParsers4GET.matching(pathInfo, req, resp))
      writer.flush
    } catch {
      case e: Exception => {
        val errmsg = s"${e.getClass.getSimpleName}, errmsg: ${e.getMessage}"
        if(e.isInstanceOf[IllegalArgumentException]) logger.error(errmsg)
        else logger.error(errmsg, e)
        val writer = resp.getWriter
        writer.print(JSONObject(Map("success" -> 0, "errmsg" -> errmsg)))
        writer.flush
      }
    }
  }
  
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    req.setCharacterEncoding("UTF-8")
    resp.setContentType("application/json) charset=UTF-8")
    
    val pathInfo = if(req.getPathInfo == null) "/" else req.getPathInfo
    
    try {
      val writer = resp.getWriter
      writer.print(uriParsers4POST.matching(pathInfo, req, resp))
      writer.flush
    } catch {
      case e: Exception => {
        val errmsg = s"${e.getClass.getSimpleName}, errmsg: ${e.getMessage}"
        if(e.isInstanceOf[IllegalArgumentException]) logger.error(errmsg)
        else logger.error(errmsg, e)
        val writer = resp.getWriter
        writer.print(JSONObject(Map("success" -> 0, "errmsg" -> errmsg)))
        writer.flush
      }
    }
  }

  var randomWordCount = 1 //DEBUG
  
  //GET
  def collectedWordCount(req: HttpServletRequest, resp: HttpServletResponse, pathParams: Map[String, String]): String = {
    JSONObject(Map("success" -> 1, "count" -> randomWordCount)).toString
  }
  
  //GET
  def randomWord(req: HttpServletRequest, resp: HttpServletResponse, pathParams: Map[String, String]): String = {
    JSONObject(Map("success" -> 1, "word" -> ("word-" + randomWordCount))).toString
  }
  
  //POST
  def collectMoreWords(req: HttpServletRequest, resp: HttpServletResponse, pathParams: Map[String, String]): String = {
    randomWordCount = randomWordCount + 1
    JSONObject(Map("success" -> 1)).toString
  }
}
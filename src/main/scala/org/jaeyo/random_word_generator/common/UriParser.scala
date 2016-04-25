package com.igloosec.scripter.util

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters
import java.util.HashMap
import com.sun.jersey.api.uri.UriTemplate

class UriParser {
  case class UriCallback(callback: (HttpServletRequest, HttpServletResponse, Map[String, String]) => String)
  case class UriMapping(uriTmpl: String, uriCallback: UriCallback)
  protected val uriMappings = ListBuffer[UriMapping]()
  protected var onMatchingFailedCallback: (HttpServletRequest, HttpServletResponse) => String = null
  
  def mapping(uriTmpl: String, callback: (HttpServletRequest, HttpServletResponse, Map[String, String]) => String): UriParser = {
    uriMappings += UriMapping(uriTmpl, UriCallback(callback))
    this
  }
  
  def onMatchingFailed(callback: (HttpServletRequest, HttpServletResponse) => String): UriParser = {
    onMatchingFailedCallback = callback
    this
  }
  
  def matching(pathInfo: String, req: HttpServletRequest, resp: HttpServletResponse) = {
    require(pathInfo != null, "pathInfo is null")
    
    var pathParams = new HashMap[String, String]()
    
    uriMappings.find {
      (uriMapping: UriMapping) => new UriTemplate(uriMapping.uriTmpl).`match`(pathInfo, pathParams)
    } match {
      case Some(found: UriMapping) => {
        import JavaConverters._
        found.uriCallback.callback(req, resp, pathParams.asScala.toMap)
      }
      case None => if(onMatchingFailedCallback != null) onMatchingFailedCallback(req, resp)
    }
  }
}
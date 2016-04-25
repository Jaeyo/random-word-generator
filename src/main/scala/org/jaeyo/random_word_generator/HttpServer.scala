package org.jaeyo.random_word_generator

import java.io.File
import org.apache.commons.io.FileUtils
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.webapp.WebAppContext
import org.jaeyo.random_word_generator.common.Path
import org.jaeyo.random_word_generator.random_word.RandomWordServlet
import org.jaeyo.random_word_generator.random_word.RandomWordServlet

object HttpServer {
  def main(args: Array[String]): Unit = {
    try {
      System.setProperty("home.path", Path.packagePath.getAbsolutePath)
      
    	jettyLogSetting
 
    	val httpServer = makeHttpServer(1234)
    	httpServer.start
    	httpServer.join
    } catch {
      case e: Exception => {
        println(s"${e.getClass.getSimpleName}, errmsg: ${e.getMessage}")
        e.printStackTrace
      }
    }
  }
  
  protected def jettyLogSetting = {
		  System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog")
		  System.setProperty("org.jaeyo.random_word_generator.LEVEL", "DEBUG")
  }
  
  protected def makeHttpServer(port: Int) = {
    val server = new Server
    server.setThreadPool(new QueuedThreadPool(20))
    server.setStopAtShutdown(true)
   
    val connector = new SelectChannelConnector
    connector.setPort(port)
    server.setConnectors(Array(connector))
    
    server.setHandler(makeWebAppContext)
    
    server
  }
  
  protected def makeWebAppContext = {
    val context = new WebAppContext
    context.setClassLoader(Thread.currentThread.getContextClassLoader)
    context.setResourceBase(HttpServer.getClass.getClassLoader.getResource("resource/static").toExternalForm)
 
    context.addServlet(classOf[RandomWordServlet], "/random-word/*")
    
    context.setContextPath("/")
    
    makeWorkDir(context)
    
    context
  }
  
  protected def makeWorkDir(context: WebAppContext) = {
    val workDir = new File(Path.packagePath.getAbsolutePath, "work")
    FileUtils.deleteDirectory(workDir)
    context.setTempDirectory(workDir)
  }
}
package org.jaeyo.random_word_generator.common

import java.io.File

object Path {
  def packagePath = {
    val jarPath = Path.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val jarFile = new File(jarPath)
    jarFile.getParentFile
  }
}
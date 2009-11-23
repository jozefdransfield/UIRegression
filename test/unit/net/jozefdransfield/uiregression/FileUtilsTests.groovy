package net.jozefdransfield.uiregression

import org.gmock.GMockTestCase

public class FileUtilsTests extends GMockTestCase {
  
  void testCopyFileCopiesFiles() {
    def srcFile = mock(File)
    def destFile = mock(File)

    ordered {
      def mockInputStream = mock(FileInputStream, constructor(srcFile))
      def mockOutputStream = mock(FileOutputStream, constructor(destFile))

      mockInputStream.read(match{true}).returns(1)
      mockOutputStream.write(match() {true}, 0, 1)
      mockInputStream.read(match{true}).returns(0)
      mockInputStream.close()
      mockOutputStream.close()
    }
    play {
      FileUtils.copyFile(srcFile, destFile) 
    }

  }

}
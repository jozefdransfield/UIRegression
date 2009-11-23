package net.jozefdransfield.uiregression

public class FileUtils {

  public static void copyFile(File src, File dst) throws IOException {
    InputStream input = new FileInputStream(src)
    OutputStream out = new FileOutputStream(dst)

    byte[] buf = new byte[1024]
    int len
    while ((len = input.read(buf)) > 0) {
      out.write(buf, 0, len)
    }
    input.close()
    out.close()
  }
}
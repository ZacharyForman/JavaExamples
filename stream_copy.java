/**
 * Class that provides miscellaneous utilities.
 *
 * Do not use in production.
 */
class Utils {
  
  private final static int BUFFER_LENGTH = 4096;

  /**
   * Copies from an input stream to an output stream.
   * The caller must handle any exceptions thrown.
   *
   * @param in The stream to copy from.
   * @param out The stream to copy to.
   */
  public static void streamCopy(final InputStream in, final OutputStream out) throws IOException {
    char[] buffer = new char[BUFFER_LENGTH];
    int length;
    while ((length = in.read(buffer)) != -1) {
      out.write(buffer, length);
    }
  }

}

package java.com.foo.bar;

/**
 * Class that performs uploads in the background.
 */
public class Uploader {

  // 4K is a suitable buffer size on most modern architectures.
  private static int BUFFER_LENGTH = 4096;

  private final UploadServiceManager uploadServiceManager;

  /**
   * Constructs an Uploader that uses the given UploadServiceManager to perform uploads.
   */
  public Uploader(UploadServiceManager uploadServiceManager) {
    this.uploadServiceManager = uploadServiceManager;
  }

  /**
   * Uploads the file at the provided path to the server given by the object's uploadServiceManager.
   * 
   * @param filePath The path to the file to upload.
   */
  public void upload(final String filePath) {
    uploadServiceManager.setCallbackOnConnected(new CallbackOnConnected() {

      @Override
      void onConnected(UploadableFile uploadTarget) {
        performUpload(uploadServiceManager, filePath, uploadTarget);
      }

    }).tryConnecting();
  }

  /**
   * Uploads a file to a serverside target.
   *
   * @param uploadManager Object that handles actually uploading the file.
   * @param filePath Path to the file to be uploaded.
   * @param uploadTarget The serverside file to 
   */
  private static void performUpload(final UploadServiceManager uploadManager, 
                                    final String filePath,
                                    final UploadableFile uploadTarget) {
    new Thread() {

      @Override
      void run() {
        try {
          FileInputStream inputStream = new FileInputStream(new File(filePath));
          copyBetweenStreams(inputStream, uploadTarget);
          inputStream.close();
        } catch (IOException exception) {
          Log(String.format("File copying failed because: %s", exception.toString()));
        }
        uploadServiceManager.performUpload(uploadTarget);
      }

    }.run();
  }

  /**
   * Copies the contents of one stream to another.
   * Doesn't handle error conditions - caller must consider chance of exception.
   *
   * @param in The stream to read information from.
   * @param out The stream to write information to.
   * @throws IOException If there is an error reading from in or writing to out.
   */
  private static void copyBetweenStreams(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[BUFFER_LENGTH];
    int len;
    while ((len = in.read(buffer)) != -1) {
      out.write(buffer, len);
    }
  }

}
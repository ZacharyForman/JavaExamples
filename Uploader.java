package java.com.foo.bar;

class Uploader {

  public void upload(String filePath) {
    uploadServiceManager.setCallbackOnConnected(new CallbackOnConnected() {
      @Override
      void onConnected(UploadableFile uploadTarget) {
        new Thread() {
          @Override
          void run() {
            FileInputStream inputStream = null;
            try {
              File file = new File(filePath);
              inputStream = new FileInputStream(file);
            } catch (IOException exception) {
              Log("There was a problem opening the file");
            }
            try {
              if (inputStream != null) {
                byte[] buffer = new byte[1345];
                int len = inputStream.read(buffer);
                while (len >= 0) {
                  uploadTarget.write(buffer);
                  len = inputStream.read(buffer);
                }
              }
            } catch (IOException exception) {
              Log("There was a problem reading or writing");
            } finally {
              if (inputStream != null) {
                inputStream.close();
              }
            }
            uploadServiceManager.performUpload(uploadTarget);
          }
        }
      }
    }).tryConnecting();
  }

}
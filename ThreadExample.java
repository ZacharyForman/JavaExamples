package java.com.foo.bar;

/**
 * Class to demonstrate basic usage of wait and notify.
 */
class ThreadExample {
  private final Object lock = new Object();
  private boolean done = false;

  /**
   * Asynchronously waits for 30 seconds, then frobulates the Foo class.
   */
  public void do_something() {
    new Thread() {
      @Override
      public void run() {
        Thread.sleep(30);
        Foo.frobulate();
        // When frobulation is complete, signal our completion.
        synchronized (lock) {
          done = true;
          lock.notify();
        }
      }
    }.run();
  }

  /**
   * Calls do_something, and waits until frobulation of Foo is complete.
   */
  public void synchronously_do_something() {
    done = false;
    do_something();
    
    synchronized (lock) {
      // Wait until the task is completed
      while (!done) {
        lock.wait();
      }
    }

    Log("Finished!");
  }

}
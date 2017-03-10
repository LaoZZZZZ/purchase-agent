package com.purchase_agent.webapp.giraffe.script;

/**
 * Created by lukez on 3/8/17.
 */
public class MultipleThreadsScript {
    public static class RunnableUnit implements Runnable {
        private Thread th;
        private String threadName;

        public RunnableUnit(final String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            System.out.println("Running thread:" + this.threadName);
            try {
                for(int i = 4; i > 0; i--) {
                    System.out.println("Thread: " + threadName + ", " + i);
                    // Let the thread sleep for a while.
                    Thread.sleep(100);
                }
            }catch (InterruptedException e) {
                System.out.println("Thread " +  threadName + " interrupted.");
            }
            System.out.println("Thread " +  threadName + " exiting.");
        }

        public void start() {
            System.out.println("Starting " +  threadName );
            if (th == null) {
                th = new Thread (this, threadName);
                th.start ();
            }
        }
    }

    public static void main(String[] argvs) {
        RunnableUnit R1 = new RunnableUnit( "Thread-1");
        RunnableUnit R2 = new RunnableUnit( "Thread-2");

        R1.start();
        R2.start();
        System.exit(0);
    }
}

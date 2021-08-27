package org.cv.cloudwatch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestClass {
    public static void main(String[] args) throws InterruptedException {


        for(int i=0; i<50; i++) {
            Thread t = new Thread(new URLInvoker());
            t.setName("Thread" +i);
            t.start();
            t.join();
        }
    }

    static class URLInvoker implements Runnable {
        @Override
        public void run() {
            for(int i=0; i<2; i++) {
                try {
                    URL myURL = new URL("http://54.89.138.33:8080/hello");
                    URLConnection myURLConnection = myURL.openConnection();
                    myURLConnection.connect();
                    InputStream inputStream = myURLConnection.getInputStream();
                    inputStream.close();
                } catch (MalformedURLException e) {
                    System.out.println(e);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

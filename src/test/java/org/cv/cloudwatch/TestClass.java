package org.cv.cloudwatch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class TestClass {
    public static void main(String[] args) throws InterruptedException {


        for(int i=0; i<150; i++) {
            Thread t = new Thread(new URLInvoker());
            t.setName("InvokerThread" +i);
            t.start();
            t.join();

            Thread t2 = new Thread(new Calculator());
            t2.setName("CalcThread" +i);
            t2.start();
            t2.join();
        }
    }

    static class URLInvoker implements Runnable {
        @Override
        public void run() {
            for(int i=0; i<50; i++) {
                try {
                    URL myURL = new URL("http://3.236.167.172:8080/hello");
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

    static class Calculator implements Runnable {
        @Override
        public void run() {
            for(int i=0; i<50; i++) {
                try {
                    int num2 = new Random().nextInt(5);
                    URL myURL = new URL("http://3.236.167.172:8080/divide?num1=5&num2="+num2);
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

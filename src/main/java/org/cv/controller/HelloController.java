package org.cv.controller;

import org.cv.cloudwatch.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.awt.geom.Arc2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HelloController {

    @Autowired
    private MetricService metricService;

    private static final Logger LOGGER=LoggerFactory.getLogger(HelloController.class);

    private Map<String, Boolean> connectionPool;

    @PostConstruct
    public void init() {
        connectionPool = new HashMap<>();
        initConnectionPool();
        metricService.initiateGauge(connectionPool); // let the metric service monitor this collections
    }

    private void initConnectionPool() {
        for(int i=0; i<50; i++) {
            connectionPool.put("Connection_"+i, Boolean.TRUE);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        metricService.incrementHitCounter();
        String connectionName = null;
        LOGGER.trace("Request received.");
        try {
            // get first free connection
            Map.Entry<String, Boolean> availableConnection = connectionPool.entrySet().stream().filter(stringBooleanEntry -> stringBooleanEntry.getValue().equals(Boolean.TRUE)).findFirst().get();
            if(availableConnection == null) {
                return "Sorry ! No Connections available";
            }
            availableConnection.setValue(Boolean.FALSE);
            connectionName = availableConnection.getKey();

            // some processing...
            Thread.currentThread().sleep(new Random().nextInt(2) * 1000L);
        } catch (Exception e) {
            System.out.println(e);
          return "<h1>Something went wrong!<h1>";
        } finally {
            connectionPool.put(connectionName, Boolean.TRUE); // connection is available again
        }
        StringBuilder sb = new StringBuilder("<h1>Hello Clairvoyant !</h1>");
        sb.append("<br>");
        sb.append("<h2>Gauge:" + metricService.getGauge().value() + "<br>");
        sb.append("Counter:" + metricService.getHitCounter().count());
        return sb.toString();
    }

    @GetMapping("/divide")
    public String divideValue(HttpServletRequest request) {
        metricService.incrementHitCounter();
        float num1 = Float.parseFloat(request.getParameter("num1"));
        float num2 = Float.parseFloat(request.getParameter("num2"));

        if(num2 == 0) {
            LOGGER.error("Can not divide by zero - Num2 is:" + num2);
            return "Can not divide by zero - Num2 is ZERO";
        }
        return "Answer is: " + num1/num2;
    }
}

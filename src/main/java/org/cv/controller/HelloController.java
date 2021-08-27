package org.cv.controller;

import org.cv.cloudwatch.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class HelloController {

    @Autowired
    private MetricService metricService;

    private Map<String, Boolean> connectionPool;

    @PostConstruct
    public void init() {
        connectionPool = new HashMap<>();
        initConnectionPool();
        metricService.setConnectionPool(connectionPool); // let the metric service monitor this collections
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
        try {
            // get first free connection
            Map.Entry<String, Boolean> availableConnection = connectionPool.entrySet().stream().filter(stringBooleanEntry -> stringBooleanEntry.getValue().equals(Boolean.TRUE)).findFirst().get();
            if(availableConnection == null) {
                return "Sorry ! No Connections available";
            }
            availableConnection.setValue(Boolean.FALSE);
            connectionName = availableConnection.getKey();

            // some processing...
            Thread.currentThread().sleep(new Random().nextInt(5) * 1000L);
        } catch (Exception e) {
            System.out.println(e);
          return "<h1>Something went wrong!<h1>";
        } finally {
            connectionPool.put(connectionName, Boolean.TRUE); // connection is available again
        }
        return "<h1>Hello Clairvoyant !</h1>";
    }
}

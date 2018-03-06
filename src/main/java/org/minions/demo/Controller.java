package org.minions.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class Controller {

    private static final Log log = LogFactory.getLog(Controller.class);
    private final String version = "0.1";

    private MinionsLibrary minionsLibrary;

    @Value("${spring.application.name}")
    private String appName;

    public Controller(MinionsLibrary minionsLibrary){
        this.minionsLibrary=minionsLibrary;
    }

    @RequestMapping( method=GET)
    @ResponseBody
    public String minion() throws UnknownHostException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Host: ").append(InetAddress.getLocalHost().getHostName()).append("<br/>");
        stringBuilder.append("Minion Type: ").append(appName).append("<br/>");
        stringBuilder.append("IP: ").append(InetAddress.getLocalHost().getHostAddress()).append("<br/>");
        stringBuilder.append("Version: ").append(version).append("<br/>");
        stringBuilder.append(minionsLibrary.getMinion(appName));
        return stringBuilder.toString();
    }
    @RequestMapping( method=POST, path = "/work/{minion}")
    public void work(@PathVariable("minion") String minion){
        log.info("I'm doing some work for: "+ minion);
    }
}

package org.minions.demo;

import java.util.List;
import java.util.Map;

import feign.Feign;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@Import(FeignClientsConfiguration.class)
public class Application implements CommandLineRunner {

    private static final Log log = LogFactory.getLog(Application.class);
    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Minion Started! ");
    }

    @Scheduled(fixedRate = 2000)
    public void isThereGoldOutThere() {
        List<String> services = this.discoveryClient.getServices();
        for (String s : services) {
            log.info("Service ID: " + s);
            List<ServiceInstance> instances = this.discoveryClient.getInstances(s);

            for (ServiceInstance si : instances) {

                Map<String, String> metadata = si.getMetadata();
                String type = metadata.get("type");
                log.info("Service Type: " + type);
                if ("worker".equals(type)) {
                    String url = "http://" + si.getServiceId() + ":" + si.getPort();
                    log.info("POST to Service @: " + url + " -> from: " + appName);
                    Worker worker = Feign.builder().target(Worker.class,
                                                           url);
                    worker.work(appName);
                }
            }
        }
    }
}

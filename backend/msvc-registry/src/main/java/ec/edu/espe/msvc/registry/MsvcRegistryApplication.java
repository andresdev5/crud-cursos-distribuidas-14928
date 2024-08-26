package ec.edu.espe.msvc.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MsvcRegistryApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsvcRegistryApplication.class, args);
	}
}

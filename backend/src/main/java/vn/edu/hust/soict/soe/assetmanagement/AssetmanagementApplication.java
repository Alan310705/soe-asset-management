package vn.edu.hust.soict.soe.assetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point. Starts the SOE Asset Management API server
 * and auto-configures all modules (auth, asset, stock, handover, etc.).
 */
@SpringBootApplication
public class AssetmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetmanagementApplication.class, args);
	}

}

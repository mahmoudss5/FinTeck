package BankSystem.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableRetry // Enable Spring Retry functionality when needed
@EnableAsync
public class AzBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzBankApplication.class, args);
	}
    /*
    Admin email:mahmoud@gmai.com
    Admin password:test1234
     */

}

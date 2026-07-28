package kr.co.teamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TeamoServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamoServerApplication.class, args);
	}

}

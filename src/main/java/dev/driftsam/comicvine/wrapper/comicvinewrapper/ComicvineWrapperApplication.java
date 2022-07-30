package dev.driftsam.comicvine.wrapper.comicvinewrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "dev.driftsam")
public class ComicvineWrapperApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ComicvineWrapperApplication.class, args);
	}

	@Bean
	public RestTemplateBuilder restTemplateBuilder(){
		return new RestTemplateBuilder();
	}

}

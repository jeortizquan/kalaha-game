package com.lighthouse.kalah.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lighthouse.kalah.game.Game;
import com.lighthouse.kalah.game.Kalahah;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSwagger2
public class KalahConfiguration {

    @Bean
    public ConcurrentHashMap<Integer, Kalahah> gamesList() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Game game(@Qualifier("gamesList") ConcurrentHashMap<Integer, Kalahah> gamesList) {
        return new Game(gamesList);
    }

    @Bean
    public Gson gsonService() {
        return new GsonBuilder().create();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}

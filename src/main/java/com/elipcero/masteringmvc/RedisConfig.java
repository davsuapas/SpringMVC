package com.elipcero.masteringmvc;

import org.springframework.context.annotation.Configuration;

/*
 We're done! We can now launch our app with the following flag:
-Dspring.profiles.active=redis
You can also generate the JAR with gradlew build and launch it with the following
command:
java -Dserver.port=$PORT -Dspring.profiles.active=redis -jar app.jar
Alternatively, you can launch it with Gradle in Bash, as follows:
SPRING_PROFILES_ACTIVE=redis ./gradlew bootRun
 */

import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@Profile("redis") // El fichero application-redis.properties solo se carga si se activa este perfil
@EnableRedisHttpSession
public class RedisConfig {
}
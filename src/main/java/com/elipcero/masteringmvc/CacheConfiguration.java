// Personaliza la configración de cache

//import java.util.concurrent.TimeUnit;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.guava.GuavaCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCaching
//public class CacheConfiguration {
//	
//	@Bean
//	public CacheManager cacheManager() {
//		
//		GuavaCacheManager cacheManager = new GuavaCacheManager("searches");
//		
//		cacheManager
//			.setCacheBuilder(
//					CacheBuilder.newBuilder()
//						.softValues()
//						.expireAfterWrite(10, TimeUnit.MINUTES)
//		);
//		
//			return cacheManager;
//	}
//}


// Configuración par aun sistema distribuido
//@Configuration
//@Profile("redis")
//@EnableRedisHttpSession
//public class RedisConfig {
//@Bean(name = "objectRedisTemplate")
//public RedisTemplate objectRedisTemplate(RedisConnectionFactory
//redisConnectionFactory) {
//RedisTemplate<Object, Object> template = new
//RedisTemplate<>();
//template.setConnectionFactory(redisConnectionFactory);
//return template;
//}
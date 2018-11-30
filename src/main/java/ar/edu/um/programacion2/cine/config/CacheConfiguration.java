package ar.edu.um.programacion2.cine.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(ar.edu.um.programacion2.cine.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Cliente.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Cliente.class.getName() + ".tickets", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Ticket.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Ticket.class.getName() + ".ocupacions", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Entrada.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Entrada.class.getName() + ".ocupacions", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Ocupacion.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Butaca.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Butaca.class.getName() + ".ocupacions", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Sala.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Sala.class.getName() + ".funcions", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Sala.class.getName() + ".butacas", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Funcion.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Funcion.class.getName() + ".ocupacions", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Pelicula.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Pelicula.class.getName() + ".funcions", jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Calificacion.class.getName(), jcacheConfiguration);
            cm.createCache(ar.edu.um.programacion2.cine.domain.Calificacion.class.getName() + ".peliculas", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}

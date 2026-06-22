package cl.catastrofescl.notifications.config;

import cl.catastrofescl.notifications.security.FiltroAutenticacionDev;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

    @Value("${catastrofescl.auth.dev-mode:false}")
    private boolean devMode;

    @Value("${catastrofescl.auth.dev-trust-gateway-firebase-headers:false}")
    private boolean devTrustGatewayFirebaseHeaders;

    @Value("${catastrofescl.auth.dev-default-role-for-gateway:AUTORIDAD}")
    private String devDefaultRoleForGateway;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/ws-notificaciones/**").permitAll()
                        .anyRequest().authenticated()
                );

        if (devMode) {
            log.warn("ms-notifications en MODO DEV (headers X-Dev-* / gateway). NO usar en produccion.");
            http.addFilterBefore(
                    new FiltroAutenticacionDev(devTrustGatewayFirebaseHeaders, devDefaultRoleForGateway),
                    UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }
}

package org.esfe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {
    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("select nombre_usuario, clave, status from usuarios where nombre_usuario = ?");
        users.setAuthoritiesByUsernameQuery(
                "select u.nombre_usuario, concat('ROLE_', upper(r.nombre)) " +
                        "from usuarios u " +
                        "inner join roles r on r.id = u.rol_id " +
                        "where u.nombre_usuario = ?"
        );
        return users;
    }

    /**
     * Configuración de protección del contenido de la aplicación
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/home", "/home/**", "/privacy", "/terms").permitAll()
                        .requestMatchers("/destinoTuristicos", "/destinoTuristicos/details/**").permitAll()
                        .requestMatchers("/busqueda").permitAll()  // Permitir acceso público a la búsqueda
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
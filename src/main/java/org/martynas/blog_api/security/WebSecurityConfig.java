package org.martynas.blog_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private static final String USERS_SQL_QUERY = "select username,password,enabled from users where username = ?";
    private static final String AUTHORITIES_SQL_QUERY = "select users.username, authorities.authority\n" +
            "from users\n" +
            "inner join users_authorities on (users.id = users_authorities.user_id)\n" +
            "inner join authorities on (users_authorities.authority_id = authorities.id)\n" +
            "where users.username = ?;";

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                // fix H2 console access problem
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/createNewPost/**", "/editPost/**").hasRole("USER")
//                .antMatchers("/login").hasRole("USER")
                .antMatchers("/deletePost/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
//                .formLogin()
                .httpBasic()
                .and()
                // No need cookies for API with Basic Auth, right ?
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder
                .jdbcAuthentication()
                .usersByUsernameQuery(USERS_SQL_QUERY) // not really necessary, as users table follows default Spring Security User schema
                .authoritiesByUsernameQuery(AUTHORITIES_SQL_QUERY)  // a must as using customized authorities table, many to many variation
                .dataSource(dataSource)
                .passwordEncoder(bcryptEncoder());
    }

}

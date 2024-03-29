package com.example.demo1.security.config;


import com.example.demo1.jwt.AuthEntryPointJwt;
import com.example.demo1.jwt.JWTAuth;
import com.example.demo1.service.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@AllArgsConstructor
public class Configuration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private UserDetailService userDetailsService;
    private AuthEntryPointJwt unauthorizedRequest;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedRequest).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/signIn").permitAll()
                .antMatchers("/signUp", "/findByPESEL", "/savePatient", "/getPdf", "/contact",
                        "/prices", "/news", "/savePatient", "/home", "/fileUpload", "/getDoctorList").permitAll()
                .antMatchers("/getAllPatients").hasAnyRole("ADMIN", "DOCTOR")
                .antMatchers("/patient/pendingVisits/**", "/doctor/getPatient/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                .antMatchers("/doctor/editVisit/**",
                        "/doctor/editPatientProfile",
                        "/doctor/deletePatient/**").hasAnyRole("DOCTOR", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/patient/**").hasRole("PATIENT")
                .antMatchers("/doctor/**").hasRole("DOCTOR")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JWTAuth authenticationJwtTokenFilter() {
        return new JWTAuth();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://192.168.0.126:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("HTTP/1.1 200 OK", "Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With", "requestId", "Correlation-Id")
                .allowCredentials(true);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

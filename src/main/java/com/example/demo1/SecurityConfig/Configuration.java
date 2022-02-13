package com.example.demo1.SecurityConfig;


import com.example.demo1.JWT.AuthEntryPointJwt;
import com.example.demo1.JWT.JWTAuth;
import com.example.demo1.Services.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
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

    @Autowired
    UserDetailService userDetailsService;

    @Autowired
    AuthEntryPointJwt unauthorizedRequest;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

                http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedRequest).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/signIn").permitAll()
                .antMatchers("/signUp","/findByPESEL","/savePatient",
                        "/saveReceptionist", "/saveDoctor", "/contact",
                        "/prices", "/news","/savePatient", "/home", "/fileUpload", "/getDoctorList").permitAll()
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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/allowCors").allowedOrigins("http://localhost:4200/**");
            }
        };
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

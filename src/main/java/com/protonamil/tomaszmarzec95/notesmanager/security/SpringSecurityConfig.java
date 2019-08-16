package com.protonamil.tomaszmarzec95.notesmanager.security;

import com.protonamil.tomaszmarzec95.notesmanager.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private JwtTokenProvider jwtTokenProvider;

    public SpringSecurityConfig(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                                JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        if(roleRepository.findByRole("USER") == null) {
            roleRepository.save(new Role("USER"));
        }

        UserDetailsService userDetailsService = userDetails();
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/user/**").permitAll()
                .and()
                .csrf().disable();
    }*/

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers("/user/**").permitAll()
                .antMatchers("/notes/**").hasAuthority("USER")
        .and()
        .csrf().disable();
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/user/**").permitAll()// SOLUCJA
                .antMatchers("/notes/**").hasAuthority("USER")
                    .anyRequest().authenticated()
                    .and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
        http.cors();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }

    @Bean
    public UserDetailsService userDetails() {
        return new UserService();
    }

}

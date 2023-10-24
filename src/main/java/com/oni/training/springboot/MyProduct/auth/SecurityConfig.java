package com.oni.training.springboot.MyProduct.auth;


import com.oni.training.springboot.MyProduct.entity.app_user.UserAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
//  「*」：代表0到多個字元。如「/products/*」適用於「/products」、「/products/123」，但不適用「/products/123/draft」
//  「**」：代表0到多個路徑。如「/products/**」適用於「/products」底下任何路徑。
//  「?」：代表一個字元。如「/products/?*」適用於「/products/1」、「/products/123」，但不適用「/products」。

    @Autowired
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "v3",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v3/api-docs.yaml",

            // other public endpoints of your API may be appended to this array
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest->
                        authorizeRequest
                                // 2023/10/23 新增白名單 讓 網頁可以連swagger
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/GetEmails").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/authenticate").permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/GetUserDetail").hasAuthority(UserAuthority.ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/users").permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/*").authenticated()
                                .requestMatchers("/users/test/ByPass/addRole/**").permitAll() // 此路徑包含  /user....../addRole
//                                .requestMatchers("users/test/ByPass/addRole/**").permitAll() //找到未授權的原因了 因為我開頭沒加上 /user

                                .requestMatchers("/products/**").permitAll()
//                                .requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers("/errors").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/AOP/**").permitAll()

//                                上面驗證的時候再打開就好 (無需要先不要開 有點麻煩<err需要處理加工會用到>)
//                                如果沒打開 基本上有錯誤都會被 變成403 然後如果打開就會顯示真的錯誤
//                                但是想要詳細內容
//                                要跟之前一樣 使用Error物件跟自己定義想回傳的ResponseEntity去回傳!
                                .anyRequest().authenticated()


                )
                .sessionManagement(sessionManagement->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
//        不會自動觸發 UsernamePasswordAuthenticationFilter
//        如果沒有.formLogin(formLogin -> formLogin
//                .loginPage("/login") // 登录页面的URL
//                .loginProcessingUrl("/perform-login") // 处理登录请求的URL
//                .defaultSuccessURL("/dashboard") // 登录成功后的默认URL
//                .permitAll()
    }
}

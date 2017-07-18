package net.teachingprogramming.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // アカウントの設定
        auth.inMemoryAuthentication().withUser("admin").password("adminpassword").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user1").password("user1password").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 「/suppl01/secret/**」は認証が必要で、それ以外は認証が不要
        http.authorizeRequests().antMatchers("/suppl01/secret/**").authenticated().anyRequest().permitAll();

        // ログイン
        http.formLogin().loginPage("/suppl01/login").usernameParameter("username").passwordParameter("password")
                .loginProcessingUrl("/suppl01/loginProcess").defaultSuccessUrl("/suppl01/secret/").failureUrl("/suppl01/login?error");

        // ログアウト
        http.logout().logoutUrl("/suppl01/logout").logoutSuccessUrl("/suppl01/");
    }

}

package com.oupeng.joke.back.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(md5Encoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/statistics/dayDetailExport","/source/crawlExport","/source/qualityExport", "/joke/incrementComment", "/joke/decrementComment", "/resources/image/*").and().formLogin().loginPage("/login.jsp").and().formLogin().loginProcessingUrl("/login").and()
				.formLogin().defaultSuccessUrl("/home").and().formLogin().failureUrl("/?error=1");
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		http.rememberMe().tokenValiditySeconds(1209600).and().rememberMe().rememberMeParameter("remember-me");
		CharacterEncodingFilter encodeFilter = new CharacterEncodingFilter();
		encodeFilter.setEncoding("utf-8");
		encodeFilter.setForceEncoding(true);
		http.addFilterBefore(encodeFilter, CsrfFilter.class); // 放在csrf filter前面
		http.headers().disable();
		HeaderWriter headerWriter = new HeaderWriter() {
			public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
				response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
				response.setHeader("Expires", "0");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("X-Frame-Options", "SAMEORIGIN");
				response.setHeader("X-XSS-Protection", "1; mode=block");
				response.setHeader("x-content-type-options", "nosniff");
			}
		};
		List<HeaderWriter> headerWriterFilterList = new ArrayList<HeaderWriter>();
		headerWriterFilterList.add(headerWriter);
		HeaderWriterFilter headerFilter = new HeaderWriterFilter(headerWriterFilterList);
		http.addFilter(headerFilter);
	}
}
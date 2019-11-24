package com.achaves.pontointeligente.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration //Classe de confirguração
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
        val funcionarioDetailsService: FuncionarioDetailsService): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider())
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?. //informa que será utilizada autenticacao nas requisicoes
                anyRequest()?. //todas as requisições serão autenticadas
                authenticated()?.and()?. //garantir que todas as requisições serão autenticadas
                httpBasic()?.and()?. //autenticação do tipo basic
                sessionManagement()?. //inclui gerenciament de sessão, pois autenticação basic demenda sessao no servidor
                sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()?. //sessão válida somente durante a requisição
                csrf()?.disable() //desabilita proteção de segurança, por ser sessão stateless
    }

    @Bean //torna a funcao inicializavel, como se fosse um servico do spring
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(funcionarioDetailsService)
        authProvider.setPasswordEncoder(encoder())
        return authProvider
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}
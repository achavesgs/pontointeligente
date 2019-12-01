package com.achaves.pontointeligente.controller

import com.achaves.pontointeligente.documents.Lancamento
import com.achaves.pontointeligente.enums.TipoEnum
import com.achaves.pontointeligente.services.LancamentoService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import java.lang.Exception
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class LancamentoControllerTest {

    @MockBean
    private val lancamentoService: LancamentoService?=  null

    @Test
    @Throws(Exception::class)
    @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))//spring cria um usuario falso com o perfil admin
//    @WithMockUser
    fun testRemoverLancamento(){
        BDDMockito.given<Lancamento>(lancamentoService?.buscarPorId("1")).willReturn(obterDadosLancamento())
    }

    private fun obterDadosLancamento(): Lancamento = Lancamento(Date(), TipoEnum.INICIO_TRABALHO,
            "1", "Descrição", "1.237,9.287", "1")
}
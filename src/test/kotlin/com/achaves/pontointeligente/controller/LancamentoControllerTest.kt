package com.achaves.pontointeligente.controller

import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.documents.Lancamento
import com.achaves.pontointeligente.dto.LancamentoDTO
import com.achaves.pontointeligente.enums.PerfilEnum
import com.achaves.pontointeligente.enums.TipoEnum
import com.achaves.pontointeligente.services.FuncionarioService
import com.achaves.pontointeligente.services.LancamentoService
import com.achaves.pontointeligente.utils.SenhaUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc //criar mock para simular as requisições http
class LancamentoControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val lancamentoService: LancamentoService?=  null

    @MockBean
    private val funcionarioService: FuncionarioService?=  null

    private val urlBase: String =  "/api/lancamentos/"
    private val idFuncionario: String =  "1"
    private val idLancamento: String =  "1"
    private val tipo: String = TipoEnum.INICIO_TRABALHO.name
    private val data: Date = Date()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


    @Test
    @Throws(Exception::class)
    @WithMockUser //utilizar um usuario qualque de teste
    fun testCadastrarLancamento(){
        val lancamento = obterDadosLancamento()
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario))
                .willReturn(funcionario())
        BDDMockito.given<Lancamento>(lancamentoService?.buscarPorId(idLancamento))
                .willReturn(obterDadosLancamento())
        BDDMockito.given<Lancamento>(lancamentoService?.persistir(lancamento))
                .willReturn(lancamento)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tipo").value(tipo))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.data").value(dateFormat.format(data)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.funcionarioId").value(idFuncionario))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros").isEmpty)
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testCadastrarLancamentoFuncionarioIdInvalido() {
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(null)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros")
                        .value("Funcionario não encontrado. ID inexistente."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))//spring cria um usuario falso com o perfil admin
//    @WithMockUser
    fun testRemoverLancamento(){
        BDDMockito.given<Lancamento>(lancamentoService?.buscarPorId(idLancamento))
                .willReturn(obterDadosLancamento())

        mvc!!.perform(MockMvcRequestBuilders.delete(urlBase+idLancamento)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Throws(JsonProcessingException::class)
    private fun obterJsonRequisicaoPost(): String{
        val lancamentoDto: LancamentoDTO = LancamentoDTO(
                data = dateFormat.format(data), tipo = tipo, descricao = "Descrição",
                localizacao = "1.234,4.234", funcionarioId = idFuncionario, id = idLancamento)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(lancamentoDto) //gera json no formato string
    }

    private fun obterDadosLancamento(): Lancamento = Lancamento(
            data = dateFormat.parse(dateFormat.format(data)), tipo = TipoEnum.valueOf(tipo),
            funcionarioId = idFuncionario, descricao = "Descrição",
            localizacao = "1.234,4.234", id = idLancamento)

    private fun funcionario(): Funcionario =
            Funcionario("Nome", "email@email.com",
                    SenhaUtils().gerarBcrypt("123456"),
                    "1234567890", PerfilEnum.ROLE_USUARIO,
                    idFuncionario)

}
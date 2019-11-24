package com.achaves.pontointeligente.controllers

import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.documents.Lancamento
import com.achaves.pontointeligente.documents.toLancamentoDto
import com.achaves.pontointeligente.dto.LancamentoDTO
import com.achaves.pontointeligente.enums.TipoEnum
import com.achaves.pontointeligente.response.Response
import com.achaves.pontointeligente.services.FuncionarioService
import com.achaves.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController (
        val lancamentoService: LancamentoService,
        val funcionarioService: FuncionarioService
) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping
    fun adicionar(
            @Valid //realiza as validações do DTO
            @RequestBody //converte de json para classe java
            lancamentoDTO: LancamentoDTO,
            result: BindingResult): ResponseEntity<Response<LancamentoDTO>> {

        val response: Response<LancamentoDTO> = Response<LancamentoDTO>()
        validarFuncionario(lancamentoDTO, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = converterDtoPraLancamento(lancamentoDTO, result)
        lancamentoService.persistir(lancamento)
        response.data = lancamento.toLancamentoDto()
        return ResponseEntity.ok(response)
    }

    private fun validarFuncionario(lancamentoDTO: LancamentoDTO, result: BindingResult) {
        if(lancamentoDTO.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionario não informado"))
            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDTO.funcionarioId).let { null }
        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionario não encontrado. ID inexistente."))
        }

    }

    private fun converterDtoPraLancamento(lancamentoDTO: LancamentoDTO,
                                          result: BindingResult): Lancamento {
        if (lancamentoDTO.id != null) {
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDTO.id!!).let { null }
            if (lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
        }
        return Lancamento(dateFormat.parse(lancamentoDTO.data), TipoEnum.valueOf(lancamentoDTO.tipo!!),
                lancamentoDTO.funcionarioId!!, lancamentoDTO.descricao, lancamentoDTO.localizacao, lancamentoDTO.id)
    }
}
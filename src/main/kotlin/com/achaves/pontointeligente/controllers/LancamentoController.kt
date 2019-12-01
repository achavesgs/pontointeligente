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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
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

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')") //anotação que é validada pelo método "EnableGlobalMethodSecurity",
    // para remover é necessário possuir um usuário com o perfil admin, definido no authorities
    fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {
        val response: Response<String> = Response<String>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Erro ao remover lancamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.remover(id)
        return ResponseEntity.ok(Response<String>())
    }

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

        val lancamento: Lancamento = lancamentoService.persistir(converterDtoPraLancamento(lancamentoDTO, result))
        response.data = lancamento.toLancamentoDto()
        return ResponseEntity.ok(response)
    }

    @GetMapping(value = "/{id}")
    fun listarLancamentoPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDTO>> {
        val response: Response<LancamentoDTO> = Response<LancamentoDTO>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null){
            response.erros.add("Lancamento não encontrado para o id $id.")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = lancamento.toLancamentoDto()
        return ResponseEntity.ok(response)
    }

    @GetMapping(value = "/funcionario/{funcionarioId}")
    fun listarPorFuncionarioId(@PathVariable("funcionarioId") funcionarioId: String,
                               @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                               @RequestParam(value = "ord", defaultValue = "id") ord: String,
                               @RequestParam(value = "dir", defaultValue = "DESC") dir: String
    ): ResponseEntity<Response<Page<LancamentoDTO>>> {
        val response: Response<Page<LancamentoDTO>> = Response<Page<LancamentoDTO>>()
        val pageRequest: PageRequest = PageRequest(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val lancamentos: Page<Lancamento> = lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)
        val lancamentosDto: Page<LancamentoDTO> = lancamentos.map { lancamento -> lancamento.toLancamentoDto() }

        response.data = lancamentosDto
        return ResponseEntity.ok(response)
    }

    @PutMapping(value = "/{id}")
    fun atualizar(
            @Valid //realiza as validações do DTO
            @RequestBody //converte de json para classe java
            lancamentoDTO: LancamentoDTO,
            @PathVariable("id") id: String,
            result: BindingResult): ResponseEntity<Response<LancamentoDTO>> {

        val response: Response<LancamentoDTO> = Response<LancamentoDTO>()
        validarFuncionario(lancamentoDTO, result)

        val lancamento: Lancamento = converterDtoPraLancamento(lancamentoDTO.copy(id = id), result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.persistir(lancamento)
        response.data = lancamento.toLancamentoDto()
        return ResponseEntity.ok(response)
    }


    private fun validarFuncionario(lancamentoDTO: LancamentoDTO, result: BindingResult) {
        if(lancamentoDTO.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionario não informado"))
            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDTO.funcionarioId)
        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionario não encontrado. ID inexistente."))
        }

    }

    private fun converterDtoPraLancamento(lancamentoDTO: LancamentoDTO,
                                          result: BindingResult): Lancamento {
        if (lancamentoDTO.id != null) {
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDTO.id!!)
            if (lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
        }
        return Lancamento(dateFormat.parse(lancamentoDTO.data), TipoEnum.valueOf(lancamentoDTO.tipo!!),
                lancamentoDTO.funcionarioId!!, lancamentoDTO.descricao, lancamentoDTO.localizacao, lancamentoDTO.id)
    }
}
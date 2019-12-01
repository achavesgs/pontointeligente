package com.achaves.pontointeligente.controllers

import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.documents.Lancamento
import com.achaves.pontointeligente.documents.atualizarDadosFuncionario
import com.achaves.pontointeligente.documents.toLancamentoDto
import com.achaves.pontointeligente.dto.FuncionarioDTO
import com.achaves.pontointeligente.dto.LancamentoDTO
import com.achaves.pontointeligente.dto.toFuncionarioDto
import com.achaves.pontointeligente.enums.TipoEnum
import com.achaves.pontointeligente.response.Response
import com.achaves.pontointeligente.services.FuncionarioService
import com.achaves.pontointeligente.services.LancamentoService
import com.achaves.pontointeligente.utils.SenhaUtils
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
@RequestMapping("/api/funcionarios")
class FuncionarioController (
        val funcionarioService: FuncionarioService
) {

    @PutMapping(value = "/{id}")
    fun atualizar(
            @Valid //realiza as validações do DTO
            @RequestBody //converte de json para classe java
            funcionarioDTO: FuncionarioDTO,
            @PathVariable("id") id: String,
            result: BindingResult): ResponseEntity<Response<FuncionarioDTO>> {

        val response: Response<FuncionarioDTO> = Response<FuncionarioDTO>()
        val funcionario: Funcionario? = funcionarioService.buscarPorId(id)

        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionario não encontrado."))
        }

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val funcAtualizar: Funcionario = funcionario!!.atualizarDadosFuncionario(funcionarioDTO)
        funcionarioService.persistir(funcAtualizar)
        response.data = funcAtualizar.toFuncionarioDto()
        return ResponseEntity.ok(response)
    }
}
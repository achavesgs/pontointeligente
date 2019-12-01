package com.achaves.pontointeligente.controllers

import com.achaves.pontointeligente.documents.Empresa
import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.documents.converterCadastroPFDto
import com.achaves.pontointeligente.dto.CadastroPFDTO
import com.achaves.pontointeligente.dto.converterDtoParaFuncionario
import com.achaves.pontointeligente.response.Response
import com.achaves.pontointeligente.services.EmpresaService
import com.achaves.pontointeligente.services.FuncionarioService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf")
class CadastroPFController(val empresaService: EmpresaService,
                           val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPFDTO: CadastroPFDTO,
                  result: BindingResult): ResponseEntity<Response<CadastroPFDTO>> {
        val response: Response<CadastroPFDTO> = Response<CadastroPFDTO>()

        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPFDTO.cnpj)
        validarDadosExistentes(cadastroPFDTO, empresa, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage ?: "")
            return ResponseEntity.badRequest().body(response)
        }

        val funcionario: Funcionario = funcionarioService.persistir(cadastroPFDTO.converterDtoParaFuncionario(empresa!!))
        response.data = funcionario.converterCadastroPFDto(empresa)

        return ResponseEntity.ok(response)
    }


    private fun validarDadosExistentes(cadastroPFDTO: CadastroPFDTO, empresa: Empresa?, result: BindingResult) {
        if (empresa == null) {
            result.addError(ObjectError("empresa", "Empresa não cadastrada."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPFDTO.cpf)
        if (funcionarioCpf != null) {
            result.addError(ObjectError("funcionario", "CPF já existente."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPFDTO.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionario", "Email já existente."))
        }
    }


}


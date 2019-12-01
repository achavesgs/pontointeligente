package com.achaves.pontointeligente.controllers

import com.achaves.pontointeligente.documents.Empresa
import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.documents.converterCadastroPJDto
import com.achaves.pontointeligente.dto.CadastroPJDTO
import com.achaves.pontointeligente.dto.converterDtoParaEmpresa
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
@RequestMapping("/api/cadastrar-pj")
class CadastroPJController(val empresaService: EmpresaService,
                           val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPJDTO: CadastroPJDTO,
                  result: BindingResult): ResponseEntity<Response<CadastroPJDTO>> {
        val response: Response<CadastroPJDTO> = Response<CadastroPJDTO>()

        validarDadosExistentes(cadastroPJDTO, result)
        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage ?: "")
            return ResponseEntity.badRequest().body(response)
        }

        val empresa: Empresa = cadastroPJDTO.converterDtoParaEmpresa()
        empresaService.persistir(empresa)

        val funcionario: Funcionario = cadastroPJDTO.converterDtoParaFuncionario(empresa)
        funcionarioService.persistir(funcionario)
        response.data = funcionario.converterCadastroPJDto(empresa)

        return ResponseEntity.ok(response)
    }


    private fun validarDadosExistentes(cadastroPJDTO: CadastroPJDTO, result: BindingResult) {
        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPJDTO.cnpj)
        if (empresa != null) {
            result.addError(ObjectError("empresa", "Empresa já existente."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPJDTO.cpf)
        if (funcionarioCpf != null) {
            result.addError(ObjectError("funcionario", "CPF já existente."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPJDTO.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionario", "Email já existente."))
        }
    }


}


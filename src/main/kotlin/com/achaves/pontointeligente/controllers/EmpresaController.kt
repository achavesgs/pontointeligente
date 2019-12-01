package com.achaves.pontointeligente.controllers

import com.achaves.pontointeligente.documents.Empresa
import com.achaves.pontointeligente.documents.converterEmpresaDto
import com.achaves.pontointeligente.dto.EmpresaDTO
import com.achaves.pontointeligente.response.Response
import com.achaves.pontointeligente.services.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService) {

    @GetMapping(value = "/cnpj/{cnpj}")
    fun buscaPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<EmpresaDTO>> {
        val response: Response<EmpresaDTO> = Response<EmpresaDTO>()
        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

        if (empresa == null) {
            response.erros.add("Empresa não encontrada para o CNPJ ${cnpj}.")
        }

        response.data = empresa?.converterEmpresaDto()

        return ResponseEntity.ok(response)
    }
}


package com.achaves.pontointeligente.dto

data class EmpresaDTO(
        val razaoSocial: String,
        val cnpj: String,
        val id: String? = null
)
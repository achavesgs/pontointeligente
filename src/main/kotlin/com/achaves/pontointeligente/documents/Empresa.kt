package com.achaves.pontointeligente.documents

import com.achaves.pontointeligente.dto.EmpresaDTO
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Empresa (
        val razaoSocial: String,
        val cnpj: String,
        @Id
        val id: String?= ObjectId().toHexString()//?=pode ser nulo
)

fun Empresa.converterEmpresaDto(): EmpresaDTO =
        EmpresaDTO(razaoSocial, cnpj, id)
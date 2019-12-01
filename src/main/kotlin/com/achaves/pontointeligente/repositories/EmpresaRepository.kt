package com.achaves.pontointeligente.repositories

import com.achaves.pontointeligente.documents.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository : MongoRepository<Empresa, String> {//deve referenciar qual documento e o tipo da chave primaria

    fun findByCnpj(cnpj: String): Empresa?
}
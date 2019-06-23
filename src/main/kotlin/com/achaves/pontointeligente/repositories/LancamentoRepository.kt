package com.achaves.pontointeligente.repositories

import com.achaves.pontointeligente.documents.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface LancamentoRepository : MongoRepository<Lancamento, String> {//deve referenciar qual documento e o tipo da chave primaria

    fun findByFuncionarioId(funcionarioId: String, pageable: Pageable): Page<Lancamento>
}
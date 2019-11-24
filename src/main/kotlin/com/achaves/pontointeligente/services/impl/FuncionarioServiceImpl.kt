package com.achaves.pontointeligente.services.impl

import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.repositories.FuncionarioRepository
import com.achaves.pontointeligente.services.FuncionarioService
import org.springframework.stereotype.Service
import java.util.*

@Service
class FuncionarioServiceImpl(val funcionarioRepository: FuncionarioRepository) : FuncionarioService {
    override fun persistir(funcionario: Funcionario): Funcionario = funcionarioRepository.save(funcionario)

    override fun buscarPorCpf(cpf: String): Funcionario? = funcionarioRepository.findByCpf(cpf)

    override fun buscarPorEmail(email: String): Funcionario? = funcionarioRepository.findByEmail(email)

    override fun buscarPorId(id: String): Optional<Funcionario> = funcionarioRepository.findById(id)
}
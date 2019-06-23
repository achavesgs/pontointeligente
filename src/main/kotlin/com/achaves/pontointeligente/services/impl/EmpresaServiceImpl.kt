package com.achaves.pontointeligente.services.impl

import com.achaves.pontointeligente.documents.Empresa
import com.achaves.pontointeligente.repositories.EmpresaRepository
import com.achaves.pontointeligente.services.EmpresaService
import org.springframework.stereotype.Service

@Service //o container do spring framework saiba da existencia deste servico
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService { //spring injeta uma instancia do repositorio

    override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)

}
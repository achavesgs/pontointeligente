package com.achaves.pontointeligente

import com.achaves.pontointeligente.documents.Empresa
import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.enums.PerfilEnum
import com.achaves.pontointeligente.repositories.EmpresaRepository
import com.achaves.pontointeligente.repositories.FuncionarioRepository
import com.achaves.pontointeligente.repositories.LancamentoRepository
import com.achaves.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PontointeligenteApplication(
		val empresaRepository: EmpresaRepository,
		val funcionarioRepository: FuncionarioRepository,
		val lancamentoRepository: LancamentoRepository
) : CommandLineRunner{
	override fun run(vararg args: String?) {
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()

		val empresa: Empresa = Empresa("Empresa", "10443887000146")
		empresaRepository.save(empresa)

		val admin: Funcionario = Funcionario("Admin", "admin@empresa.com",
				SenhaUtils().gerarBcrypt("12456"), "25708317000",
				PerfilEnum.ROLE_ADMIN, empresa.id!!)
		funcionarioRepository.save(admin)

		val funcionario: Funcionario = Funcionario("Funcionario", "funcionario@empresa.com",
				SenhaUtils().gerarBcrypt("12456"), "44325441557",
				PerfilEnum.ROLE_USUARIO, empresa.id!!)
		funcionarioRepository.save(funcionario)

		System.out.println("Empresa ID: ${empresa.id}")
		System.out.println("Admin ID: ${admin.id}")
		System.out.println("Funcionario ID: ${funcionario.id}")
	}

}

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}

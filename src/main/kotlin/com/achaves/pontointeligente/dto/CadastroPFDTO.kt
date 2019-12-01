package com.achaves.pontointeligente.dto

import com.achaves.pontointeligente.documents.Empresa
import com.achaves.pontointeligente.documents.Funcionario
import com.achaves.pontointeligente.enums.PerfilEnum
import com.achaves.pontointeligente.utils.SenhaUtils
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class CadastroPFDTO (
        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Email deve conter entre 3 e 200 caracteres.")
        @get:Email(message = "Email inválido.")
        val email: String = "",

        @get:NotEmpty(message = "Senha não pode ser vazia.")
        val senha: String = "",

        @get:NotEmpty(message = "CPF não pode ser vazia.")
        @get:CPF(message = "CPF inválido.")
        val cpf: String = "",

        @get:NotEmpty(message = "CNPJ não pode ser vazia.")
        @get:CNPJ(message = "CNPJ inválido.")
        val cnpj: String = "",

        val empresaId: String = "",
        val valorHora: String? = null,
        val qtdHorasTrabalhoDia: String? = null,
        val qtdHorasAlmoco: String? = null,
        val id: String? = null
)

fun CadastroPFDTO.converterDtoParaFuncionario(empresa: Empresa): Funcionario =
        Funcionario(nome, email, SenhaUtils().gerarBcrypt(senha), cpf, PerfilEnum.ROLE_ADMIN,
                empresa.id.toString(), valorHora?.toDouble(), qtdHorasTrabalhoDia?.toFloat(),
                qtdHorasAlmoco?.toFloat(), id)
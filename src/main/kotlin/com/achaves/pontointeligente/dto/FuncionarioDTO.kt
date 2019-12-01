package com.achaves.pontointeligente.dto

import com.achaves.pontointeligente.documents.Funcionario
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class FuncionarioDTO (

        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Email deve conter entre 3 e 200 caracteres.")
        @get:Email(message = "Email inválido.")
        val email: String = "",

        val senha: String? = null,
        val valHora: String? = null,
        val qtdHorasTrabalhoDia: String? = null,
        val qtdHorasAlmoco: String? = null,
        val id: String? = null
)

fun Funcionario.toFuncionarioDto(): FuncionarioDTO =
        FuncionarioDTO(nome, email, "", valorHora.toString(), qtdHorasTrabalhoDia.toString(),
                qtdHorasAlmoco.toString(), id)
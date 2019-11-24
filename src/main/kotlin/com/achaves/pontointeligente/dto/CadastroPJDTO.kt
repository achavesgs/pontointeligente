package com.achaves.pontointeligente.dto

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class CadastroPJDTO (
        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Email deve conter entre 3 e 200 caracteres.")
        @get:Email(message = "Email inválido.")
        val email: String = "",

        @get:NotEmpty(message = "Senha não pode ser vazia.")
        val senha: String? = null,

        @get:NotEmpty(message = "CPF não pode ser vazia.")
        @get:CPF(message = "CPF inválido.")
        val cpf: String? = null,

        @get:NotEmpty(message = "CNPJ não pode ser vazia.")
        @get:CNPJ(message = "CNPJ inválido.")
        val cnpj: String? = null,

        @get:NotEmpty(message = "Razao Social não pode ser vazia.")
        @get:Length(min = 5, max = 200, message = "Razão Social deve conter entre 5 e 200 caracteres.")
        val razaoSocial: String? = null,

        val id: String? = null
)
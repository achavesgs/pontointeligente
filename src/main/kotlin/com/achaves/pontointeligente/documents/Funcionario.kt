package com.achaves.pontointeligente.documents

import com.achaves.pontointeligente.dto.CadastroPFDTO
import com.achaves.pontointeligente.dto.CadastroPJDTO
import com.achaves.pontointeligente.dto.FuncionarioDTO
import com.achaves.pontointeligente.enums.PerfilEnum
import com.achaves.pontointeligente.utils.SenhaUtils
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Funcionario (
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    val perfil: PerfilEnum,
    val empresaId: String,
    val valorHora: Double? = 0.0,
    val qtdHorasTrabalhoDia: Float? = 0.0f,
    val qtdHorasAlmoco: Float? = 0.0f,
    @Id val id: String? = ObjectId().toHexString()
)

fun Funcionario.converterCadastroPJDto(empresa: Empresa): CadastroPJDTO =
        CadastroPJDTO(nome, email, "", cpf, empresa.cnpj, empresa.razaoSocial, id)

fun Funcionario.converterCadastroPFDto(empresa: Empresa): CadastroPFDTO =
        CadastroPFDTO(nome, email, "", cpf,
                empresa.cnpj, id.toString(), valorHora.toString(),
                qtdHorasTrabalhoDia.toString(),
                qtdHorasTrabalhoDia.toString(),
                id)

fun Funcionario.atualizarDadosFuncionario(funcionarioDTO: FuncionarioDTO): Funcionario {
    var senha: String
    if (funcionarioDTO.senha == null){
        senha = this.senha
    } else {
        senha = SenhaUtils().gerarBcrypt(funcionarioDTO.senha)
    }

    return Funcionario(funcionarioDTO.nome, email, senha,
            cpf, perfil, empresaId,
            funcionarioDTO.valHora?.toDouble(),
            funcionarioDTO.qtdHorasTrabalhoDia?.toFloat(),
            funcionarioDTO.qtdHorasAlmoco?.toFloat(),
            id)
}
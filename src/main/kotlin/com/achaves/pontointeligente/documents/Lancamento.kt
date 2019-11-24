package com.achaves.pontointeligente.documents

import com.achaves.pontointeligente.dto.LancamentoDTO
import com.achaves.pontointeligente.enums.TipoEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.text.SimpleDateFormat
import java.util.*

@Document
data class Lancamento (
        val data: Date,
        val tipo: TipoEnum,
        val funcionarioId: String,
        val descricao: String? = "",
        val localizacao: String? = "",
        @Id val id: String? = ""
)

fun Lancamento.toLancamentoDto(): LancamentoDTO {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return LancamentoDTO(dateFormat.format(data),
            tipo.toString(), descricao, localizacao, funcionarioId, id)
}
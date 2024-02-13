package tihonin.sergey.databasemodels.token

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tihonin.sergey.databasemodels.user.Users


object Tokens: Table("sessions") {
    private val login = Tokens.varchar("login", 150)
    private val token = Tokens.varchar("token", 150)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }
    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDTO(
                            login = it[login],
                            token = it[token]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun deleteToken(tokenDTO: TokenDTO){
        transaction {
            Tokens.deleteWhere { token.eq(tokenDTO.token) }
        }
    }
}
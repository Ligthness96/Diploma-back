package tihonin.sergey.utils

import tihonin.sergey.databasemodels.token.Tokens

object  TokenCheck {
    fun isTokenValid(token: String): Boolean = Tokens.fetchTokens().firstOrNull { it.token == token } != null

}
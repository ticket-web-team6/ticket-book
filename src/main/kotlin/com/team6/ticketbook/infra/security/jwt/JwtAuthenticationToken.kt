package com.team6.ticketbook.infra.security.jwt

import com.team6.ticketbook.infra.security.MemberPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import java.io.Serializable

class JwtAuthenticationToken(
    private val principal: MemberPrincipal,
    details: WebAuthenticationDetails,
) : AbstractAuthenticationToken(principal.authorities), Serializable {
    init {
        super.setAuthenticated(true)
        super.setDetails(details)
    }

    override fun getPrincipal() = principal
    override fun getCredentials() = null
    override fun isAuthenticated() = true
}
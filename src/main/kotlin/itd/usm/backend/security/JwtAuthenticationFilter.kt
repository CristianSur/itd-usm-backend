package itd.usm.backend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,

    ) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
//        val HEADER_STRING: String = "Authorization"
//        val lengthOfBearerWithSpace: Int = 7
//
//        val authHeader = request.getHeader(HEADER_STRING)
//
//        if (authHeader == null || !authHeader.startsWith("Bearer")) {
//            filterChain.doFilter(request, response)
//            return
//        }
//
//        val jwToken = authHeader.substring(lengthOfBearerWithSpace)
//        val username = jwtUtil.extractUsername(jwToken)
//
//        if (username != null && SecurityContextHolder.getContext().authentication == null) {
//            val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
//
//            if (jwtUtil.isTokenValid(jwToken, userDetails)) {
//                val authenticationToken = UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.authorities
//                )
//                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
//                SecurityContextHolder.getContext().authentication = authenticationToken
//            }
//        }
//        filterChain.doFilter(request, response)

        var username: String? = null
        var jwt: String? = null
//        if (Objects.nonNull(request.cookies) {
////            jwt = Stream.of(request.cookies)
////                .filter { cookie -> cookie.getName().equals("token") }
////                .findFirst()
////                .map(Cookie::getValue)
////                .orElse(null)
//
////                val jwt2 = request.cookies.filter { it.name == "Bearer" }
//        }
//
        if (request.cookies != null) {
            jwt = request.cookies.filter { it.name == "Bearer" }.first().value
        }
//        if (Objects.isNull(jwt)) {
//            val authHeader = request.getHeader("Authorization")
//            if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
//                jwt = authHeader.replaceFirst("Bearer ".toRegex(), "")
//            }
//        }
        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt)
            } catch (ex: Exception) {
//                md.fin.data.business.filters.JwtRequestFilter.log.warn(ex.message)
            }
//            md.fin.data.business.filters.JwtRequestFilter.log.info("jwt: $jwt")
//            md.fin.data.business.filters.JwtRequestFilter.log.info("username: $username")
        }
        if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().authentication)) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }
}
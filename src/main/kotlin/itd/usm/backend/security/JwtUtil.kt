package itd.usm.backend.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import kotlin.collections.HashMap


@Service
class JwtUtil {

    /**
     * hardcoded 512-bit hex formatted secret key (generated on https://allkeysgenerator.com/random/security-encryption-key-generator.aspx)
     */
    private val SECRET_KEY = "472D4B6150645367566B5970337336763979244226452948404D6251655468576D" +
            "5A7134743777217A25432A462D4A614E645266556A586E3272357538782F41"

    private val expirationTime = (1000 * 60 * 24).toLong()

    fun extractUsername(token: String?): String {
        return extractAnyClaim(token, Claims::getSubject)
    }

    fun <T> extractAnyClaim(token: String?, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    fun generateToken(extraClaims: Map<String?, Any?>?, userDetails: UserDetails): String? {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun extractAllClaimsFromToken(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token) //json web signature
            .body
    }

    fun generateToken(userDetails: UserDetails): String? {
        return generateToken(HashMap(), userDetails)
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String?): Date {
        return extractAnyClaim(token, Claims::getExpiration)
    }

    private fun getSignInKey(): Key? {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
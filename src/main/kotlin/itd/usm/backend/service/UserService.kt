package itd.usm.backend.service

import itd.usm.backend.security.JwtUtil
import itd.usm.backend.model.auth.AuthenticationRequest
import itd.usm.backend.model.auth.AuthenticationResponse
import itd.usm.backend.model.auth.RegisterRequest
import itd.usm.backend.entity.UserEntity
import itd.usm.backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager
) {
    fun register(registerRequest: RegisterRequest): AuthenticationResponse {

        val user = UserEntity(
            0,
            registerRequest.firstname,
            registerRequest.lastname,
            registerRequest.email,
            registerRequest.username,
            passwordEncoder.encode(registerRequest.password)
        )

        userRepository.save(user)

        val token = jwtUtil.generateToken(user)

        return AuthenticationResponse(token!!)
    }

    fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse {

        this.authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        val user = this.userRepository.findByUsername(authenticationRequest.username)

        return AuthenticationResponse(jwtUtil.generateToken(user)!!)
    }
}
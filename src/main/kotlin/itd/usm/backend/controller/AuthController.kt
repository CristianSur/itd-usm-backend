package itd.usm.backend.controller

import itd.usm.backend.model.auth.AuthenticationRequest
import itd.usm.backend.model.auth.AuthenticationResponse
import itd.usm.backend.model.auth.RegisterRequest
import itd.usm.backend.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.MessageSource
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val mailSender: JavaMailSender,
    private val messages: MessageSource
) {

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        val recipientAddress: String = registerRequest.email
        val subject = "Registration Confirmation"
        val confirmationUrl: String = "http://localhost:8080" + "/regitrationConfirm?token=" + "134514512"
//        val message: String = messages.getMessage("message.regSucc", null, Locale.getDefault())

        val email = SimpleMailMessage()
        email.setTo(recipientAddress)
        email.subject = subject
        email.text = "\r\nBuna ziua,\n\t\tDumneavoastra a-ti fost inregistrat in sistema!\n\t\t\tusername:${registerRequest.username}"

        mailSender.send(email)
        return ResponseEntity.ok(userService.register(registerRequest))
    }

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody authenticationRequest: AuthenticationRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthenticationResponse> {
        val response = userService.authenticate(authenticationRequest)


        val cookie = Cookie("Bearer", response.token)
        cookie.secure = true
        cookie.isHttpOnly = true
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok(response)
    }
}
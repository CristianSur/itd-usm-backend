package itd.usm.backend

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


//@SpringBootTest
class ItdUsmBackendApplicationTests {

    @Test
    fun contextLoads() {
        val encoder = BCryptPasswordEncoder(16)
        val result = encoder.encode("myPassword")
        println(result)
        assertTrue(encoder.matches("myPassword", result))
    }

}

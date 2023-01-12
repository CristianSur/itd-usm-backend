package itd.usm.backend.model.auth

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
data class AuthenticationRequest(
    val username: String,
    val password: String
)

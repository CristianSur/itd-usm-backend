package itd.usm.backend.model.auth

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
data class AuthenticationResponse(
    val token: String
)

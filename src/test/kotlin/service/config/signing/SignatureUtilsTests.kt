package service.config.signing

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SignatureUtilsTests {

    private val properties = SigningProperties(password = "fksjdlkjfls dfjsdlkfjsdlk jfsdlfkj sldkjfls".toCharArray())
    private val cut = SignatureUtils(properties)

    @Test
    fun `signatures are always the same for the same input values`() {
        val values = mapOf("foo" to "bar", "bar" to "lorem")

        val signature = cut.getSignature(values)
        repeat(10) {
            assertThat(cut.getSignature(values)).isEqualTo(signature)
        }
    }
}

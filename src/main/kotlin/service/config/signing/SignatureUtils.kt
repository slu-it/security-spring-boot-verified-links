package service.config.signing

import org.springframework.stereotype.Component
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64.Default.decode
import kotlin.io.encoding.Base64.Default.encode
import kotlin.io.encoding.ExperimentalEncodingApi

@Component
@OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
class SignatureUtils(
    properties: SigningProperties
) {

    private val secretKey = SecretKeySpec(properties.password.toString().toByteArray(), "AES")
    private val ivParameterSpec = IvParameterSpec(ByteArray(16))

    fun getSignature(values: Map<String, String>): String {
        val hash = deterministicHash(values)

        val encrypted = encrypt(hash)
        return encode(encrypted)
    }

    fun verifySignature(signature: String, values: Map<String, String>): Boolean {
        val expectedHash = deterministicHash(values)

        val decoded = decode(signature)
        val decrypted = decrypt(decoded)

        return decrypted.contentEquals(expectedHash)
    }

    private fun deterministicHash(values: Map<String, String>): ByteArray {
        val deterministicString = values.keys.sorted()
            .joinToString(separator = "|#|") { key ->
                val value = values.getValue(key)
                "$key=$value"
            }
        return hash(deterministicString)
    }

    private fun hash(value: String): ByteArray =
        MessageDigest.getInstance("SHA-256")
            .digest(value.toByteArray())

    private fun encrypt(data: ByteArray): ByteArray =
        Cipher.getInstance("AES/CBC/PKCS5Padding")
            .apply { init(ENCRYPT_MODE, secretKey, ivParameterSpec) }
            .doFinal(data)

    private fun decrypt(encryptedData: ByteArray): ByteArray =
        Cipher.getInstance("AES/CBC/PKCS5Padding")
            .apply { init(DECRYPT_MODE, secretKey, ivParameterSpec) }
            .doFinal(encryptedData)
}

package me.pablorincon.proyecto_app.domain

// Reglas de validación del registro, en el mismo orden que el mockup:
// se devuelve solo el primer error encontrado.
object AuthValidator {

    enum class RegisterError { NameTooShort, InvalidEmail, PasswordTooShort }

    private const val MIN_NAME_LENGTH = 2
    private const val MIN_PASSWORD_LENGTH = 6

    // Igual que /^\S+@\S+\.\S+$/ del mockup: algo@algo.algo, sin espacios.
    private val EmailRegex = Regex("""^\S+@\S+\.\S+$""")

    // El nombre y el correo deben llegar ya limpios (ver normalizeName / normalizeEmail).
    fun validateRegister(name: String, email: String, password: String): RegisterError? = when {
        name.length < MIN_NAME_LENGTH -> RegisterError.NameTooShort
        !EmailRegex.matches(email) -> RegisterError.InvalidEmail
        password.length < MIN_PASSWORD_LENGTH -> RegisterError.PasswordTooShort
        else -> null
    }

    fun normalizeName(name: String) = name.trim()

    // El correo se guarda sin espacios y en minúsculas, como en el mockup.
    fun normalizeEmail(email: String) = email.trim().lowercase()
}

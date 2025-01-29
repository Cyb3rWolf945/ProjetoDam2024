import android.content.Context
import android.content.SharedPreferences

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USERID = "userid"
    private const val KEY_NOME = "nome"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(userid: Int, nome: String) {
        preferences.edit().apply {
            putInt(KEY_USERID, userid)
            putString(KEY_NOME, nome)
            apply()
        }
    }

    fun getUser(): Pair<Int, String>? {
        val userid = preferences.getInt(KEY_USERID, -1)
        val nome = preferences.getString(KEY_NOME, null)

        return if (userid != -1 && nome != null) {
            Pair(userid, nome)
        } else {
            null
        }
    }

    fun clearUser() {
        preferences.edit().clear().apply()
    }
}

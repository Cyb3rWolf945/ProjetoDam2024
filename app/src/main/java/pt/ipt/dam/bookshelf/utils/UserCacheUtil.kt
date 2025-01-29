package pt.ipt.dam.bookshelf.utils

import android.content.Context
import pt.ipt.dam.bookshelf.models.UserLogin
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object UserCacheUtil {
    private const val FILE_NAME = "user_data.ser"

    // Salvar o utilizador em cache
    fun saveUserToCache(context: Context, user: UserLogin) {
        val directory: File = context.cacheDir
        val file: File = File(directory, FILE_NAME)

        try {
            val fileOutputStream = FileOutputStream(file)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(user)  // Save the User object
            objectOutputStream.close()
            fileOutputStream.close()

            println("Utilizador salvo")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error saving user data")
        }
    }

    // Ler utilizador do ficheiro em cache
    fun readUserFromCache(context: Context): UserLogin? {
        val directory: File = context.cacheDir
        val file: File = File(directory, FILE_NAME)

        try {
            val fileInputStream = FileInputStream(file)
            val objectInputStream = ObjectInputStream(fileInputStream)
            val user = objectInputStream.readObject() as UserLogin
            objectInputStream.close()
            fileInputStream.close()
            return user
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error reading user data")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            println("Error: User class not found")
        }

        return null
    }
}
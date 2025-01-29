package pt.ipt.dam.bookshelf.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import pt.ipt.dam.bookshelf.R

object ToastUtils {
    fun showCustomToast(context: Context, message: String) {
        val layoutInflater = LayoutInflater.from(context)
        val layout = layoutInflater.inflate(R.layout.toast_layout, null)

        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)

        toastText.text = message


        toastIcon.setImageResource(R.drawable.logo)

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}

package com.ceppc.desktop

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private val navy = Color.rgb(13, 28, 54)
    private val blue = Color.rgb(45, 105, 210)
    private val white = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = navy
        window.navigationBarColor = navy

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(navy)
        }

        val desktop = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 24, 24, 16)
        }

        val heading = TextView(this).apply {
            text = "CepPC"
            textSize = 34f
            setTextColor(white)
            setTypeface(null, Typeface.BOLD)
        }

        val subtitle = TextView(this).apply {
            text = "Masaüstüne hoş geldin"
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 4, 0, 28)
        }

        desktop.addView(heading)
        desktop.addView(subtitle)

        val row1 = LinearLayout(this)
        val row2 = LinearLayout(this)

        addTile(row1, "🌐", "İnternet", "Web sitelerini aç", ::openBrowser)
        addTile(row1, "📁", "Dosyalar", "Dosyalarını görüntüle", ::openFiles)
        addTile(row2, "⚙️", "Ayarlar", "Tablet ayarları", ::openSettings)
        addTile(row2, "💻", "Bilgisayarım", "CepPC hakkında", ::showAbout)

        desktop.addView(row1)
        desktop.addView(row2)

        root.addView(
            desktop,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        val taskbar = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL
            orientation = LinearLayout.HORIZONTAL
            setPadding(12, 10, 12, 10)
            background = rounded(blue, 24)
        }

        val start = TextView(this).apply {
            text = "⊞ Başlat"
            textSize = 17f
            setTextColor(white)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(18, 12, 18, 12)
            setOnClickListener { showStartMenu() }
        }

        val taskTitle = TextView(this).apply {
            text = "   CepPC"
            textSize = 16f
            setTextColor(white)
        }

        taskbar.addView(start)
        taskbar.addView(taskTitle)

        root.addView(
            taskbar,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(12, 0, 12, 12)
            }
        )

        setContentView(root)
    }

    private fun addTile(
        row: LinearLayout,
        icon: String,
        title: String,
        description: String,
        action: () -> Unit
    ) {
        val tile = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(10, 16, 10, 16)
            background = rounded(Color.rgb(36, 57, 88), 20)
            setOnClickListener { action() }
        }

        val iconView = TextView(this).apply {
            text = icon
            textSize = 30f
        }

        val titleView = TextView(this).apply {
            text = title
            textSize = 17f
            setTextColor(white)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val descView = TextView(this).apply {
            text = description
            textSize = 11f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        tile.addView(iconView)
        tile.addView(titleView)
        tile.addView(descView)

        val params = LinearLayout.LayoutParams(0, 125, 1f)
        params.setMargins(7, 7, 7, 7)
        row.addView(tile, params)
    }

    private fun rounded(color: Int, radius: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }
    }

    private fun openBrowser() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Tarayıcı bulunamadı", Toast.LENGTH_LONG).show()
        }
    }

    private fun openFiles() {
        try {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Dosya yöneticisi açılamadı", Toast.LENGTH_LONG).show()
        }
    }

    private fun openSettings() {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    private fun showAbout() {
        AlertDialog.Builder(this)
            .setTitle("CepPC")
            .setMessage("CepPC masaüstü başlangıç sürümü.\n\nSamsung Galaxy Tab için hazırlanıyor.")
            .setPositiveButton("Tamam", null)
            .show()
    }

    private fun showStartMenu() {
        val items = arrayOf(
            "İnternet",
            "Dosyalar",
            "Ayarlar",
            "Hakkında"
        )

        AlertDialog.Builder(this)
            .setTitle("Başlat")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> openBrowser()
                    1 -> openFiles()
                    2 -> openSettings()
                    3 -> showAbout()
                }
            }
            .show()
    }
}

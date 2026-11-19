
package com.ceppc.desktop

import android.app.*
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.content.Intent
import android.net.Uri
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*

class MainActivity : Activity() {

    private val blue = Color.rgb(20, 45, 85)
    private val panel = Color.rgb(32, 48, 72)
    private lateinit var desktop: FrameLayout
    private var windowCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = blue
        window.navigationBarColor = blue

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(blue)

        desktop = FrameLayout(this)
        desktop.setBackgroundColor(blue)

        val wallpaper = TextView(this).apply {
            text = "CepPC\n\nMasaüstüne hoş geldin"
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        desktop.addView(wallpaper, FrameLayout.LayoutParams(-1, -1))

        val icons = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 12, 12, 12)
        }

        addIcon(icons, "🌐 İnternet") { openBrowser() }
        addIcon(icons, "📁 Dosyalar") { openFiles() }
        addIcon(icons, "⚙️ Ayarlar") { openSettings() }
        addIcon(icons, "💻 Bilgisayarım") { openComputer() }

        desktop.addView(icons)

        root.addView(desktop, LinearLayout.LayoutParams(-1, 0, 1f))

        val bar = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(Color.rgb(15, 25, 43))
            setPadding(12, 8, 12, 8)
        }

        val start = TextView(this).apply {
            text = "⊞ Başlat"
            textSize = 18f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            setPadding(18, 10, 18, 10)
            setOnClickListener { showStart() }
        }

        val clock = TextView(this).apply {
            text = "  CepPC"
            textSize = 16f
            setTextColor(Color.WHITE)
        }

        bar.addView(start)
        bar.addView(clock)
        root.addView(bar)

        setContentView(root)
    }

    private fun addIcon(list: LinearLayout, name: String, action: () -> Unit) {
        val item = TextView(this).apply {
            text = name
            textSize = 17f
            setTextColor(Color.WHITE)
            setPadding(12, 14, 12, 14)
            setOnClickListener { action() }
        }
        list.addView(item)
    }

    private fun showStart() {
        val menu = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
            background = rounded(panel, 22)
        }

        val title = TextView(this).apply {
            text = "Başlat"
            textSize = 25f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }
        menu.addView(title)

        addIcon(menu, "🌐 İnternet") { openBrowser() }
        addIcon(menu, "📁 Dosyalar") { openFiles() }
        addIcon(menu, "⚙️ Ayarlar") { openSettings() }
        addIcon(menu, "💻 Bilgisayarım") { openComputer() }

        val popup = PopupWindow(menu, 280, -2, true)
        popup.isOutsideTouchable = true
        popup.showAtLocation(desktop, Gravity.BOTTOM or Gravity.START, 12, 65)
    }

    private fun rounded(color: Int, radius: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }
    }

    private fun createWindow(title: String, content: View) {
        windowCount++

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = rounded(Color.rgb(235, 240, 248), 14)
            elevation = 18f
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(12, 8, 8, 8)
            setBackgroundColor(Color.rgb(25, 55, 100))
        }

        val label = TextView(this).apply {
            text = title
            textSize = 16f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val close = TextView(this).apply {
            text = " ✕ "
            textSize = 18f
            setTextColor(Color.WHITE)
            setOnClickListener { desktop.removeView(box) }
        }

        header.addView(label, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(close)

        box.addView(header)
        box.addView(content, LinearLayout.LayoutParams(-1, 0, 1f))

        val params = FrameLayout.LayoutParams(650, 480)
        params.gravity = Gravity.CENTER
        desktop.addView(box, params)

        var startX = 0f
        var startY = 0f

        header.setOnTouchListener { _, event ->
            val p = box.layoutParams as FrameLayout.LayoutParams
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    startY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    p.leftMargin += (event.rawX - startX).toInt()
                    p.topMargin += (event.rawY - startY).toInt()
                    p.gravity = Gravity.TOP or Gravity.START
                    startX = event.rawX
                    startY = event.rawY
                    box.layoutParams = p
                    true
                }
                else -> false
            }
        }
    }

    private fun openBrowser() {
        val web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.webViewClient = WebViewClient()
        web.loadUrl("https://www.google.com")

        createWindow("İnternet", web)
    }

    private fun openFiles() {
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        val text = TextView(this).apply {
            text = "Dosyalarını açmak için aşağıya dokun."
            textSize = 18f
            setTextColor(Color.BLACK)
        }

        val button = Button(this).apply {
            text = "Dosya seç"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivity(intent)
            }
        }

        content.addView(text)
        content.addView(button)
        createWindow("Dosya yöneticisi", content)
    }

    private fun openSettings() {
        startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
    }

    private fun openComputer() {
        val text = TextView(this).apply {
            text = "CepPC bilgisayarım\n\nAndroid cihaz depolaması\n\nWindows EXE desteği henüz eklenmedi."
            textSize = 19f
            setTextColor(Color.BLACK)
            setPadding(20, 20, 20, 20)
        }
        createWindow("Bilgisayarım", text)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("CepPC")
            .setMessage("Masaüstünden çıkmak istiyor musun?")
            .setNegativeButton("Hayır", null)
            .setPositiveButton("Çıkış") { _, _ -> finish() }
            .show()
    }
}

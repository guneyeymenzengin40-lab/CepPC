
private fun openFiles() {
    val content = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(20, 20, 20, 20)
    }

    val text = TextView(this).apply {
        text = "Dosyalar ve Windows programları"
        textSize = 18f
        setTextColor(Color.BLACK)
    }

    val fileButton = Button(this).apply {
        text = "Dosya seç"
        setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 100)
        }
    }

    val exeButton = Button(this).apply {
        text = "Windows EXE seç"
        setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 101)
        }
    }

    content.addView(text)
    content.addView(fileButton)
    content.addView(exeButton)

    createWindow("Dosya yöneticisi", content)
}

override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode == RESULT_OK && data?.data != null) {
        val uri = data.data!!

        if (requestCode == 101) {
            Toast.makeText(
                this,
                "EXE seçildi. Çalıştırmak için Wine/Box64 motoru gerekiyor.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                "Dosya seçildi: $uri",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

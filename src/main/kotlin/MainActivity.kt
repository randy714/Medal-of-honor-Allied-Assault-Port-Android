package com.mohaa.launcher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject

class MainActivity : Activity() {
    private lateinit var wv: WebView
    private val engine = "org.openmoh.openmohaa"
    private val prefs by lazy { getSharedPreferences("mohaa", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK
        wv = WebView(this)
        wv.setBackgroundColor(Color.BLACK)
        wv.settings.javaScriptEnabled = true
        wv.settings.domStorageEnabled = true
        wv.addJavascriptInterface(Bridge(), "Android")
        setContentView(wv)
        wv.loadUrl("file:///android_asset/index.html")
    }

    private fun kids(): List<Pair<String, String>> {
        val s = prefs.getString("u", null) ?: return emptyList()
        val u = Uri.parse(s)
        val parent = DocumentsContract.buildChildDocumentsUriUsingTree(u, DocumentsContract.getTreeDocumentId(u))
        val out = ArrayList<Pair<String, String>>()
        try {
            contentResolver.query(parent, arrayOf(Document.COLUMN_DISPLAY_NAME, Document.COLUMN_DOCUMENT_ID), null, null, null)?.use {
                while (it.moveToNext()) out.add(it.getString(0) to it.getString(1))
            }
        } catch (e: Exception) {}
        return out
    }

    private fun info(): String {
        val s = prefs.getString("u", null) ?: return ""
        val path = Uri.decode(DocumentsContract.getTreeDocumentId(Uri.parse(s)))
        return path + "|" + kids().count { it.first.endsWith(".pk3", true) }
    }

    private fun push() {
        val p = info().split("|")
        if (p.size == 2) wv.post { wv.evaluateJavascript("onFolder(${JSONObject.quote(p[0])},${p[1]})", null) }
    }

    override fun onActivityResult(r: Int, c: Int, d: Intent?) {
        super.onActivityResult(r, c, d)
        val u = d?.data
        if (r == 1 && c == RESULT_OK && u != null) {
            contentResolver.takePersistableUriPermission(u, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            prefs.edit().putString("u", u.toString()).apply()
            push()
        }
    }

    inner class Bridge {
        @JavascriptInterface fun pickFolder() {
            runOnUiThread {
                startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION), 1)
            }
        }
        @JavascriptInterface fun getFolder(): String = info()
        @JavascriptInterface fun writeCfg(cfg: String): String {
            val s = prefs.getString("u", null) ?: return "nofold"
            return try {
                val u = Uri.parse(s)
                val parent = DocumentsContract.buildDocumentUriUsingTree(u, DocumentsContract.getTreeDocumentId(u))
                kids().filter { it.first == "autoexec.cfg" }.forEach {
                    try { DocumentsContract.deleteDocument(contentResolver, DocumentsContract.buildDocumentUriUsingTree(u, it.second)) } catch (e: Exception) {}
                }
                val f = DocumentsContract.createDocument(contentResolver, parent, "application/octet-stream", "autoexec.cfg") ?: return "err"
                contentResolver.openOutputStream(f)?.use { it.write(cfg.toByteArray()) }
                "ok"
            } catch (e: Exception) { "err" }
        }
        @JavascriptInterface fun launch(cfg: String): String {
            writeCfg(cfg)
            val i = packageManager.getLaunchIntentForPackage(engine) ?: return "nopkg"
            runOnUiThread { startActivity(i) }
            return "ok"
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { if (wv.canGoBack()) wv.goBack() else super.onBackPressed() }
}

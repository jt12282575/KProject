package dada.com.kproject.ui.song

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dada.com.kproject.R
import dada.com.kproject.const.BundleKey.Companion.ARG_URL
import dada.com.kproject.const.SchemeConst.Companion.HTTPS_SCHEME
import dada.com.kproject.const.SchemeConst.Companion.KKBOX_SCHEME
import dada.com.kproject.util.SchemeUtil.Companion.getMarketSearchHttpsUrl
import dada.com.kproject.util.SchemeUtil.Companion.getMarketSearchUri
import dada.com.kproject.util.logi
import kotlinx.android.synthetic.main.activity_song_page.*


class SongPageActivity : AppCompatActivity() {
    private var songPageUrl: String = ""
    private var hasError = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_page)
        init()

        if (songPageUrl.isEmpty()) {
            Toast.makeText(this, getString(R.string.api_error), Toast.LENGTH_LONG).show()
            finish()
        }
        asp_retry_button.setOnClickListener {
            asp_web_song.loadUrl(songPageUrl)
        }

        asp_web_song.loadUrl(songPageUrl)
        asp_web_song.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request != null && request.url != null) {
                    loadUrl(request)
                }

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                hasError = false
                asp_progress_bar.isVisible = true
                asp_retry_button.isVisible = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (hasError){
                    asp_web_song.isVisible = false
                    asp_progress_bar.isVisible = false
                    asp_retry_button.isVisible = true
                }else{
                    asp_web_song.isVisible = true
                    asp_progress_bar.isVisible = false
                    asp_retry_button.isVisible = false
                }

            }



            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                hasError = true

            }
        }


    }

    private fun loadUrl(request: WebResourceRequest?) {
        when (request?.url?.scheme) {
            HTTPS_SCHEME -> {
                asp_web_song?.loadUrl(request?.url.toString())
            }
            KKBOX_SCHEME -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request!!.url!!.toString()))
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

                    this@SongPageActivity.startActivity(intent)
                } catch (e: Exception) {
                    launchInPlayStore(request!!.url!!.scheme!!)
                }
            }

        }
    }

    private fun launchInPlayStore(scheme: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getMarketSearchUri(scheme))
                )
            )
        } catch (e: ActivityNotFoundException) {
            val url = getMarketSearchHttpsUrl(scheme)
            asp_web_song.loadUrl(url)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KEYCODE_BACK && asp_web_song.canGoBack()) {
            asp_web_song.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun init() {
        songPageUrl = intent.getStringExtra(ARG_URL)
    }
}
package jp.ac.nitech.club.c0de.myfirstvoiceapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import jp.ac.nitech.club.c0de.myfirstvoiceapp.R
import jp.ac.nitech.club.c0de.myfirstvoiceapp.search.Place
import jp.ac.nitech.club.c0de.myfirstvoiceapp.search.Search
import jp.ac.nitech.club.c0de.myfirstvoiceapp.search.Synonym
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_PERMISSION = 100
    }

    lateinit var tts: TextToSpeech
    lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this) { status ->
            when (status) {
                TextToSpeech.SUCCESS -> {
                    tts.setSpeechRate(1.0f)
                    //todo 本来ならフラグ管理すべき
                    val message = """
                        |これらの場所間の経路が調べられます
                        |・ロサンゼルス空港
                        |　[LA空港/LAX]
                        |・カルフォルニア大学ロサンゼルス校
                        |　[UCLA/カルフォルニア大学]
                        |・ハリウッド
                        |・アナハイム
                        |・グランドキャニオン
                        |・サンディエゴ
                        |・ダウンタウン
                        |・パサデナ
                        |・ディズニーランド
                        |・ラスベガス
                  """.trimMargin()
                    postBotMessage(message, "これらの場所間の経路が調べられます")
                }
                else -> {
                    Toast.makeText(this, "読み上げ機能が使えません", Toast.LENGTH_LONG).show()
                }
            }
        }

        adapter = RecyclerAdapter(this, mutableListOf())
        rv_chat.adapter = adapter
        rv_chat.layoutManager = LinearLayoutManager(this)

        val permission = Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // 権限なし
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // 一度拒否されている
                Toast.makeText(this, "音声入力のために権限が必要です", Toast.LENGTH_SHORT).show()
            } else {
                //
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_PERMISSION)
            }
        } else {
            // 権限あり
            init()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 許可された
                    init()
                } else {
                    // 拒否された
                    Toast.makeText(this, "権限が必要です", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 音声認識の準備
     */
    private fun init() {
        bt_mic.isEnabled = true
        bt_mic.setOnClickListener {
            if (tts.isSpeaking) {
                tts.stop()
            }

            bt_mic.background = resources.getDrawable(R.drawable.mic_background_accent)
            bt_mic.isEnabled = false

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, applicationContext.packageName)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "音声入力")
            }
            val recognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onRmsChanged(rmsDb: Float) {
                    //TODO("not implemented")
                }

                override fun onBufferReceived(buffer: ByteArray) {
                    //TODO("not implemented")
                }

                override fun onEvent(eventType: Int, params: Bundle) {
                    //TODO("not implemented")
                }

                override fun onReadyForSpeech(params: Bundle) {
                    //TODO("not implemented")
                }

                override fun onBeginningOfSpeech() {
                    //postUserMessage("")
                }

                override fun onEndOfSpeech() {
                    bt_mic.background = resources.getDrawable(R.drawable.mic_background)
                    bt_mic.isEnabled = true
                }

                override fun onPartialResults(partialResults: Bundle) {
                    //TODO("not implemented")
                }

                override fun onResults(results: Bundle) {
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let {
                        handleResult(it)
                    }
                }

                override fun onError(error: Int) {
                    val messagePrint: String
                    val messageSpeak: String
                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> {
                            messagePrint = "録音中にエラーが発生しました．"
                            messageSpeak = "録音中にエラーが発生しました。"
                        }
                        SpeechRecognizer.ERROR_CLIENT -> {
                            messagePrint = "端末でエラーが発生しました．"
                            messageSpeak = "端末でエラーが発生しました。"
                        }
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                            messagePrint = "必要な権限がありません．"
                            messageSpeak = "必要な権限がありません。"
                        }
                        SpeechRecognizer.ERROR_NETWORK -> {
                            messagePrint = "通信中にエラーが発生しました．"
                            messageSpeak = "通信中にエラーが発生しました。"
                        }
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> {
                            messagePrint = "通信がタイムアウトしました．"
                            messageSpeak = "通信がタイムアウトしました。"
                        }
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            messagePrint = "何と言ったか分かりませんでした．"
                            messageSpeak = "何と言ったか分かりませんでした。"
                        }
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                            messagePrint = "音声認識に高負荷がかかっています．"
                            messageSpeak = "音声認識に高負荷がかかっています。"
                        }
                        SpeechRecognizer.ERROR_SERVER -> {
                            messagePrint = "サーバーでエラーが発生しました．"
                            messageSpeak = "サーバーでエラーが発生しました。"
                        }
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                            messagePrint = "タイムアウトしました．"
                            messageSpeak = "タイムアウトしました。"
                        }
                        else -> {
                            messagePrint = "不明なエラーが発生しました．"
                            messageSpeak = "不明なエラーが発生しました。"
                        }
                    }
                    postBotMessage(messagePrint, messageSpeak)
                }
            })
            recognizer.startListening(intent)
        }
    }

    /**
     * userとしてタイムラインへ投稿
     * @param message 表示する文字列
     */
    private fun postUserMessage(message: String) {
        adapter.data.add(ChatItem(true, message))
        adapter.notifyItemInserted(adapter.data.size - 1)
        //rv_chat.smoothScrollToPosition(adapter.itemCount-1) // 干渉してるっぱい
    }

    /**
     * botとしてタイムラインへ投稿
     * @param messagePrint 表示する文字列
     * @param messageSpeak 読み上げる文字列
     */
    private fun postBotMessage(messagePrint: String, messageSpeak: String) {
        tts.speak(messageSpeak, TextToSpeech.QUEUE_FLUSH, null)
        adapter.data.add(ChatItem(false, messagePrint))
        adapter.notifyItemInserted(adapter.data.size-1)
        rv_chat.smoothScrollToPosition(adapter.itemCount-1)
    }

    /**
     * 音声認識の結果を処理
     * @param candidacy 候補
     */
    private fun handleResult(candidacy: ArrayList<String>) {
        val regex = kotlin.run {
            val sub = Synonym.map.entries.joinToString("|", "(", ")") { it.key }
            """$sub.*$sub""".toRegex(RegexOption.IGNORE_CASE)
        }

        var from: Place? = null
        var to: Place? = null
        for (txt in candidacy) {
            val result = regex.find(txt)
            if (result != null) {
                from = Synonym.map[result.groupValues[1].toUpperCase()]
                to = Synonym.map[result.groupValues[2].toUpperCase()]

                postUserMessage(txt.toUpperCase())
                break
            }
        }

        if (from == null || to == null) {
            postUserMessage(candidacy[0])
            val message = "判断できませんでした．"
            postBotMessage(message, message)
            return
        }

        if (from == to) {
            val message = "出発地点と目的地点が同一です．"
            postBotMessage(message, message)
            return
        }

        thread {
            val result = Search.search(from, to)
            val messagePrint: String
            val messageSpeak: String
            if (result.found) {
                val link = LinkedList<String>()
                var trace = result.node
                while (trace != null) {
                    link.addFirst(trace.place.toString())
                    trace = trace.parent
                }

                if (link.size <= 2) {
                    messagePrint = "${link.first}から${link.last}まで直接行くことができます．\nコストは${result.node!!.getPathCost()}です．"
                    messageSpeak = "${link.first}から${link.last}まで直接行くことができます。コストは${result.node!!.getPathCost()}です。"
                } else {
                    val start = link.removeFirst()
                    val goal = link.removeLast()

                    messagePrint = "${start}から${goal}までは\n${link.joinToString(",\n") { it -> "    $it" } }\nを経由して行くことができます．\nコストは合計で${result.node!!.getPathCost()}です．"
                    messageSpeak = "${start}から${goal}までは${link.joinToString(",")}を経由して行くことができます。コストは合計で${result.node!!.getPathCost()}です。"
                }
            } else {
                messagePrint = "経路が見つかりませんでした．"
                messageSpeak = "経路が見つかりませんでした．"
            }

            runOnUiThread {
                postBotMessage(messagePrint, messageSpeak)
            }
        }
    }
}

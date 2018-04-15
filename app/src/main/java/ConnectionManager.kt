import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import infinity.to.loop.supermarket.list.MessageListener
import infinity.to.loop.supermarket.models.Message
import okhttp3.*
import okio.ByteString

object ConnectionManager : WebSocketListener() {

    private val TAG: String = ConnectionManager.javaClass.simpleName
    private val client = OkHttpClient()
    private val gson: Gson = GsonBuilder().create()
    private var connection: WebSocket? = null
    private lateinit var listener: MessageListener
    private var alive: Boolean = false

    fun establishConnection(url: String, messageListener: MessageListener) {
        val request = Request.Builder().url(url).build()
        this.connection = client.newWebSocket(request, this)
        this.listener = messageListener
        this.alive = true
    }

    fun close() {
        connection?.let {
            it.cancel()
            it.close(1000, null)
            alive = false
        }
    }

    fun isAlive() = alive

    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        Log.d(TAG, "Connection successful ${response?.isSuccessful}")
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        Log.d(TAG, "OnFailure at: ${t?.message}")
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        if (text != null) {
            val message = gson.fromJson(text, Message::class.java)
            listener.onNewMessage(message)
            Log.d(TAG, "onMessage: $text")
        }
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        Log.d(TAG, "onMessage: $bytes")
    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        Log.d(TAG, "onClosed with code: $code, reason: $reason")
    }
}
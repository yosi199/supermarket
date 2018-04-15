package infinity.to.loop.supermarket

import ConnectionManager
import android.app.Activity
import android.app.ActivityOptions
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.widget.Button
import infinity.to.loop.supermarket.list.ItemChoice
import infinity.to.loop.supermarket.list.MessageAdapter
import infinity.to.loop.supermarket.list.MessageListener
import infinity.to.loop.supermarket.models.Message
import java.lang.ref.WeakReference


class MainViewModel : ViewModel(),
        View.OnClickListener,
        ItemChoice,
        MessageListener {

    private var adapter: MessageAdapter = MessageAdapter(this)
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var activity: WeakReference<Activity>
    private lateinit var connectionBtn: WeakReference<Button>

    companion object {
        val connectionManager = ConnectionManager
    }

    fun getAdapter() = adapter

    fun setActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    fun setConnectionBtn(connectionBtn: Button) {
        this.connectionBtn = WeakReference(connectionBtn)
    }

    override fun onClick(v: View) {
        tryConnection(v)
    }

    override fun onItemChoosen(color: Int) {
        if (connectionManager.isAlive().not()) {
            val options = ActivityOptions.makeSceneTransitionAnimation(activity.get())
            val intent = Intent(activity.get(), Transitioned::class.java)
            intent.putExtra("bgColor", color)
            startActivity(activity.get()!!, intent, options.toBundle())
        }
    }

    private fun tryConnection(v: View) {
        if (connectionManager.isAlive().not()) {
            adapter.allowPress(false)
            val url = v.context?.getString(R.string.socket_address)
            if (url != null) {
                connectionManager.establishConnection(url, this)
            }
        } else {
            adapter.allowPress(true)
            connectionManager.close()
        }

        updateConnectionBtnUi(v)
    }

    private fun updateConnectionBtnUi(v: View) {

        if (connectionBtn.get() == null) {
            return
        }
        val btn = connectionBtn.get()
        if (connectionManager.isAlive()) {
            btn?.text = v.resources.getString(R.string.disconnect)
        } else {
            btn?.text = v.resources.getString(R.string.connect)
        }
    }

    override fun onNewMessage(message: Message) {
        handler.post({ adapter.update(message) })
    }

    fun onDestroyCalled() {
        if (connectionManager.isAlive()) {
            connectionManager.close()
        }
    }


}
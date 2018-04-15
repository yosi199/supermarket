package infinity.to.loop.supermarket

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Explode
import android.transition.Slide
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var connectionBtn: Button? = null
    private var itemsList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        this.viewModel.setActivity(this)

        this.itemsList = findViewById(R.id.items_list)
        this.itemsList?.layoutManager = LinearLayoutManager(this)
        this.itemsList?.adapter = viewModel.getAdapter()

        this.connectionBtn = findViewById(R.id.connectionBtn)
        this.connectionBtn?.setOnClickListener(viewModel)
        this.viewModel.setConnectionBtn(connectionBtn as Button)

        val exitTrans = Explode()
        window.exitTransition = exitTrans

        val reenterTrans = Slide()
        window.reenterTransition = reenterTrans
    }

    override fun onDestroy() {
        viewModel.onDestroyCalled()
        super.onDestroy()

    }

}

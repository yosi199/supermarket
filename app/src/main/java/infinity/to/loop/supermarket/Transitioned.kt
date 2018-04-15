package infinity.to.loop.supermarket

import android.app.Activity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.transition.Explode
import android.transition.Slide


class Transitioned : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transitioned)

        val enterTrans = Explode()
        window.enterTransition = enterTrans

        val returnTrans = Slide()
        window.returnTransition = returnTrans

        val bgColor = intent?.getIntExtra("bgColor", 0)
        if (bgColor != null) {
            val layout = findViewById<ConstraintLayout>(R.id.transitioned_activity)
            layout.setBackgroundColor(bgColor)
        }
    }
}

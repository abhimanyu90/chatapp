package com.example.chattingapp
import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.av.chattingapp.MainActivity


class splash : AppCompatActivity() {
    var logo: ImageView? = null
    var name: TextView? = null
    var own1: TextView? = null
    var own2: TextView? = null
    var topAnim: Animation? = null
    var bottomAnim: Animation? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        logo = findViewById<ImageView>(R.id.logoimg)
        name = findViewById<TextView>(R.id.logonameimg)
        own1 = findViewById<TextView>(R.id.ownone)
        own2 = findViewById<TextView>(R.id.owntwo)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        logo.setAnimation(topAnim)
        name.setAnimation(bottomAnim)
        own1.setAnimation(bottomAnim)
        own2.setAnimation(bottomAnim)

        Handler().postDelayed({
            val intent = Intent(
                this@splash,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 4000)
    }
}
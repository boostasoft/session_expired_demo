package com.boostasoft.session_expired_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.boostasoft.session_expired_demo.databinding.ActivityMyLoginBinding

class MyLoginActivity : AppCompatActivity() {
    private var binding: ActivityMyLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding!!.goToMainActivityBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


}

package com.selim.notesapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.selim.notesapp.databinding.ActivityLoginBinding
import com.selim.notesapp.utils.Constants

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var userId :FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        setup()
    }


    private fun setup() {
        binding.btnLogin.setOnClickListener {
            if (validateInputs()){
                val email =binding.etLoginEmail.text?.trim().toString()
                val password = binding.etLoginPassword.text?.trim().toString()
                firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            userId = firebaseAuth.currentUser!!
                            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity,MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Can't Login", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.tvLoginTextRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun goToRegister() {
        val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun validateInputs() :Boolean{
        if (binding.etLoginEmail.text?.isNotEmpty()!! && binding.etLoginPassword.text?.isNotEmpty()!!){
            return  true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        val from =intent.getIntExtra(Constants.FROM_STRING,0)
        if (from==Constants.FROM_Splash){
            if (firebaseAuth.currentUser != null){
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
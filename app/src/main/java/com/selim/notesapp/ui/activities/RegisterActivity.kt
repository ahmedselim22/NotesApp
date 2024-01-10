package com.selim.notesapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.selim.notesapp.R
import com.selim.notesapp.databinding.ActivityRegisterBinding
import com.selim.notesapp.utils.Constants

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth =FirebaseAuth.getInstance()
        setup()
    }
    private fun setup() {
        binding.btnRegister.setOnClickListener {
            if (validateInputs()){
                val email =binding.etRegisterEmail.text?.trim().toString()
                val password = binding.etRegisterPassword.text?.trim().toString()
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Failed To Register", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.tvRegisterTextLogin.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
        intent.putExtra(Constants.FROM_STRING,Constants.CAME_FROM_REGISTER)
        startActivity(intent)
    }

    private fun validateInputs(): Boolean {
        if (binding.etRegisterName.text?.isNotEmpty()!! && binding.etRegisterEmail.text?.isNotEmpty()!! &&
                binding.etRegisterPassword.text?.isNotEmpty()!!&& binding.etRegisterConfirmPassword.text?.isNotEmpty()!!){
            return true
        }
        return false
    }

}
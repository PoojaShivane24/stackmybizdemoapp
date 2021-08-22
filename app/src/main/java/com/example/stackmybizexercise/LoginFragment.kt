package com.example.stackmybizexercise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.stackmybizexercise.databinding.FragmentRegisterBinding
import com.example.stackmybizexercise.roomdatabase.UserDatabase
import com.example.stackmybizexercise.roomdatabase.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var fragmentLoginRegisterBinding : FragmentRegisterBinding
    val coroutineScope = CoroutineScope(SupervisorJob())

    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLoginRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return fragmentLoginRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        fragmentLoginRegisterBinding.btnLogReg.setOnClickListener {
            loginUser()
        }

        fragmentLoginRegisterBinding.tvLogReg.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

    private fun loginUser() {
        val userName = fragmentLoginRegisterBinding.tvName.text.toString()
        val password = fragmentLoginRegisterBinding.tvPassword.text.toString()

        when {
            userName.isEmpty() -> {
                fragmentLoginRegisterBinding.textInputLayout2.isEndIconVisible = true
                fragmentLoginRegisterBinding.tvName.error = "Enter User Name"
                fragmentLoginRegisterBinding.tvName.requestFocus()
            }
            password.isEmpty() -> {
                fragmentLoginRegisterBinding.textInputLayout2.isEndIconVisible = false
                fragmentLoginRegisterBinding.tvPassword.error = "Enter Password"
                fragmentLoginRegisterBinding.tvPassword.requestFocus()
            }
            else -> {
                fragmentLoginRegisterBinding.textInputLayout2.isEndIconVisible = true
                if (userName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {

                    firebaseLogin(userName, password)

                    val entity = UserEntity(userName, password)
                    coroutineScope.launch {
                        val userInfo =  UserDatabase.getInstance(requireContext()).getUserDetailDao().getUser(userName, password)
                        Log.e("TAG", "onViewCreated: $userInfo")

//                        if (userInfo != null) {
//                            var handle = Handler(Looper.getMainLooper()).post {
//                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
//                                val intent = Intent(context, UserProfileActivity::class.java).apply {
//                                    putExtra("userName", userInfo.userName)
//                                    putExtra("userPassword", userInfo.password)
//                                }
//                                (context as Activity).startActivity(intent)
//                            }
//                        }
                    }
                } else {
                    Toast.makeText(context, "Enter valid Email Id", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun firebaseLogin(userName: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireActivity(), UserProfileActivity::class.java).apply {
                    putExtra("userName", userName)
                    putExtra("userPassword", password)
                }
                requireActivity().startActivity(intent)
            } else {
                Toast.makeText(context, "Login error", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
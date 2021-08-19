package com.example.stackmybizexercise

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.stackmybizexercise.databinding.FragmentRegisterBinding
import com.example.stackmybizexercise.roomdatabase.UserDatabase
import com.example.stackmybizexercise.roomdatabase.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var count: Long = 0L
    lateinit var fragmentLoginRegisterBinding : FragmentRegisterBinding

    val coroutineScope = CoroutineScope(SupervisorJob())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login_register, container, false)


        fragmentLoginRegisterBinding = FragmentRegisterBinding.bind(inflater.inflate(R.layout.fragment_register, container, false))

        initView()
        return fragmentLoginRegisterBinding.root
    }

    private fun initView() {
        fragmentLoginRegisterBinding.textInputLayout3.visibility = View.VISIBLE
        fragmentLoginRegisterBinding.btnLogReg.text = resources.getString(R.string.register)
        fragmentLoginRegisterBinding.tvLogReg.text = resources.getString(R.string.login)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentLoginRegisterBinding.btnLogReg.setOnClickListener {
//            if (checkInput(fragmentLoginRegisterBinding.tvName.text.toString(), fragmentLoginRegisterBinding.tvPassword.text.toString() )
            val userName = fragmentLoginRegisterBinding.tvName.text.toString()
            val password = fragmentLoginRegisterBinding.tvPassword.text.toString()
            val confirmPassword = fragmentLoginRegisterBinding.tvConfirmPassword.text.toString()

            when {
                userName.isEmpty() -> fragmentLoginRegisterBinding.tvName.error = "Enter User Name"
                password.isEmpty() -> fragmentLoginRegisterBinding.tvPassword.error = "Enter Password"
                confirmPassword.isEmpty() -> fragmentLoginRegisterBinding.tvConfirmPassword.error = "Enter Confirm Password"
            }
            if (userName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                if (password == confirmPassword) {
                    val entity = UserEntity(userName, password)
                    coroutineScope.launch {
                        count = UserDatabase.getInstance(requireContext()).getUserDetailDao()
                            .insert(entity)
                        Log.e("TAG", "onViewCreated: " + count)
                        if (count > 0L) {
                            var handle = Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(context, "Enter valid Email Id", Toast.LENGTH_SHORT).show()
            }

        }
        fragmentLoginRegisterBinding.tvLogReg.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun checkInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }


}
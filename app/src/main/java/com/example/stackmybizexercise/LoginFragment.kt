package com.example.stackmybizexercise

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.stackmybizexercise.databinding.FragmentRegisterBinding
import com.example.stackmybizexercise.roomdatabase.UserDatabase
import com.example.stackmybizexercise.roomdatabase.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var fragmentLoginRegisterBinding : FragmentRegisterBinding
    val coroutineScope = CoroutineScope(SupervisorJob())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLoginRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        initView()
        return fragmentLoginRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLoginRegisterBinding.btnLogReg.setOnClickListener {
            val userName = fragmentLoginRegisterBinding.tvName.text.toString()
            val password = fragmentLoginRegisterBinding.tvPassword.text.toString()

            if (userName.isEmpty())
                fragmentLoginRegisterBinding.tvName.error = "Enter User Name"
            else if (password.isEmpty())
                fragmentLoginRegisterBinding.tvPassword.error = "Enter Password"

            if (userName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                val entity = UserEntity(userName, password)
                coroutineScope.launch {
                     val userInfo =  UserDatabase.getInstance(requireContext()).getUserDetailDao().getUser(userName, password)
                    Log.e("TAG", "onViewCreated: "+userInfo )

                    if (userInfo != null) {
                        Runnable {
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                        }
                        val intent = Intent(context, UserProfileActivity::class.java).apply {
                            putExtra("userName", userInfo.userName)
                            putExtra("userPassword", userInfo.password)
                        }
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(context, "Enter valid Email Id", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun initView() {
        val userName = "abc12345@gmail.com"
        val password = "123"
        val entity = UserEntity(userName, password)
        coroutineScope.launch {
            var count =
                context?.let { UserDatabase.getInstance(it).getUserDetailDao().insert(entity) }
            Log.e("TAG", "initView: "+count )
            if (count != null) {
                if (count > 0L) {
                    Runnable {
                        Toast.makeText(context, " inserted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}
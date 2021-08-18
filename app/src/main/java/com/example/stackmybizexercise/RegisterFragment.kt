package com.example.stackmybizexercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stackmybizexercise.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    lateinit var fragmentLoginRegisterBinding : FragmentRegisterBinding

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
        fragmentLoginRegisterBinding.tvConfirmPassword.visibility = View.VISIBLE
        fragmentLoginRegisterBinding.btnLogReg.text = resources.getString(R.string.register)
        fragmentLoginRegisterBinding.tvLogReg.text = resources.getString(R.string.login)


    }


    companion object {

    }
}
package com.udacity.shoestore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)

        configureListeners()

        return binding.root
    }

    private fun configureListeners(){

        binding.loginButton.setOnClickListener { verifyAndNavigateToInstructions(it) }

        binding.registerButton.setOnClickListener { verifyAndNavigateToInstructions(it) }
    }

    private fun verifyAndNavigateToInstructions(view: View) {

        with(binding){

            // validate email and password fields - for now we are only testing for emptiness
            if( emailEditText.text.isNullOrEmpty() || pwdEditText.text.isNullOrEmpty() ) {
                Toast.makeText(activity, getString(R.string.err_email_pwd_required), Toast.LENGTH_LONG).show()
                return
            }

            view.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
        }

    }
}

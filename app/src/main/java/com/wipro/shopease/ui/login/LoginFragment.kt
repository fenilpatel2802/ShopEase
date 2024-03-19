package com.wipro.shopease.ui.login

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.wipro.shopease.R
import com.wipro.shopease.common.hideKeyboard
import com.wipro.shopease.common.mainNav
import com.wipro.shopease.common.showToast
import com.wipro.shopease.databinding.FragmentLoginBinding
import com.wipro.shopease.ui.BaseFragment
import com.wipro.shopease.utils.Resource

class LoginFragment : BaseFragment(), View.OnClickListener {

    // binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        if (sharedPref.getPrefBoolean("prefIsLogin")) {
            mainNav.navigate(R.id.action_loginFragment_to_homeFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserver()
    }

    private fun init() {
        // focus change for icon color
        viewModel.endIconColorChange(requireContext(), binding)
        // remove white space in edit text
        binding.etPassword.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filterNot { it.isWhitespace() }
        })
        // text watcher
        binding.etEmail.addTextChangedListener(viewModel.emailTextWatcher())
        binding.etPassword.addTextChangedListener(viewModel.passwordTextWatcher())
        // click event
        binding.btnLogin.setOnClickListener(this)
    }

    private fun initObserver() {
        // email error
        viewModel.emailError.observe(viewLifecycleOwner) {
            binding.tvEmailError.text = it
        }
        // password error
        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.tvPasswordError.text = it
        }
        // login api response
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveInPref(it)
                        mainNav.navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    binding.pbLoader.visibility = View.GONE
                    binding.btnLogin.isClickable = true
                }

                Resource.Status.ERROR -> {
                    requireActivity().showToast(getString(R.string.login_failed))
                    binding.pbLoader.visibility = View.GONE
                    binding.btnLogin.isClickable = true
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                    binding.btnLogin.isClickable = false
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnLogin -> {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                with(viewModel) {
                    if (!validation(email, password)) {
                        emailValidation(email)
                        passwordValidation(password)
                        return
                    }
                    requireActivity().hideKeyboard()
                    apiCall(email, password)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
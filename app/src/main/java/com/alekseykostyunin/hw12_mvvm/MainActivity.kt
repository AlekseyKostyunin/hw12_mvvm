package com.alekseykostyunin.hw12_mvvm

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.alekseykostyunin.hw12_mvvm.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            viewModel.onSearchClick(binding.textForSearch.text.toString())
        }

        binding.textForSearch.addTextChangedListener {
            viewModel.onTextChanged(it.toString())
        }

        viewModel.viewModelScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    State.Initial -> {
                        binding.progress.isVisible = false
                        binding.searchLayout.error = null
                        binding.button.isEnabled = false
                    }

                    State.Loading -> {
                        binding.progress.isVisible = true
                        binding.searchLayout.error = null
                        binding.button.isEnabled = false
                    }

                    is State.Success -> {
                        binding.progress.isVisible = false
                        binding.searchLayout.error = null
                        binding.button.isEnabled = true
                        binding.textResultSearch.text =
                            "По запросу \"${state.textSearch}\" ничего не найдено"
                    }

                    is State.Error -> {
                        binding.progress.isVisible = false
                        binding.searchLayout.error = state.textError
                        binding.button.isEnabled = false
                        binding.textResultSearch.text = null
                    }

                }
            }
        }

        viewModel.viewModelScope.launch {
            viewModel.isEnabledButtonSearch.collect { isEnable ->
                binding.button.isEnabled = isEnable

            }
        }

    }
}
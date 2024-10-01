package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ksv.costmemories.databinding.FragmentFindBinding

class FindFragment : Fragment() {
    private var _binding: FragmentFindBinding? = null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                binding.radioShop.id -> {
                    binding.resultText.text = "Shop"
                }
                binding.radioProduct.id -> {
                    binding.resultText.text = "Product"
                }
            }

        }
    }
}
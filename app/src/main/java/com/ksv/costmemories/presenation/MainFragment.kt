package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editPurchaseButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_editPurchaseFragment)
        }

        binding.shopsButton.setOnClickListener {
            parentFragmentManager.commit {
                replace<ShopsFragment>(R.id.fragment_container)
                addToBackStack(ShopsFragment::class.java.name)
            }
        }
        binding.productsButton.setOnClickListener {
            parentFragmentManager.commit {
                replace<FillDbFragment>(R.id.fragment_container)
                addToBackStack(FillDbFragment::class.java.name)
            }
        }
        binding.findFragmentButton.setOnClickListener {
            parentFragmentManager.commit {
                replace<FindFragment>(R.id.fragment_container)
                addToBackStack(FindFragment::class.java.name)
            }
        }
    }

}
package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentMainBinding
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import com.ksv.costmemories.supporded.FillDb
import kotlinx.coroutines.launch

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

        binding.fillDb.setOnClickListener {
            lifecycleScope.launch {
                FillDb.fill()
            }
        }


    }

}
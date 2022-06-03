package com.example.infystore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.infystore.adapter.ProductAdapter
import com.example.infystore.databinding.FragmentHomeBinding
import com.example.infystore.model.Product
import com.example.infystore.viewmodel.HomeViewModel
import com.example.infystore.utils.CommonUtils
import com.example.infystore.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
 * The DashboardFragment, A Fragment to show ordered list of products
 * Hilt provide dependency to other classes which have Android entry point
 */
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    /**
     * The _binding , An Instance of FragmentHomeBinding
     */
    private var _binding: FragmentHomeBinding? = null

    /**
     * The productAdapter , An Adapter class of Product listing
     */
    private lateinit var productAdapter: ProductAdapter

    /**
     * The binding, A binding Instance
     */
    private val binding get() = _binding!!

    /**
     * The homeViewModel, An Instance of HomeViewModel
     */
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initialSetUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * The initialSetUp method, to set adapter and recyclerview
     */
    private fun initialSetUp() {
        val orderList: List<Product>? = CommonUtils.getArrayListFromPref(Constants.ORDER_LIST)
        orderList.let {
            productAdapter = ProductAdapter(homeViewModel)
            productAdapter.product = it!!
            binding.rvProduct.apply {
                adapter = productAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }
        if(orderList?.isEmpty() == true) binding.tvNoDataFound.visibility=View.VISIBLE else binding.tvNoDataFound.visibility=View.GONE
        binding.pbTopLinear.visibility = View.GONE
    }



}
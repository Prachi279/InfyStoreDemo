package com.example.infystore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.infystore.MyApplication
import com.example.infystore.adapter.ProductAdapter
import com.example.infystore.databinding.FragmentHomeBinding
import com.example.infystore.utils.CommonUtils
import com.example.infystore.utils.Constants
import com.example.infystore.utils.PreferenceHelper.get
import com.example.infystore.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The HomeFragment class, to show product listing
 * Hilt provide dependency to other classes which have Android entry point
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    /**
     * The _binding , An Instance of FragmentHomeBinding
     */
    private var _binding: FragmentHomeBinding? = null

    /**
     * The binding, A binding Instance
     */
    private val binding get() = _binding!!

    /**
     * The homeViewModel, An Instance of HomeViewModel
     */
    private lateinit var homeViewModel: HomeViewModel

    /**
     * The productAdapter , An Adapter class of Product listing
     */
    private lateinit var productAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onResume() {
        super.onResume()
        initialSetUp()
    }

    override fun onStop() {
        super.onStop()
        if(MyApplication.prefHelper!![Constants.IS_LOGGED_IN,false]!!){
            CommonUtils.saveObjIntoPref(
                homeViewModel.orderList,
                Constants.ORDER_LIST
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * The initialSetUp method, to set adapter and recyclerview
     */
    private fun initialSetUp() {
        productAdapter = ProductAdapter(homeViewModel)
        binding.rvProduct.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        homeViewModel.productList.observe(viewLifecycleOwner) { alList ->
            productAdapter.product = alList
            binding.pbTopLinear.visibility = View.GONE
        }
    }
}
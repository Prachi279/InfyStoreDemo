package com.example.infystore.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.infystore.adapter.ProductAdapter
import com.example.infystore.databinding.FragmentHomeBinding
import com.example.infystore.model.Product
import com.example.infystore.utils.Constants
import com.example.infystore.utils.PrefImpl
import com.example.infystore.utils.PreferenceHelper.get
import com.example.infystore.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

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

    /**
     *The applicationPref, SharedPreference Instance
     */
    @Inject
    @Named("Pref")
    lateinit var applicationPref: SharedPreferences

    /**
     *The prefImpl, PrefImpl Instance
     */
    @Inject
    lateinit var prefImpl: PrefImpl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        //binding of viewmodel class with xml file variable viewmodel
        // is compulsory if we want to call any method or perform operations from the xml
        binding.homeViewModel = homeViewModel
        val root: View = binding.root
        return root
    }

    override fun onResume() {
        super.onResume()
        initialSetUp()
    }

    override fun onStop() {
        super.onStop()
        if (applicationPref[Constants.IS_LOGGED_IN, false]!!) {
            prefImpl.saveObjIntoPref(
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
        val orderList: List<Product>? = prefImpl.getArrayListFromPref(Constants.ORDER_LIST)

        binding.rvProduct.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        homeViewModel.productList.observe(viewLifecycleOwner) { alList ->
            orderList.let { data ->
                if (!data.isNullOrEmpty()) {
                    alList.forEach { item ->
                        data.map { orderItem ->
                            if (orderItem.id == item.id) {
                                item.isPurchased = true
                            }
                        }
                    }
                }
            }
            productAdapter.product = alList
            binding.pbTopLinear.visibility = View.GONE
        }
    }

}
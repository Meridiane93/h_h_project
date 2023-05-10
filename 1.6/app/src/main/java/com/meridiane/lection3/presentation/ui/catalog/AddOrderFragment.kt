package com.meridiane.lection3.presentation.ui.catalog

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.meridiane.lection3.Constants
import com.meridiane.lection3.databinding.FragmentAddOrderBinding
import com.meridiane.lection3.presentation.ui.MapActivity
import com.meridiane.lection3.presentation.viewModel.ProductDetailsViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddOrderFragment : Fragment() {

    private lateinit var binding: FragmentAddOrderBinding

    private val viewModel: ProductDetailsViewModel by viewModels()
    private var countOrder = 1

    val latitude: Double = 54.18 //  широта Саранск
    val longitude: Double = 45.18 // долготу Саранск

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString(Constants.SEND_ID_ADD_ORDER)!!

        viewModel.getProductDetails(id)

        lifecycleScope.launch {
            viewModel.productsState.collectLatest { product ->

                if (product.title != null) {
                    with(binding) {
                        textOrderTitle.text =
                            if (product.title.length > 30) product.title.substring(
                                0,
                                30
                            ) else product.title
                        textOrderDepartament.text = product.department
                        imageOrderIcon.load(product.preview)
                    }
                }
            }
        }

        binding.increment.setOnClickListener {
            countOrder++
            binding.countOrder.text = countOrder.toString()
        }

        binding.decrement.setOnClickListener {
            if (countOrder != 0) countOrder--
            binding.countOrder.text = countOrder.toString()
        }

        binding.textHome.setOnClickListener {
            //map(binding.textHome.text.toString())
            //getAddress(latitude,longitude)
            val intent = Intent(binding.root.context, MapActivity::class.java)
            startActivity(intent)
        }

    }


//    private fun map(adress: String) {
//
//        val geoUriString = "geo:0,0?q=$adress?z=2"
//        val geoUri: Uri = Uri.parse(geoUriString)
//        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
//
//        try {
//            startActivity(mapIntent)
//        } catch (ex: ActivityNotFoundException) {
//            Log.d("MyTag", "Ex: $ex")
//        }
//    }

//    private fun getAddress(latitude: Double, longitude: Double): String {
//        val url =
//            "https://geocode-maps.yandex.ru/1.x/?apikey=84e69859-f23a-438a-873d-f3df7c0845d7&format=json&geocode=$longitude,$latitude"
//        val json = URL(url).readText()
//        val obj = JSONObject(json)
//        val featureMember = obj.getJSONObject("response").getJSONObject("GeoObjectCollection")
//            .getJSONArray("featureMember").getJSONObject(0)
//        val metaDataProperty =
//            featureMember.getJSONObject("GeoObject").getJSONObject("metaDataProperty")
//                .getJSONObject("GeocoderMetaData")
//
//        Log.d("MyTag","maps: $metaDataProperty")
//        return metaDataProperty.getString("text")
//    }


}
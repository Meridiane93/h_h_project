package com.meridiane.lection3.presentation.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.ActivityMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), UserLocationObjectListener {

    private lateinit var binding: ActivityMapBinding

    private lateinit var locationMapKit: UserLocationLayer

    private var defaultCoordinate = getString(R.string.default_coordinate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapKit: MapKit = MapKitFactory.getInstance()

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btnChoose -> {
                    if (binding.tvAddress.text.contains(",")) {
                        val intent = Intent()
                        intent.putExtra(
                            Constants.KEY_SEND_ADD_MAP_ACTIVITY,
                            binding.tvAddress.text.toString()
                        )
                        intent.putExtra(Constants.COORDINATE_SEND_ADD_MAP_ACTIVITY,
                            defaultCoordinate)
                        setResult(RESULT_OK, intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@MapActivity,
                            getString(R.string.text_map_coordinate),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    true
                }

                else -> true

            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        startActivityChangeText()
        locationMapKit = mapKit.createUserLocationLayer(binding.mapview.mapWindow)
        locationMapKit.isVisible = true
        locationMapKit.setObjectListener(this)
        SearchFactory.initialize(this)

        binding.mapview.map.addInputListener(inputListener)

        binding.mapview.map.move(
            CameraPosition(
                Point(
                    defaultCoordinate.substringBefore(',').toDouble(),
                    defaultCoordinate.substringAfterLast(',').toDouble()
                ), 18.0f, 0.0f, 0.0f
            ),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )

    }


    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMapKit.setAnchor(
            PointF(
                (binding.mapview.width() * 0.5).toFloat(),
                (binding.mapview.height() * 0.5).toFloat()
            ),
            PointF(
                (binding.mapview.width() * 0.5).toFloat(),
                (binding.mapview.height() * 0.83).toFloat()
            )
        )

        userLocationView.arrow.setIcon(ImageProvider.fromResource(this, R.drawable.my_location))
        val picIcon = userLocationView.pin.useCompositeIcon()
        picIcon.setIcon(
            "icon",
            ImageProvider.fromResource(this, R.drawable.location_on),
            IconStyle().setAnchor(PointF(0f, 0f)).setRotationType(RotationType.NO_ROTATION)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE
    }

    override fun onObjectRemoved(userLocationView: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}

    // обработчик нажатий на карту
    private val inputListener: InputListener = object : InputListener {
        override fun onMapTap(
            p0: com.yandex.mapkit.map.Map,
            p1: Point
        ) {

            defaultCoordinate = "${p1.latitude},${p1.longitude}"

            val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
            val addresses = geocoder.getFromLocation(p1.latitude, p1.longitude, 1)
            val address = addresses?.get(0)?.getAddressLine(0)

            binding.bottomBar.visibility = View.VISIBLE
            binding.tvAddress.text = address!!.substringBeforeLast(",")

            val mapObjects: MapObjectCollection = binding.mapview.map.mapObjects
            mapObjects.addPlacemark(
                p1,
                ImageProvider.fromResource(this@MapActivity, R.drawable.ic_picker)
            )
        }

        override fun onMapLongTap(
            p0: com.yandex.mapkit.map.Map,
            p1: Point
        ) {
            // обработка длинного тапа пока не нужна
        }
    }

    // получение данных из AddOrder
    private fun startActivityChangeText() {
        val getMessageMainActivity = intent.getStringExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY)

        if (!getMessageMainActivity.isNullOrBlank()) {
            binding.bottomBar.visibility = View.VISIBLE
            binding.tvAddress.text = getMessageMainActivity
        } else binding.bottomBar.visibility = View.INVISIBLE

        val getCoordinateActivity = intent.getStringExtra(Constants.COORDINATE_SEND_ADD_MAP_ACTIVITY)
        if (!getCoordinateActivity.isNullOrBlank()) {
            defaultCoordinate = getCoordinateActivity
        }
    }

}
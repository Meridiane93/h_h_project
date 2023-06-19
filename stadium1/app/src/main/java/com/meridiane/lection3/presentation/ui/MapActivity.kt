package com.meridiane.lection3.presentation.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
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
                        intent.putExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY, binding.tvAddress.text.toString())
                        setResult(RESULT_OK, intent)
                        finish()

                    } else {
                        Toast.makeText(this@MapActivity, "Укажитие место доставки", Toast.LENGTH_LONG)
                            .show()
                    }
                    true
                }

                else -> {
                    true
                }

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
            CameraPosition(Point(54.18, 45.18), 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )

    }

    // запрос на получение текущего местоположения
//    private fun requstLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                0
//            )
//            return
//        } else {
//            Toast.makeText(this, getString(R.string.map_toast), Toast.LENGTH_LONG)
//                .show()
//        }
//    }

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
            PointF((binding.mapview.width() * 0.5).toFloat(), (binding.mapview.height() * 0.5).toFloat()),
            PointF((binding.mapview.width() * 0.5).toFloat(), (binding.mapview.height() * 0.83).toFloat())
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

            val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
            val addresses = geocoder.getFromLocation(p1.latitude, p1.longitude, 1)
            val address = addresses?.get(0)?.getAddressLine(0)

            binding.bottomBar.visibility = View.VISIBLE
            binding.tvAddress.text = address!!.substringBeforeLast(",")

        }

        override fun onMapLongTap(
            p0: com.yandex.mapkit.map.Map,
            p1: Point
        ) {
            // обработка длинного тапа пока не нужна
        }
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val x = event.x.toInt()
//        val y = event.y.toInt()
//        when (event.action) {
//            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
//                Log.d("MyTag","x: $x , y: $y")
//            }
//        }
//        return false
//    }

    // получение данных из AddOrder
    private fun startActivityChangeText() {
        val getMessageMainActivity = intent.getStringExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY)

        if (!getMessageMainActivity.isNullOrBlank()) {
            binding.bottomBar.visibility = View.VISIBLE
            binding.tvAddress.text = getMessageMainActivity
        }
        else binding.bottomBar.visibility = View.INVISIBLE
    }

}
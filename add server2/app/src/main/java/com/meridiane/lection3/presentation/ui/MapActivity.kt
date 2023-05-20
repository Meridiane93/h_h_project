package com.meridiane.lection3.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), UserLocationObjectListener {

    private lateinit var mapView: MapView
    private lateinit var myLocation: ImageView
    private lateinit var locationMapKit: UserLocationLayer
    private lateinit var editText: EditText
    private lateinit var btDone: ImageView
    private lateinit var cdDone: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)

        val mapKit: MapKit = MapKitFactory.getInstance()

        mapView = findViewById(R.id.mapview)
        myLocation = findViewById(R.id.bt_my_location)
        editText = findViewById(R.id.edit_search)
        btDone = findViewById(R.id.bt_done)
        cdDone = findViewById(R.id.card_view_done)

        if (editText.text.contains(",")) cdDone.visibility = View.VISIBLE
        else cdDone.visibility = View.INVISIBLE


        startActivityChangeText()
        locationMapKit = mapKit.createUserLocationLayer(mapView.mapWindow)
        locationMapKit.isVisible = true
        locationMapKit.setObjectListener(this)
        SearchFactory.initialize(this)

        mapView.map.addInputListener(inputListener)

        mapView.map.move(
            CameraPosition(Point(54.18, 45.18), 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )

        myLocation.setOnClickListener {
            requstLocationPermission()
        }

        btDone.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY,editText.text.toString())
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    // запрос на получение текущего местоположения
    private fun requstLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                0
            )
            return
        } else {
            Toast.makeText(this, getString(R.string.map_toast), Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMapKit.setAnchor(
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.5).toFloat()),
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.83).toFloat())
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
            p1: Point)
        {

            val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
            val addresses = geocoder.getFromLocation(p1.latitude, p1.longitude, 1)
            val address = addresses?.get(0)?.getAddressLine(0)

            editText.setText(address!!.substringBeforeLast(","))
            cdDone.visibility = View.VISIBLE

        }

        override fun onMapLongTap(
            p0: com.yandex.mapkit.map.Map,
            p1: Point)
        {
            // обработка длинного тапа нам пока не нужна
        }
    }

    // получение данных из AddOrder
    private fun startActivityChangeText(){
        val getMessageMainActivity = intent.getStringExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY)

        if (!getMessageMainActivity.isNullOrBlank()) editText.setText(getMessageMainActivity)
        else editText.setText(R.string.search_text)
    }

}
package com.meridiane.lection3.presentation.ui

import android.Manifest
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), UserLocationObjectListener {

    private lateinit var mapView: MapView
    private lateinit var myLocation: ImageView
    private lateinit var locationMapKit: UserLocationLayer
//    private lateinit var searchEdit: EditText
//    private lateinit var searchManager: SearchManager
//    private lateinit var searchSession: Session

//    private fun submitQuery(query: String) {
//        searchSession = searchManager.submit(
//            query, VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
//            SearchOptions(), this
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(Constants.KEY_MAP)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_map)

        val mapKit: MapKit = MapKitFactory.getInstance()

        mapView = findViewById(R.id.mapview)
        myLocation = findViewById(R.id.bt_my_location)
//        searchEdit = findViewById(R.id.edit_search)

        locationMapKit = mapKit.createUserLocationLayer(mapView.mapWindow)
        locationMapKit.isVisible = true
        locationMapKit.setObjectListener(this)
        SearchFactory.initialize(this)
//        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
//        mapView.map.addCameraListener(this)

        mapView.map.addInputListener(inputListener)

//        searchEdit.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                submitQuery(searchEdit.text.toString())
//            }
//            false
//        }


        mapView.map.move(
            CameraPosition(Point(54.18, 45.18), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )

        myLocation.setOnClickListener {
            requstLocationPermission()
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
            Toast.makeText(this, "Пожалуйста включите ваше местоположение", Toast.LENGTH_LONG)
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

    //
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

//    override fun onSearchResponse(response: Response) {
//        val mapObject: MapObjectCollection = mapView.map.mapObjects
//        mapObject.clear()
//        for (searchResult in response.collection.children) {
//            val resultLocation = searchResult.obj!!.geometry[0].point!!
//            mapObject.addPlacemark(
//                resultLocation,
//                ImageProvider.fromResource(this, R.drawable.location_on)
//            )
//        }
//    }

    // обработчик нажатий на карту
    private val inputListener: InputListener = object : InputListener {
        override fun onMapTap(
            p0: com.yandex.mapkit.map.Map,
            p1: Point)
        {
            Log.d("MyTag","p0: $p0 , p1: $p1")
            Log.d("MyTag","p0: $p0 , p1: ${p1.latitude}")
            Log.d("MyTag","p0: $p0 , p1: ${p1.longitude}")

            val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
            val addresses = geocoder.getFromLocation(p1.latitude, p1.longitude, 1)
            val address = addresses?.get(0)?.getAddressLine(0)
            Log.d("MyTag","address: $address")
        }

        override fun onMapLongTap(
            p0: com.yandex.mapkit.map.Map,
            p1: Point)
        {
            Log.d("MyTag","LongTap p0: $p0 , p1: $p1")
            // обработка длинного тапа нам пока не нужна
        }
    }

//    override fun onSearchError(error: Error) {
//        val errorMessage: String = if (error is NetworkError) getString(R.string.error_network)
//        else error.toString()
//        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
//    }
//
//    override fun onCameraPositionChanged(
//        map: Map,
//        cameraPosition: CameraPosition,
//        cameraUpdateReason: CameraUpdateReason,
//        finished: Boolean
//    ) {
//
//    }

}
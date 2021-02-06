package com.example.mymap

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback:LocationCallback

    private lateinit var boardList:ArrayList<Board>
    private lateinit var bDatabase: DatabaseReference

    var ab: ActionBar? = null

    private var refreshCount = 0

    val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)

    val PERM_FLAG = 99

    lateinit var bitmapDrawable : BitmapDrawable

    lateinit var food1: ImageView
    lateinit var food2: ImageView
    lateinit var storename1: TextView
    lateinit var storename2: TextView
    lateinit var text1: TextView
    lateinit var text2: TextView
    lateinit var leftdate: TextView
    lateinit var rightdate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        ab = supportActionBar
        ab!!.setDisplayShowHomeEnabled(false)
        ab!!.customView = layoutInflater.inflate(R.layout.action_bar, null)
        ab!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        super.onCreate(savedInstanceState)

        if(isPermitted()){
            startProcess()
        }else{
            ActivityCompat.requestPermissions(this,permissions,PERM_FLAG)
        }

        boardList = ArrayList()
        bDatabase = FirebaseDatabase.getInstance().getReference("Board")

        food1 = findViewById(R.id.food1)
        food2 =  findViewById(R.id.food2)
        storename1 = findViewById(R.id.storename1)
        storename2 = findViewById(R.id.storename2)
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)
        leftdate = findViewById(R.id.leftdate)
        rightdate = findViewById(R.id.rightdate)

        bDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(@NonNull snapshot: DataSnapshot) { //데이터가 변경 되면 호출
                boardList.clear()
                for (dataSnapshot in snapshot.getChildren())
                {
                    boardList.add(dataSnapshot.getValue(Board::class.java)!!)
                }

                if(boardList.size != 0){
                    mMap.clear()

//                    val myLocation = LatLng(location.latitude, location.longitude)
//                    val marker = MarkerOptions()
//                        .position(myLocation)
//                        .title("내 위치")

                    var size: Int
                    size = boardList.size-1

                    food1.setImageResource(getCategoryImg(boardList.get(size).category))
                    food2.setImageResource(getCategoryImg(boardList.get(size-1).category))
                    storename1.setText(boardList.get(size).title)
                    storename2.setText(boardList.get(size-1).title)
                    text1.setText(boardList.get(size).content)
                    text2.setText(boardList.get(size-1).content)
                    leftdate.setText(boardList.get(size).date)
                    rightdate.setText(boardList.get(size-1).date)

                    for (idx in boardList){
                        if (idx.isComplete != true){
                            var bitmapDrawable: BitmapDrawable

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                bitmapDrawable = getDrawable(getCategoryIcon(idx.category)) as BitmapDrawable
                            } else{
                                bitmapDrawable = resources.getDrawable(getCategoryIcon(idx.category)) as BitmapDrawable
                            }

                            var discriptor = BitmapDescriptorFactory.fromBitmap(bitmapDrawable.bitmap)

                            val markermaker = MarkerOptions()
                                .position(LatLng(idx.latitude, idx.longitude))
                                .title(idx.title)
                                .snippet(idx.content)
                                .icon(discriptor)

                            mMap.addMarker(markermaker)
                        }
                    }
                }

            }

            override fun onCancelled(@NonNull error: DatabaseError) {

            }
        })
    }

    fun getCategoryImg(category:String):Int {
        when (category) {
            "분식" -> return R.drawable.dduck
            "피자" -> return R.drawable.pizza
            "중식" -> return R.drawable.black_noddle
            "일식" -> return R.drawable.sushi
            "한식" -> return R.drawable.bossam
            "치킨" -> return R.drawable.chicken
        }
        return -1
    }

    fun getCategoryIcon(category:String):Int {
        when (category) {
            "분식" -> return R.drawable.dduck_icon
            "피자" -> return R.drawable.pizza_icon
            "중식" -> return R.drawable.jjajang_icon
            "일식" -> return R.drawable.sushi_icon
            "한식" -> return R.drawable.rice_icon
            "치킨" -> return R.drawable.chicken_icon
        }
        return -1
    }

    fun isPermitted():Boolean{
        for (perm in permissions){
           if(ContextCompat.checkSelfPermission(this,perm) != PERMISSION_GRANTED) {
               return false
           }
        }

        return true
    }

    fun startProcess(){
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupdateLocationListener()
    }

    // 내 위치를 가져오는 코드
    @SuppressLint("MissingPermission")
    fun setupdateLocationListener(){
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for((i,location) in it.locations.withIndex()){
                        Log.d("Location","$i ${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }

    fun setLastLocation(location: Location){
        val myLocation = LatLng(location.latitude, location.longitude)
        var bitmapDrawable: BitmapDrawable

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            bitmapDrawable = getDrawable(R.drawable.human_icon) as BitmapDrawable
        } else{
            bitmapDrawable = resources.getDrawable(R.drawable.human_icon) as BitmapDrawable
        }

        var discriptor = BitmapDescriptorFactory.fromBitmap(bitmapDrawable.bitmap)

        val marker = MarkerOptions()
            .position(myLocation)
            .title("내 위치")
            .icon(discriptor)


        val idx:Int =1

        val cameraOption = CameraPosition.builder()
                .target(myLocation)
                .zoom(16.0f)
                .build()

        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)

//            for (idx in boardList){
//            if (idx.isComplete != true){
//                val markermaker = MarkerOptions()
//                    .position(LatLng(idx.latitude, idx.longitude))
//                    .title(idx.title)
//                    .snippet(idx.content)
//
//                mMap.addMarker(markermaker)
//            }
//        }

        mMap.addMarker(marker)

        mMap.moveCamera(camera)

        if (mMap != null) {

            // More info: https://developers.google.com/maps/documentation/android/infowindows
            mMap.setOnInfoWindowClickListener {
                // Determine what marker is clicked by using the argument passed in
                // for example, marker.getTitle() or marker.getSnippet().
                // Code here for navigating to fragment activity.
                var date: String? = null
                for (idx in boardList){
                    if (idx.content == it!!.snippet){
                        date = idx.date
                        break
                    }
                }
                val intent = Intent(this, PostActivity::class.java)
                intent.putExtra("REG_DATE",date)
                startActivity(intent)
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        when(requestCode){
            PERM_FLAG -> {
                var check = true
                for(grant in grantResults){
                    if(grant != PERMISSION_GRANTED){
                        check = false
                        break
                    }
                }
                if(check){
                    startProcess()
                }else{
                    Toast.makeText(this,"권한을 승인해야지만 앱을 사용할 수 있습니다. ",Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    /*
    //BoardActivity로 이동
    fun onCallBtnBoard(view: View) {

    }

    //MapsActivity로 이동(변화가 없다고 보면 됨)
    fun onCallBtnHome(view: View) {

    }

    //MainActivity로 이동(글쓰기 레이아웃)
    fun onCallBtnNew(view: View) {

    }
*/
//    var intent = getIntent()

    fun onCallBtnBoard(v: View?) {
        val intent = Intent(this, BoardActivity::class.java)
        startActivity(intent)
    }

    fun onCallBtnNew(v: View?) {
        val intent = Intent(this, InsertActivity::class.java)
        startActivity(intent)
    }

    fun onleftclick(view: View) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("REG_DATE",leftdate.text.toString())
        startActivity(intent)
    }
    fun onrightclick(view: View) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("REG_DATE",rightdate.text.toString())
        startActivity(intent)
    }
}








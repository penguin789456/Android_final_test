package com.example.android_final_test

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException


class MainActivity : AppCompatActivity(),LocationListener {

    private lateinit var mLocationManager: LocationManager      //LocationManager
    private lateinit var event:List<Event>                      //全部event資料 內含:id,name,tel,address,longitude,latitude
    private lateinit var dbHelper: DBHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var adapter:travel_Adapter
    private lateinit var nearestEvent:Event
    private lateinit var recyclerView:RecyclerView
    private lateinit var fragment : FragmentContainerView
    private var hasGPS:Boolean = false          //檢查是否有GPS
    private var eventDistance: Float? = null      //活動距離(公尺)
    private var isInEventRange:Boolean = false  //判斷是否有在範圍內
    private var disableView:Boolean = true          //顯示fragment || recyclerView
    private var nowLocation = Location("nowLocation")       //現在位置
    private var nearestEventLocation = Location("nearestEvent")   //活動位置
    private lateinit var btnAdv: Button
    private lateinit var location: Location
    private var locationCode:Int=1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient //抓取最後的GPS位置
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnAdv = findViewById<Button>(R.id.btnadv)
        dbHelper = DBHelper(this)
        db = dbHelper.writableDatabase
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager    //取得location manager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) //抓取最後的GPS位置


        checkLocationPermission()  //檢查location權限
        checkFirstRun()            //檢查是否第一次開啟程式
        getNowLocation(25.021002,121.465042)
        SetRecyclerView(dbHelper.queryNearEvents(nowLocation),nowLocation)

        recyclerView.visibility=View.GONE
        btnAdv.setOnClickListener {
            fragment = findViewById(R.id.fragmentContainerView)
            toggleViews()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onLocationChanged(p0: Location) {
        getNowLocation(p0.latitude,p0.longitude)                             //目前經緯度
        val nearestEvent =dbHelper.queryNearestEvents(nowLocation)          //最近的活動
        if (nearestEvent != null) {
            getNearestEventLocation(nearestEvent.latitude,nearestEvent.longitude)
        }
        eventDistance = nowLocation.distanceTo(nearestEventLocation)    //取得與最近活動間的距離
        isInEventRange = inEventRange(eventDistance!!.toInt())          //判斷是否在活動範圍內
        if (isInEventRange){
            makeShowNotify(nearestEvent!!.name)
        }
        var fragmentName:TextView=findViewById(R.id.eventName)
        fragmentName.text="名字:${nearestEvent!!.name}\n 電話:${nearestEvent!!.tel}\n 地址:${nearestEvent.address}\n 距離:${eventDistance!!.toInt()}公尺"


//        fusedLocationClient.lastLocation  //抓取最後位置
//            .addOnSuccessListener { locaion : Location? ->
//                if (locaion!=null){
//                    nowLocation=locaion
//                }
//            }

        Log.d("LTag","eventName:${nearestEvent!!.name} \neventLongitude:${nearestEvent.longitude} \neventLatitude:${nearestEvent.latitude}  \n" +
                "Distance:${eventDistance}m \ninEventRange:${isInEventRange}\n"
                + "nowLongitude:${nowLocation.longitude}\n"+ "nowLatitude:${nowLocation.latitude}")

        changeItem(dbHelper.queryNearEvents(nowLocation))  //顯示附近的活動
    }

    //取得現在位置
    private fun getNowLocation(latitude: Double, longitude: Double) {
//        nowLocation.longitude = longitude
//        nowLocation.latitude = latitude
        //測試用致理門口
        nowLocation.longitude = longitude
        nowLocation.latitude = latitude
    }

    //取得活動位置
    private fun getNearestEventLocation(latitude: Double, longitude: Double) {
        nearestEventLocation.longitude = longitude
        nearestEventLocation.latitude = latitude
    }

    //判斷是否進入活動範圍
    private fun inEventRange(distance:Int): Boolean {
        //距離活動小於50m 回傳true
        return distance < 50
    }

    //檢查location權限
    private fun checkLocationPermission(){
        hasGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //檢查GPS是否啟用
        if(hasGPS){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,1.0f,this)  //取得位置(1秒更新,移動1m更新)
        }else{
            Log.d("LTag","設備未提供定位服務")  //如果沒找到GPS
        }
    }

    //將CSV資料寫入資料庫
    private fun readCSV2DataBase(){
        val inputStream: InputStream = resources.openRawResource(R.raw.acd4f2fa19cd40ed)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.readLine()
        var line: String?
        while (reader.readLine().also { line = it } != null) {

            val data = line?.split(",")
            val id = data?.get(0)?.toLong() ?: 0L
            val name:String = data?.get(1) ?: ""
            val tel:String = data?.get(2) ?: ""
            val address:String = data?.get(3) ?: ""
            val longitude:Double = data?.get(4)?.toDouble() ?: 0.0
            val latitude:Double = data?.get(5)?.toDouble() ?: 0.0
            var event:Event = Event(id,name,tel,address,longitude,latitude)
            dbHelper.insertEvent(event)

            Log.d("LTag","${event.id}:${event.name}")
        }
        reader.close()
    }
    //檢查是否第一次進入程式
    private fun checkFirstRun() {
        Log.d("LTag", "Checked.")
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            readCSV2DataBase()
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
    }
    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000L,1.0f,this)
    }

    override fun onPause() {
        super.onPause()
        mLocationManager.removeUpdates(this)  //取消location更新
    }

    //生成RecyclerView
    private fun SetRecyclerView(eventList:List<Event>,mLocaton:Location){
        recyclerView=findViewById(R.id.Recycler_travel)
        recyclerView.layoutManager = LinearLayoutManager(this)  // 或其他布局管理器，例如 GridLayoutManager
        adapter=travel_Adapter(this,eventList,mLocaton)
        recyclerView.adapter = adapter
    }
    private fun changeItem(event:List<Event>){
        //覆蓋OLD EVENTS
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "取得權限", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "需要定位權限", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun makeShowNotify(s:String){
        val notifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel= NotificationChannel("mCounter","Channel Counter",NotificationManager.IMPORTANCE_DEFAULT)
        notifyManager.createNotificationChannel(channel)
        val myBuilder= NotificationCompat.Builder(this,"mCounter").apply{
            setContentTitle("附近有活動!!")
            setContentText("活動地點:$s")
            setSubText("我在狀態列")
            setWhen(System.currentTimeMillis())
            setChannelId("mCounter")
            setSmallIcon(R.drawable.pepega)
        }
        notifyManager.notify(1,myBuilder.build())

    }
    fun toggleViews() {
        if (disableView) {
            fragment.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            disableView = false
            btnAdv.text = "最近地點"
        } else {
            fragment.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            disableView = true
            btnAdv.text = "附近地點"
        }
    }
}

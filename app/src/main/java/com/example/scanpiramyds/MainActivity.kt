package com.example.scanpiramyds

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiramyds.UI.PiramydsListAdapter
import com.example.scanpiramyds.database.Piramyd
import com.example.scanpiramyds.database.PiramydViewModel
import com.example.scanpiramyds.database.ViewModelFactory
import com.example.scanpiramyds.network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), Callback<List<Piramyd>> {

    private lateinit var piramydViewModel: PiramydViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    private val Image_Capture_Code = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //syncWithDatabase()


        recyclerView = recycler_view
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = PiramydsListAdapter(this)
        recyclerView.adapter = adapter

        piramydViewModel =
            ViewModelProvider(this, ViewModelFactory(application)).get(PiramydViewModel::class.java)
        piramydViewModel.allPiramyds.observe(
            this,
            Observer { piramyds -> piramyds?.let { adapter.setPiramyds(it) } })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = getMenuInflater().inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_item_scan_barcodes -> openCameraActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    fun openCameraActivity(){
        val cInt = Intent(this, CameraActivity::class.java)
        startActivityForResult(cInt, Image_Capture_Code)
    }


    fun syncWithDatabase(){

        NetworkService.getNetworkInstance()
            .getJSONApi()
            .getPiramyds()
            .enqueue(this)
    }

    override fun onFailure(call: Call<List<Piramyd>>, t: Throwable) {
       Toast.makeText(this, "Request error: " + t.message, Toast.LENGTH_LONG).show()
        Log.e("ErrorTag", t.message.toString())
    }

    override fun onResponse(call: Call<List<Piramyd>>, response: Response<List<Piramyd>>) {
        if(response.isSuccessful()){

            val allPiramyds = response.body()
            allPiramyds?.forEach{ it.name?.let { piramydName -> Log.d("TAG", piramydName) }}

        }
    }

    fun syncronizeDataWithDatabase(){
        piramydViewModel.updateAll()
    }

    override fun onDestroy() {
        syncronizeDataWithDatabase()

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }


}
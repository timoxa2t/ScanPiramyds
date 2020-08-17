package com.example.scanpiramyds

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.scanpiramyds.UI.PiramydsListAdapter

import com.example.scanpiramyds.database.PiramydViewModel
import com.example.scanpiramyds.database.ViewModelFactory
import com.example.scanpiramyds.network.NetworkService
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory
import com.example.scanpiramyds.database.Piramyd as Piramyd
import retrofit2.Callback as Callback



class MainActivity : AppCompatActivity(), Callback<List<Piramyd>> {

    private lateinit var piramydViewModel: PiramydViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        syncWithDatabase()

        recyclerView = recycler_view
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = PiramydsListAdapter(this)
        recyclerView.adapter = adapter

        piramydViewModel = ViewModelProvider(this, ViewModelFactory(application)).get(PiramydViewModel::class.java)
        piramydViewModel.allPiramyds.observe(this, Observer { piramyds -> piramyds?.let {adapter.setPiramyds(it)}})

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
}
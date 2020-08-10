package com.example.scanpiramyds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.scanpiramyds.UI.PiramydsListAdapter

import com.example.scanpiramyds.database.Piramyd
import com.example.scanpiramyds.database.PiramydViewModel
import com.example.scanpiramyds.database.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var piramydViewModel: PiramydViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = recycler_view
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = PiramydsListAdapter(this)
        recyclerView.adapter = adapter

        piramydViewModel = ViewModelProvider(this, ViewModelFactory(application)).get(PiramydViewModel::class.java)
        piramydViewModel.allPiramyds.observe(this, Observer { piramyds -> piramyds?.let {adapter.setPiramyds(it)} })

    }

    fun quickTest(list: List<Piramyd>){


//        if (list.isNotEmpty()) {
//            tv_piramyd.text = "" + list.get(0).code + list.get(0).name
//        }

    }
}
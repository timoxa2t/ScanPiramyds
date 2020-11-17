package com.example.scanpiramyds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiramyds.UI.PiramydsListAdapter
import com.example.scanpiramyds.database.Pyramid
import com.example.scanpiramyds.database.PiramydViewModel
import com.example.scanpiramyds.database.ViewModelFactory
import com.example.scanpiramyds.network.NetworkService
import com.google.firebase.database.*
import com.google.firebase.database.ktx.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), Callback<List<Pyramid>> {

    private lateinit var piramydViewModel: PiramydViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var database: FirebaseDatabase

    private val Image_Capture_Code = 1
    private val TAG: String = "MyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //syncWithDatabase()

        database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("pyramids")


        Log.d(TAG, "Getting reference")
        val piramid = Pyramid("123", "asd", false)
        val secpiramid = Pyramid("124", "cmd", false)
        myRef.child(piramid.code).setValue(piramid)
        myRef.child(secpiramid.code).setValue(secpiramid)

        Log.d(TAG, "value writen")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<Any>()



                Log.d(TAG, "Value is changed" + value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        myRef.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val pyramid = dataSnapshot.getValue<Pyramid>()
                if (pyramid !== null) {
                    piramydViewModel.insert(pyramid)
                }
                Log.d(TAG, "onChildAdded:" + pyramid)
                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue<Pyramid>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", error.toException())
                Toast.makeText(this@MainActivity, "Failed to load comments.", Toast.LENGTH_SHORT).show()

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue<Pyramid>()
                val commentKey = dataSnapshot.key

                // ...
            }


            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }



        })

        recyclerView = recycler_view
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = PiramydsListAdapter(this)
        recyclerView.adapter = adapter

        piramydViewModel =
            ViewModelProvider(this, ViewModelFactory(application)).get(PiramydViewModel::class.java)
        piramydViewModel.allPyramids.observe(
            this,
            Observer { pyramids -> pyramids?.let { adapter.setPiramyds(it) } })



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

    override fun onFailure(call: Call<List<Pyramid>>, t: Throwable) {
       Toast.makeText(this, "Request error: " + t.message, Toast.LENGTH_LONG).show()
        Log.e("ErrorTag", t.message.toString())
    }

    override fun onResponse(call: Call<List<Pyramid>>, response: Response<List<Pyramid>>) {
        if(response.isSuccessful()){

            val allPiramyds = response.body()
            allPiramyds?.forEach{ it.name?.let { piramydName -> Log.d("TAG", piramydName) }}

        }
    }

    fun syncronizeDataWithDatabase(){
        piramydViewModel.updateAll()
    }

    override fun onPause() {
        syncronizeDataWithDatabase()
        super.onPause()
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
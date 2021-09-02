package com.example.happyplaces.activites

import android.app.Activity
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.databases.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModels
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        fabAddHappyPlaces.setOnClickListener {
            val intent = Intent(this, AddHappyPlaces::class.java)
            startActivityForResult(intent, ADD_ACTIVITY_FORUPDATE)
        }
        getHapppyPlacesListfromLocalDB()
    }



    private fun setupHappyPlaceRecyclerview(happyplacelist:ArrayList<HappyPlaceModels>){
        rv_happy_places_list.layoutManager = LinearLayoutManager(this)

        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this,happyplacelist)
        rv_happy_places_list.adapter=placesAdapter

        placesAdapter.setOnClickListener(object :
            HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModels) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                startActivity(intent)
            }
        })



        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_ACTIVITY_FORUPDATE
                )

            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)


        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHapppyPlacesListfromLocalDB()

            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)
    }






    private fun getHapppyPlacesListfromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlacesList:ArrayList<HappyPlaceModels> = dbHandler.getHappyPlacesList()

        if(getHappyPlacesList.size > 0)
        {
            tv_no_records_available.visibility = View.GONE
            rv_happy_places_list.visibility = View.VISIBLE
            setupHappyPlaceRecyclerview(getHappyPlacesList)

        }
        else{
            tv_no_records_available.visibility = View.VISIBLE
            rv_happy_places_list.visibility = View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== ADD_ACTIVITY_FORUPDATE)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                getHapppyPlacesListfromLocalDB()
            }
            else
            {
                Log.e("Activity","Cancelled or BackPressed")
            }
        }
    }


    companion object{
        var ADD_ACTIVITY_FORUPDATE=1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}
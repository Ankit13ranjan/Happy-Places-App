package com.example.happyplaces.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.example.happyplaces.models.HappyPlaceModels
import kotlinx.android.synthetic.main.activity_add_happy_places.*
import kotlinx.android.synthetic.main.activity_happy_place_detail.*

class HappyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_place_detail)

        var happyPlaceDetailModel: HappyPlaceModels?=null
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel=
                intent.getSerializableExtra(
                    MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModels?
        }


        if(happyPlaceDetailModel != null)
        {
            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title=happyPlaceDetailModel.title

            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_images.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tv_description.text=happyPlaceDetailModel.description
            tv_location.text=happyPlaceDetailModel.location



            btn_view_on_map.setOnClickListener {
                val intent = Intent(this,MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,happyPlaceDetailModel)
                startActivity(intent)
            }
        }






    }
}
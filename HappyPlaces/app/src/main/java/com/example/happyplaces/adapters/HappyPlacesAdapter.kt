package com.example.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.activites.AddHappyPlaces
import com.example.happyplaces.activites.MainActivity
import com.example.happyplaces.databases.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModels
import kotlinx.android.synthetic.main.item_happy_place.view.*

class HappyPlacesAdapter(private val context: Context , private var list: ArrayList<HappyPlaceModels>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    private var onClickListener: OnClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false

            )
        )
    }


    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val models = list[position]

        if(holder is MyViewHolder){
            holder.itemView.iv_place_image.setImageURI(Uri.parse(models.image))
            holder.itemView.tvTitle.text=models.title
            holder.itemView.tvDescription.text=models.description

            holder.itemView.setOnClickListener{
                if(onClickListener !=null)
                {
                    onClickListener!!.onClick(position,models)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun removeAt(position:Int){
        val dbHandler = DatabaseHandler(context)
        val isDelete = dbHandler.deleteHappyPlace(list[position])
        if(isDelete>0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaces::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        ) // Activity is started with requestCode

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }



    interface OnClickListener{
        fun onClick(position: Int , model:HappyPlaceModels)
    }


    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

    }
}
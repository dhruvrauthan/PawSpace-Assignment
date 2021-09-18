package com.example.pawspaceassignment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawspaceassignment.activity.EditPetActivity
import com.example.pawspaceassignment.models.Pet
import android.text.format.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class PetsAdapter(private val pets: ArrayList<Pet>) :
    RecyclerView.Adapter<PetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pet_type.text= pets[position].type
        holder.pet_weight.text= pets[position].weight.toString()+" kg"
        holder.id= pets[position].id.toString()

        val timestamp= pets[position].timestamp
        val calendar= Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis= timestamp!!.seconds*1000
        holder.timestamp.text= DateFormat.format("dd-MM-yyyy HH:mm", calendar).toString()
    }

    override fun getItemCount(): Int {
        return pets.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        var pet_type= view.findViewById<TextView>(R.id.item_textview_pet_type)
        var pet_weight= view.findViewById<TextView>(R.id.item_textview_pet_weight)
        var timestamp= view.findViewById<TextView>(R.id.item_textview_ownerid)
        var id= ""
        var c= view.context

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent= Intent(c, EditPetActivity::class.java)
            intent.putExtra("id", id)
            c.startActivity(intent)
        }
    }

//    interface onCardClick{
//        fun onClick(position: Int)
//    }

}
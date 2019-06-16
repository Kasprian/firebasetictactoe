package com.example.firebasetictactoe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.letsbuildthatapp.kotlinmessenger.models.User
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.firebasetictactoe.NewGameActivity.Companion.USER_KEY
import com.google.firebase.auth.FirebaseAuth

class NewGameActivity : AppCompatActivity() {
    var userList: ArrayList<User> = ArrayList<User>()
    lateinit var ListAdapter: ListAdapter

    companion object {
        val USER_KEY = "USER_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playerslist)
        supportActionBar?.title = "Select User"

        fetchUsers()
        setUpListRecyclerView()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid!= FirebaseAuth.getInstance().uid) {
                        userList.add(user)
                    }
                    ListAdapter.changeDataSet(userList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    fun setUpListRecyclerView() {
        val listRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_onlineplayers)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        this.ListAdapter = ListAdapter(this.userList, this)
        listRecyclerView.adapter = this.ListAdapter
    }


}



class ListAdapter(var items: ArrayList<User>, val context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.user_row_new_game, parent, false)
        var viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener {
                val intent = Intent(context, Game::class.java)
                intent.putExtra(USER_KEY,findByName(viewHolder.username.text.toString()))

                (context as Activity).startActivityForResult(intent, 1)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.username.text =item.username

    }

    fun changeDataSet(newItems: ArrayList<User>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    fun findByName(name: String) : User? {
        return this.items.find { user -> user.username == name }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.username_textview_new_message)

    }
}

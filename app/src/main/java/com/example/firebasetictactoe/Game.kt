package com.example.firebasetictactoe

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.firebasetictactoe.models.Move
import com.example.firebasetictactoe.models.WhoseMove
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.letsbuildthatapp.kotlinmessenger.models.User
import kotlinx.android.synthetic.main.gameboard.*

class Game : AppCompatActivity() {
    var toUser: User? = null

    var me = ArrayList<Int>()
    var opponent = ArrayList<Int>()
    var activePlayer = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameboard)
        toUser = intent.getParcelableExtra<User>(NewGameActivity.USER_KEY)

        supportActionBar?.title = "Opponent: "+toUser?.username
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewGameActivity.USER_KEY)
        val toId = user.uid
        FirebaseDatabase.getInstance().getReference("/games/$toId/$fromId").child("options").
            addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot ) {
                if (!p0.exists()) {
                    FirebaseDatabase.getInstance().getReference("/games/$toId/$fromId/options/move").setValue(WhoseMove(fromId!!))
                }
            }override fun onCancelled(p0: DatabaseError) {

                }
        })
        FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId").child("options").
            addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot ) {
                    if (!p0.exists()) {
                        FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId/options/move").setValue(WhoseMove(fromId!!))
                    }
                }override fun onCancelled(p0: DatabaseError) {

                }
            })
        val ref = FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId/options")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val whoseMove = p0.getValue(WhoseMove::class.java) ?: return
               Log.d("Move","ruch"+whoseMove.whoseMove)
                if(whoseMove.whoseMove==fromId){
                    activePlayer=1
                    turn.text="YOUR TURN"
                }else{
                    activePlayer=2
                    turn.text="ENEMY TURN"
                }
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val whoseMove = p0.getValue(WhoseMove::class.java) ?: return
                if(whoseMove.whoseMove==fromId){
                    activePlayer=1
                    turn.text="YOUR TURN"
                }else{
                    activePlayer=2
                    turn.text="ENEMY TURN"
                }
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
        listenForMoves()
    }
    fun ButtonClick(view: View) {
        val selectedButton = view as Button
        var number = 0
        when(selectedButton.id) {
            R.id.button -> number = 1
            R.id.button2 -> number = 2
            R.id.button3 -> number = 3
            R.id.button4 -> number = 4
            R.id.button5 -> number = 5
            R.id.button6 -> number = 6
            R.id.button7 -> number = 7
            R.id.button8 -> number = 8
            R.id.button9 -> number = 9
        }
        if(activePlayer==1) {
            performSendMove(number)
            selectedButton.isEnabled = false
        }else{
            Toast.makeText(this, " Not your move!!", Toast.LENGTH_LONG).show()
        }
    }


    private fun listenForMoves() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newMove = p0.getValue(Move::class.java)
                if (newMove != null) {
                    me.add(newMove.number)
                    selectButton(newMove.number,1)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
        val toReference = FirebaseDatabase.getInstance().getReference("/games/$toId/$fromId")
        toReference.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newMove = p0.getValue(Move::class.java)
                if (newMove != null) {
                    opponent.add(newMove.number)
                    selectButton(newMove.number,2)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
    private fun selectButton(number: Int,color:Int) {
        if (color== 1) {
            when(number) {
                1 -> print1Button(findViewById<Button>(R.id.button))
                2 -> print1Button(findViewById<Button>(R.id.button2))
                3 -> print1Button(findViewById<Button>(R.id.button3))
                4 -> print1Button(findViewById<Button>(R.id.button4))
                5 -> print1Button(findViewById<Button>(R.id.button5))
                6 -> print1Button(findViewById<Button>(R.id.button6))
                7 -> print1Button(findViewById<Button>(R.id.button7))
                8 -> print1Button(findViewById<Button>(R.id.button8))
                9 -> print1Button(findViewById<Button>(R.id.button9))
            }
        } else {
            when(number) {
                1 -> print2Button(findViewById<Button>(R.id.button))
                2 -> print2Button(findViewById<Button>(R.id.button2))
                3 -> print2Button(findViewById<Button>(R.id.button3))
                4 -> print2Button(findViewById<Button>(R.id.button4))
                5 -> print2Button(findViewById<Button>(R.id.button5))
                6 -> print2Button(findViewById<Button>(R.id.button6))
                7 -> print2Button(findViewById<Button>(R.id.button7))
                8 -> print2Button(findViewById<Button>(R.id.button8))
                9 -> print2Button(findViewById<Button>(R.id.button9))
            }
        }
        isVictory()
    }
    private fun print1Button(selectedButton: Button){
        selectedButton.setBackgroundColor(Color.parseColor("#008B8B"))
        selectedButton.text = "X"
        selectedButton.isEnabled = false
    }
    private fun print2Button(selectedButton: Button){
        selectedButton.setBackgroundColor(Color.parseColor("#FF7F50"))
        selectedButton.text = "O"
        selectedButton.isEnabled = false
    }

    private fun performSendMove(number: Int) {

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewGameActivity.USER_KEY)
        val toId = user.uid

        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId").push()

        val newMove= Move(reference.key!!, number, fromId, toId)

        reference.setValue(newMove)
            .addOnSuccessListener {
                Log.d("Move", "Saved our chat message: ${reference.key}")
            }
        FirebaseDatabase.getInstance().getReference("/games/$toId/$fromId/options/move").setValue(WhoseMove(toId))
        FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId/options/move").setValue(WhoseMove(toId))

    }
    fun isVictory(){
        var winner=-1
        for(i in 1..7 step 3){
            if (me.contains(i ) && me.contains(i+1) && me.contains(i+2)){
                    winner=1
                }
            if (opponent.contains(i ) && opponent.contains(i+1) && opponent.contains(i+2)){
                winner=2
            }
        }
        for(i in 1..3){
            if(me.contains(i)&& me.contains(i+3) && me.contains(i+6)){
                winner=1
            }
            if(opponent.contains(i)&& opponent.contains(i+3) && opponent.contains(i+6)){
                winner=2
            }
        }
        if(me.contains(1) && me.contains(5) && me.contains(9)){
            winner=1
        }
        if(opponent.contains(1) && opponent.contains(5) && opponent.contains(9)){
            winner=2
        }
        if(me.contains(3) && me.contains(5) && me.contains(7)){
            winner=1
        }
        if(opponent.contains(3) && opponent.contains(5) && opponent.contains(7)){
            winner=2
        }
        if(winner != -1) {
            if (winner == 1) {
                Toast.makeText(this, " You won !!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, " You lost !!", Toast.LENGTH_LONG).show()
            }
            val fromId = FirebaseAuth.getInstance().uid
            val user = intent.getParcelableExtra<User>(NewGameActivity.USER_KEY)
            val toId = user.uid
            FirebaseDatabase.getInstance().getReference("/games/$toId/$fromId").removeValue()
            FirebaseDatabase.getInstance().getReference("/games/$fromId/$toId").removeValue()
            me.clear()
            opponent.clear()
            finish()
        }
    }
}

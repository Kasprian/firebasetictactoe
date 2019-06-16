package com.example.firebasetictactoe.models

class Move(val id: String, val number: Int, val fromId: String, val toId: String) {
    constructor() : this("",  0, "","")
}
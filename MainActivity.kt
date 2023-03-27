package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNewGame = findViewById<Button>(R.id.button1)
        val btnAbout = findViewById<Button>(R.id.button2)


        btnNewGame.setOnClickListener{
            val newGameIntent=Intent(this,GameScreen::class.java)
            startActivity(newGameIntent)
        }

// Create a nullable variable to hold the pop-up window instance
        var popupWindow: PopupWindow? = null

// Set onClickListener for the button
        btnAbout.setOnClickListener {
            if (popupWindow != null && popupWindow!!.isShowing) {
                // If the pop-up window is showing, dismiss it
                popupWindow!!.dismiss()
            } else {
                // If the pop-up window is not showing, create a new instance and show it
                popupWindow = PopupWindow(this)

                // Set the content view of the pop-up window to the layout you created
                val view = layoutInflater.inflate(R.layout.popup_about, null)
                popupWindow!!.contentView = view

                // Set the size of the pop-up window
                popupWindow!!.width = LinearLayout.LayoutParams.WRAP_CONTENT
                popupWindow!!.height = LinearLayout.LayoutParams.WRAP_CONTENT

                // Show the pop-up window
                popupWindow!!.showAsDropDown(btnAbout)
            }
        }




    }
    }


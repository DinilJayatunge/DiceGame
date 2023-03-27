package com.example.dicegame

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible

class GameScreen : AppCompatActivity() {

    var userRan1:Int=1
    var userRan2:Int=1
    var userRan3:Int=1
    var userRan4:Int=1
    var userRan5:Int=1
    var computerRan1:Int=1
    var computerRan2:Int=1
    var computerRan3:Int=1
    var computerRan4:Int=1
    var computerRan5:Int=1

    var ran: java.util.Random = java.util.Random()
    val sides= arrayOf<String>("die_face_1","die_face_1","die_face_2","die_face_3","die_face_4","die_face_5","die_face_6")
    val usernums= arrayOf<Int>(userRan1,userRan2,userRan3,userRan4,userRan5)
    val computernums= arrayOf<Int>(computerRan1,computerRan2,computerRan3,computerRan4,computerRan4)

    //--scores--
    var userSum:Int=0
    var computerSum:Int=0
    var target:Int=109

    var btnget1:Button?=null
    var btnget2:Button?=null
    var btnget3:Button?=null
    var btnget4:Button?=null
    var btnget5:Button?=null

    var btn1Tapped:Boolean=false
    var btn2Tapped:Boolean=false
    var btn3Tapped:Boolean=false
    var btn4Tapped:Boolean=false
    var btn5Tapped:Boolean=false

    var btnshuffle:Button?=null
    var btnscore:Button?=null
    var tvscore:TextView?=null
    var tvattempt:TextView?=null

    var throws:Int=0
    var attempt:Int=1

    var dialogLost:Dialog?=null
    var dialogLostBinding:View?=null
    var dialogWin:Dialog?=null
    var dialogWinBinding:View?=null

    //--for computer random rolls--
    var check:Boolean=true

    //--for tie state--
    var isTie:Boolean=false

    var user1img:ImageView?=null
    var user2img:ImageView?=null
    var user3img:ImageView?=null
    var user4img:ImageView?=null
    var user5img:ImageView?=null
    var computer1img:ImageView?=null
    var computer2img:ImageView?=null
    var computer3img:ImageView?=null
    var computer4img:ImageView?=null
    var computer5img:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        target = intent.getIntExtra("EXTRA_DATA", 109)
        println(target)

        dialogLost = Dialog(this)
        dialogLostBinding = layoutInflater.inflate(R.layout.popup_lost, null)

        dialogWin = Dialog(this)
        dialogWinBinding = layoutInflater.inflate(R.layout.popup_win, null)

        user1img = findViewById(R.id.user1)
        user2img = findViewById(R.id.user2)
        user3img = findViewById(R.id.user3)
        user4img = findViewById(R.id.user4)
        user5img = findViewById(R.id.user5)
        computer1img = findViewById(R.id.computer1)
        computer2img = findViewById(R.id.computer2)
        computer3img = findViewById(R.id.computer3)
        computer4img = findViewById(R.id.computer4)
        computer5img = findViewById(R.id.computer5)

        btnget1 = findViewById<Button>(R.id.btnget1)
        btnget2 = findViewById<Button>(R.id.btnget2)
        btnget3 = findViewById<Button>(R.id.btnget3)
        btnget4 = findViewById<Button>(R.id.btnget4)
        btnget5 = findViewById<Button>(R.id.btnget5)

        btnshuffle = findViewById<Button>(R.id.shuffle)
        btnscore = findViewById<Button>(R.id.btnscore)
        tvscore = findViewById<TextView>(R.id.score_view)
        tvattempt = findViewById<TextView>(R.id.tvattempt)

        initialize()

        btnget1?.setOnClickListener {
            btn1Tapped = !btn1Tapped
            if (btn1Tapped) {
                btnget1?.setBackgroundColor(getResources().getColor(R.color.teal_700));
            } else btnget1?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        }
        btnget2?.setOnClickListener {
            btn2Tapped = !btn2Tapped
            if (btn2Tapped) {
                btnget2?.setBackgroundColor(getResources().getColor(R.color.teal_700));
            } else btnget2?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        }
        btnget3?.setOnClickListener {
            btn3Tapped = !btn3Tapped
            if (btn3Tapped) {
                btnget3?.setBackgroundColor(getResources().getColor(R.color.teal_700));
            } else btnget3?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        }
        btnget4?.setOnClickListener {
            btn4Tapped = !btn4Tapped
            if (btn4Tapped) {
                btnget4?.setBackgroundColor(getResources().getColor(R.color.teal_700));
            } else btnget4?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        }
        btnget5?.setOnClickListener {
            btn5Tapped = !btn5Tapped
            if (btn5Tapped) {
                btnget5?.setBackgroundColor(getResources().getColor(R.color.teal_700));
            } else btnget5?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        }

        hideControllers()

        btnshuffle?.setOnClickListener {
            if(isTie){
                //--if the game tied--
                println("tied")
                hideControllers()
                generateUserValues()
                generateComputerValues()
                setImages()
                setScore()
            }
            //--if the game not tied
            if(throws<2){
                generateUserValues()

                if (throws==0){
                    println(throws)
                    generateComputerValues()
                    btnshuffle?.text="Re-throw"}
                else if(throws==1){
                    btnshuffle?.text="Final Re-Throw"}

                setImages()
                throws+=1

                resetButtons()

            }else if(throws==2){
                println(throws)
                generateUserValues()
                generateComputerValuesRandom()
                generateComputerValuesRandom()
                setImages()
                setScore()
                throws=0
            }

            if (throws==1){
                showControllers()
            }
        }
        btnscore?.setOnClickListener {
            if(throws==2){
                generateComputerValuesRandom()
                generateComputerValuesRandom()
                setImages()
            }
            setScore()
        }
    }



    private fun setScore() {
        attempt++
        tvattempt?.text = "Round $attempt"
        userSum = genSum(true)
        computerSum = genSum(false)

        tvscore?.text = "$computerSum/$userSum"

        if (userSum >= target || computerSum >= target) {
            checkWins()
        }

        hideControllers()
        resetButtons()
        btnshuffle?.text = "Throw"
        throws = 0
    }

    private fun generateUserValues() {
        for (i in 0 until usernums.size) {
            if (!buttonTapped(i + 1)) {
                usernums[i] = ran.nextInt(5) + 1
            }
        }
    }

    private fun buttonTapped(buttonIndex: Int): Boolean {
        return when (buttonIndex) {
            1 -> btn1Tapped
            2 -> btn2Tapped
            3 -> btn3Tapped
            4 -> btn4Tapped
            5 -> btn5Tapped
            else -> false
        }
    }

    private fun generateComputerValues() {
        for (i in computernums.indices) {
            computernums[i] = ran.nextInt(6) + 1
        }
    }

    private fun generateComputerValuesRandom() {
        val shouldReroll = ran.nextBoolean()
        println("generate random numbers for computer: $shouldReroll")

        if (shouldReroll) {
            println("should reroll")
            val buttons = arrayOf(btn1Tapped, btn2Tapped, btn3Tapped, btn4Tapped, btn5Tapped)

            for (i in computernums.indices) {
                if (!buttons[i]) {
                    computernums[i] = ran.nextInt(5) + 1
                }
            }

            // randomly determine which buttons to keep
            btn1Tapped = ran.nextBoolean()
            btn2Tapped = ran.nextBoolean()
            btn3Tapped = ran.nextBoolean()
            btn4Tapped = ran.nextBoolean()
            btn5Tapped = ran.nextBoolean()
        } else {
            println("should not reroll")
            // no rolls
        }
    }

    private fun checkWins(){
        print("check wins ran")
        if (userSum>=target || computerSum>=target){
            if(userSum==computerSum){
                //--tie state functions--
                println("tied")
                isTie=true
            }
            else if (userSum>computerSum){
                isTie=false
                //--user wins
                println("user won")
                dialogWinBinding?.let { dialogWin?.setContentView(it) }
                dialogWin?.setCancelable(true)
                dialogWin?.setCanceledOnTouchOutside(false)
                dialogWin?.setOnCancelListener{
                    finish()
                }
                dialogWin?.show()
            } else if(computerSum>userSum){
                isTie=false
                //--user lost
                println("use lost")
                dialogLostBinding?.let { dialogLost?.setContentView(it) }
                dialogLost?.setCancelable(true)
                dialogLost?.setCanceledOnTouchOutside(false)
                dialogLost?.setOnCancelListener{
                    finish()
                }
                dialogLost?.show()
            }
        }
    }

    private fun setImages() {
        val userDiceValues = usernums
        val computerDiceValues = computernums

        for (i in 0 until userDiceValues.size) {
            val userImageView = when (i) {
                0 -> user1img
                1 -> user2img
                2 -> user3img
                3 -> user4img
                else -> user5img
            }
            userImageView?.setImageResource(
                resources.getIdentifier(
                    sides[userDiceValues[i]],
                    "drawable",
                    "com.example.dicegame"
                )
            )
        }

        for (i in 0 until computerDiceValues.size) {
            val computerImageView = when (i) {
                0 -> computer1img
                1 -> computer2img
                2 -> computer3img
                3 -> computer4img
                else -> computer5img
            }
            computerImageView?.setImageResource(
                resources.getIdentifier(
                    sides[computerDiceValues[i]],
                    "drawable",
                    "com.example.dicegame"
                )
            )
        }
    }

    private fun resetButtons(){
        btnget1?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        btnget2?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        btnget3?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        btnget4?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))
        btnget5?.setBackgroundColor(getResources().getColor(R.color.backgroundcolor))

        btn1Tapped=false
        btn2Tapped=false
        btn3Tapped=false
        btn4Tapped=false
        btn5Tapped=false
    }

    private fun hideControllers(){

        println("hide")
        btnscore?.isVisible=false
        btnget1?.isVisible=false
        btnget2?.isVisible=false
        btnget3?.isVisible=false
        btnget4?.isVisible=false
        btnget5?.isVisible=false
    }

    private fun showControllers(){
        btnscore?.isVisible=true
        btnget1?.isVisible=true
        btnget2?.isVisible=true
        btnget3?.isVisible=true
        btnget4?.isVisible=true
        btnget5?.isVisible=true
    }

    private fun initialize() {
        val defaultDrawable = resources.getIdentifier(sides[0], "drawable", "com.example.dicegame")
        user1img?.setImageResource(defaultDrawable)
        user2img?.setImageResource(defaultDrawable)
        user3img?.setImageResource(defaultDrawable)
        user4img?.setImageResource(defaultDrawable)
        user5img?.setImageResource(defaultDrawable)
        computer1img?.setImageResource(defaultDrawable)
        computer2img?.setImageResource(defaultDrawable)
        computer3img?.setImageResource(defaultDrawable)
        computer4img?.setImageResource(defaultDrawable)
        computer5img?.setImageResource(defaultDrawable)

        usernums.fill(1)
        computernums.fill(1)
    }

    private fun genSum(user:Boolean):Int{
        if (user){
            for(i in 0..usernums.size-1){
                print(usernums[i])
                userSum=userSum+usernums[i]
            }
            return userSum
        }else{
            for(k in 0..computernums.size-1){
                print(computernums[k])
                computerSum=computerSum+computernums[k]
            }
            return computerSum
        }
    }
}
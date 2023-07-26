package com.example.tips

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIALTIPPERCENT = 15


class MainActivity : AppCompatActivity() {

    //Declare member variables
    // private lateinit var id: typeWidget

    private lateinit var etBaseAmt: EditText
    private lateinit var sb: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmt: TextView
    private lateinit var tvTotalAmt: TextView
    private lateinit var tvDesc: TextView
    private lateinit var etSplit: EditText
    private lateinit var tvTotalPerAmt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set pointers to object
        etBaseAmt = findViewById(R.id.etBaseAmt)
        sb = findViewById(R.id.sb)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmt = findViewById(R.id.tvTipAmt)
        tvTotalAmt = findViewById(R.id.tvTotalAmt)
        tvDesc = findViewById(R.id.tvDesc)
        etSplit = findViewById(R.id.etSplit)
        tvTotalPerAmt = findViewById(R.id.tvTotalPerAmt)

        sb.progress = INITIALTIPPERCENT
        tvTipPercentLabel.text = "$INITIALTIPPERCENT%"
        updateDesc(INITIALTIPPERCENT)

        // set a listener on seekbar
        // object is anonymous classes function
        sb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                computeSplit()
                updateDesc(p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        // set a listener on Base Amt
        etBaseAmt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                computeTipAndTotal()
                computeSplit()
            }
        })

        // set a listener on TipTextPercent
        etSplit.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                computeSplit()
            }
        })
    }

    private fun computeSplit() {
        if (etSplit.text.isEmpty()){
            tvTotalPerAmt.text = tvTotalAmt.text.toString()
            return
        }
        val total = computeTipAndTotal().toString().toDouble()
        val person = etSplit.text.toString().toDouble()
        val perPerson = total / person
        tvTotalPerAmt.text = "$ %,.2f".format(perPerson)
    }

    private fun updateDesc(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvDesc.text = tipDescription
        //update color
        val tipColor = ArgbEvaluator().evaluate(
            tipPercent.toFloat() /sb.max,
            ContextCompat.getColor(this, R.color.worst),
            ContextCompat.getColor(this,R.color.best)
        ) as Int
        tvDesc.setTextColor(tipColor)
    }

    private fun computeTipAndTotal(): Double {
        if (etBaseAmt.text.isEmpty()){
            tvTotalAmt.text = ""
            tvTipAmt.text = ""
            return 0.0
        }
        //Get base value and tip
        val baseAmt = etBaseAmt.text.toString().toDouble()
        val tipPercent = sb.progress
        //calc tip and total
        val tip = baseAmt * tipPercent / 100
        val total = tip + baseAmt
        //update UI
        tvTipAmt.text = "$ %,.2f".format(tip)
        tvTotalAmt.text = "$ %,.2f".format(total)
        return total
    }


}
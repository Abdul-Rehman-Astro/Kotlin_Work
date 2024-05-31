package com.example.calculator_kotlin


import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: EditText
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultText = findViewById(R.id.resultText)
    }

    fun onDigit(view: View) {
        if (stateError) {
            resultText.setText((view as Button).text)
            stateError = false
        } else {
            resultText.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            resultText.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            resultText.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric && !stateError) {
            val text = resultText.text.toString()
            try {
                val result = evaluate(text)
                resultText.setText(result.toString())
                lastDot = true
            } catch (e: Exception) {
                resultText.setText("Error")
                stateError = true
                lastNumeric = false
            }
        }
    }

    fun onClear(view: View) {
        resultText.setText("")
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    private fun evaluate(expression: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm() // addition
                    else if (eat('-'.toInt())) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor() // multiplication
                    else if (eat('/'.toInt())) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus

                var x: Double
                val startPos = this.pos
                if (eat('('.toInt())) { // parentheses
                    x = parseExpression()
                    eat(')'.toInt())
                } else if ((ch >= '0'.toInt() && ch <= '9'.toInt()) || ch == '.'.toInt()) { // numbers
                    while ((ch >= '0'.toInt() && ch <= '9'.toInt()) || ch == '.'.toInt()) nextChar()
                    x = expression.substring(startPos, this.pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }

                return x
            }
        }.parse()
    }
}




//
//
//package com.example.basiccalculator
//
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var result: EditText
//    private var operand1: Double = 0.0
//    private var operand2: Double = 0.0
//    private var pendingOperation = "="
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        result = findViewById(R.id.result)
//
//        val listener = View.OnClickListener { v ->
//            val b = v as Button
//            result.append(b.text)
//        }
//
//        findViewById<Button>(R.id.btn0).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn1).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn2).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn3).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn4).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn5).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn6).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn7).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn8).setOnClickListener(listener)
//        findViewById<Button>(R.id.btn9).setOnClickListener(listener)
//        findViewById<Button>(R.id.btnDot).setOnClickListener(listener)
//
//        val opListener = View.OnClickListener { v ->
//            val b = v as Button
//            val op = b.text.toString()
//            val value = result.text.toString()
//            if (value.isNotEmpty()) {
//                performOperation(value, op)
//            }
//            pendingOperation = op
//        }
//
//        findViewById<Button>(R.id.btnAdd).setOnClickListener(opListener)
//        findViewById<Button>(R.id.btnSub).setOnClickListener(opListener)
//        findViewById<Button>(R.id.btnMul).setOnClickListener(opListener)
//        findViewById<Button>(R.id.btnDiv).setOnClickListener(opListener)
//        findViewById<Button>(R.id.btnEquals).setOnClickListener(opListener)
//        findViewById<Button>(R.id.btnClear).setOnClickListener {
//            operand1 = 0.0
//            operand2 = 0.0
//            pendingOperation = "="
//            result.setText("")
//        }
//    }
//
//    private fun performOperation(value: String, operation: String) {
//        if (pendingOperation == "=") {
//            operand1 = value.toDouble()
//        } else {
//            operand2 = value.toDouble()
//            if (pendingOperation == "+") {
//                operand1 += operand2
//            } else if (pendingOperation == "-") {
//                operand1 -= operand2
//            } else if (pendingOperation == "*") {
//                operand1 *= operand2
//            } else if (pendingOperation == "/") {
//                operand1 /= operand2
//            }
//        }
//        result.setText(operand1.toString())
//    }
//}

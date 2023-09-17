package com.example.empyapplication

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import java.util.regex.Pattern

/**
 * a single grey box on in the game UI
 */
class LetterEditor : androidx.appcompat.widget.AppCompatEditText {
    private val letterEditors: MutableList<LetterEditor>
    val tag = "main"
    private val index: Int

    /**
     * creates the row.
     */
    constructor(
        context: Context,
        index: Int,
        rows: MutableList<LetterEditor>
    ) : super(context) {
        this.index = index
        this.letterEditors = rows

        setBackgroundColor(Color.GRAY)
        var layout = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            200
        )
        layout.setMargins(10, 10, 10, 10)
        this.gravity = Gravity.CENTER

        layout.weight = 1.0f
        this.layoutParams = layout
        this.inputType =
            InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val pattern = Pattern.compile("[a-zA-Z]+")
            if (!pattern.matcher(source).matches()) {
                ""
            } else {
                null // Accept alphabetic characters
            }
            // Accept alphabetic characters

        }
        this.filters = arrayOf(
            filter,
            InputFilter.LengthFilter(1),
            InputFilter.AllCaps(),
        )



        //handles moving to the next element when user enters a character
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text changes
                val inputText = s.toString()
                // You can do something with the inputText here
                if (s == "") {
                    if (this@LetterEditor.index - 1 >= 0) {
                        val next = this@LetterEditor.letterEditors[this@LetterEditor.index - 1]
                        next.requestFocus()
                    }
                }
                else {
                    if (this@LetterEditor.index + 1 <= 4) {
                        val next = this@LetterEditor.letterEditors[this@LetterEditor.index + 1]
                        next.requestFocus()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //if the string is null, move to previous box
                val inputText = s.toString()
                if (inputText == "") {
                    if (this@LetterEditor.index - 1 >= 0) {
                        val next = this@LetterEditor.letterEditors[this@LetterEditor.index - 1]
                        next.requestFocus()
                }
                }
                // This method is called after the text changes

            }
        })




    }



}
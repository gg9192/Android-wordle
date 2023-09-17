package com.example.empyapplication

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rows: MutableList<LetterRow>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    /** frees each row from the parent so we can use them again in a new fragment.
     * If we don't do this before we switch fragments, we can never use this UI
     * item again
     */
    private fun freeRows() {
        val parentActivity = requireActivity() as MainActivity
        val rows = parentActivity.rows()
        for (row in rows) {
            val view:View = requireView()
            view.findViewById<LinearLayout>(R.id.gameFragLayout).removeView(row)
        }
    }

    /**
     * Gives the feedback string for the given guess
     * See the docs for LetterRow.showFeedback(...)
     * @oaram guess the guess
     */
    private fun getFeedback(guess:String):String {
        val parentActivity = requireActivity() as MainActivity
        val correct = parentActivity.getCorrectWord()
        val sb = StringBuilder()
        for (i in 0..4) {
            if (correct[i] == guess[i]) {
                sb.append('c')
            }
            else if (correct.contains(guess[i])) {
                sb.append('i')
            }
            else {
                sb.append('n');
            }
        }
        return sb.toString()
    }

    /**
     * if the user submits a word, we want to display a new row.
     * This adds the last row of row list to the view
     */
    private fun renderLastRow() {
        val parentActivity = requireActivity() as MainActivity
        rows = parentActivity.rows()
        val last = rows.last()
        val view:View = requireView()
        view.findViewById<LinearLayout>(R.id.gameFragLayout).addView(last)
    }

    /**
     * Renders the rows for the game
     * @param view the main view
     */
    private fun renderRows(view:View) {
        /*
            User
            get a reference to the current activity kotlin android studip
            ChatGPT
            To get a reference to the current activity in an Android app using Kotlin, you can use the this keyword or the activity property depending on the context in which you need it. Here are two common ways to do it:

            Inside an Activity:

            If you are inside an Activity class, you can simply use this to reference the current activity.

            kotlin
            Copy code
            val currentActivity = this
            Inside a Fragment:

            If you are inside a Fragment and need a reference to the hosting Activity, you can use the activity property.

            kotlin
            Copy code
            val currentActivity = activity
            Remember that when using the activity property in a Fragment, you should be cautious about potential nullability. The activity property can be null if the Fragment is not currently attached to an Activity. You should check for null before using it or use safe calls (?.) to handle this situation.

            Here's an example of how you might use it in a Fragment safely:

            kotlin
            Copy code
            val currentActivity = activity
            if (currentActivity != null) {
                // Do something with currentActivity
            } else {
                // Handle the case where the Fragment is not attached to an Activity
            }
            Make sure you're using these references in the appropriate context based on your specific requirements within your Android app.




            User
            I did val currentActivity = activity
            ChatGPT
            Great, using val currentActivity = activity is a valid way to obtain a reference to the current hosting Activity within a Fragment in an Android app. Just remember to handle potential nullability, as the activity property can be null in certain situations.

            If you're certain that your Fragment is only used when it's attached to an Activity, and you don't expect it to be detached during its lifecycle, then using val currentActivity = activity should work perfectly fine. However, it's always a good practice to check for null or use safe calls (?.) if there's a chance that the Fragment might be detached at some point.




            User
            why am i not able to access a method associated with the class
            ChatGPT
            If you're not able to access a method associated with a class in Android, there could be several reasons for this issue. Here are some common reasons and solutions:

            Scope and Access Modifier: Ensure that the method you are trying to access is declared with the correct access modifier (e.g., public, protected, private, or package-private). If it's declared as private, it can only be accessed within the same class. If it's declared as protected, it can be accessed from subclasses. If it's public, it should be accessible from anywhere within your code.

            Import Statements: Make sure you have imported the correct package or class where the method is defined. Incorrect imports can lead to method access issues.

            Class Inheritance: If the method is defined in a superclass, ensure that you are extending the correct class in your subclass to access the method. Also, make sure the method is not private in the superclass if you intend to override it in a subclass.

            Instance vs. Static Method: Ensure that you are calling the method correctly based on whether it's an instance method (called on an object) or a static method (called on the class itself). Static methods are accessed using the class name, while instance methods are called on objects.

            Constructor or Initialization: If the method you are trying to access is part of an instance of the class, ensure that you have correctly initialized the object before calling the method. If you are working with an Android Activity or Fragment, make sure it's properly created and attached.

            Method Signature: Check the method's signature, including its name and parameter types, to make sure you are calling it with the correct arguments.

            NullPointerException: If you are encountering a NullPointerException, it means that you are trying to access a method or property on a null object. Ensure that the object you are calling the method on is properly initialized and not null.

            Proguard/R8: If you are using code obfuscation tools like Proguard or R8, they can sometimes obfuscate class and method names, making it challenging to access them. You may need to configure your Proguard/R8 rules to keep the necessary class and method names intact.

            Compilation Errors: Check your code for compilation errors. If there are errors in your code, the IDE or build system may prevent you from accessing the method until you resolve those errors.

            If you can provide more specific details about the error message you are encountering or the code you are working with, I can provide more targeted assistance.




            User
            val rows = activity.getRows();
            ChatGPT
            It seems like you are trying to call a method named getRows() on the activity object. To do this, make sure the following points are true:

            The activity object is correctly initialized and not null.
            The getRows() method is a public or protected method within the activity's class or a class that the activity class inherits from.
            The activity class has been properly imported or is within the same package as the class where you are trying to make this call.
            There are no compilation errors or issues with your project setup.
            If you are working with an Android Activity, you should typically have access to its methods directly. Ensure that you are calling getRows() within a valid context, such as inside an Activity or a Fragment associated with the current activity.

            If you are encountering specific error messages or issues, please provide more details, and I can offer more targeted assistance.




            User
            i have get rows in the parent activity and I am calling get rows from a child fragment
            ChatGPT
            If you have a method called getRows() in the parent Activity and you want to call it from a child Fragment, you need to make sure that the child Fragment has a reference to the parent Activity. You can achieve this by using a cast to the appropriate Activity type.

            Here's how you can do it:

            In the child Fragment, you need to obtain a reference to the parent Activity. You can do this by casting the requireActivity() result to the type of your parent Activity.

            For example, if your parent Activity is named ParentActivity, you would cast it like this:

            kotlin
            Copy code
            val parentActivity = requireActivity() as ParentActivity
            Make sure to replace ParentActivity with the actual name of your parent Activity class.

            Once you have the reference to the parent Activity, you can call its getRows() method:

            kotlin
            Copy code
            val rows = parentActivity.getRows()
            Please note that this approach assumes that the parent Activity is of the correct type (ParentActivity). If it's not, or if there's a chance it could be a different Activity type, you should perform type checking and error handling to avoid crashes. You can use the is keyword to check the type or use safe casts (as?) to handle potential type mismatches.

            Here's an example with type checking:

            kotlin
            Copy code
            val parentActivity = requireActivity()
            if (parentActivity is ParentActivity) {
                val rows = parentActivity.getRows()
                // Use 'rows' as needed
            } else {
                // Handle the case where the parent Activity is not of the expected type
            }
            This ensures that you only call getRows() if the parent Activity is of the correct type and avoids runtime errors.

        */
        val parentActivity = requireActivity() as MainActivity
        val rows = parentActivity.rows()
        for (row in rows) {
            view.findViewById<LinearLayout>(R.id.gameFragLayout).addView(row)
        }
    }

    /**
     * method for user or autoplay to submit word, handles all UI updates
     */
    public fun submitWord() {
        Log.d("SubmitWord", "made it gere")
        val vieww = this.requireView()
        val parentActivity = requireActivity() as MainActivity
        val rows = parentActivity.rows()

        val guess = rows.last().getWord()
        Log.d("submitted", guess)
        if (guess.length != 5) {
            freeRows()
            parentActivity.gotoInvalid();
        }
        else {
            val feedback = getFeedback(guess)
            if (feedback == "ccccc") {
                parentActivity.setGameOver(true)
                rows.last().showFeedback(feedback)
                vieww.findViewById<TextView>(R.id.botText).setText("You Win!")
            }
            else {
                rows.last().showFeedback(feedback)
                val manager = AutoplayGuessManager.getInstance()
                if (rows.size >= 6) {
                    if (parentActivity.getGameOver() == false) {
                        parentActivity.setGameOver(true)
                        var w = parentActivity.getCorrectWord()
                        vieww.findViewById<TextView>(R.id.botText).setText("You Lose! The word was $w")

                    }
                }
                else {

                    manager!!.enqueueJob(parentActivity,parentActivity.getAutoplay(), feedback,guess)
                    parentActivity.createRow()
                    renderLastRow()
                }
            }

        }
        Log.d("SubmitWord", "exited")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fraqgment
        var vieww = inflater.inflate(R.layout.fragment_game, container, false)
        renderRows(vieww)

        val parentActivity = requireActivity() as MainActivity
        //attatch the event listener to the button
        var submit = vieww.findViewById<Button>(R.id.submit).
        setOnClickListener{
            if (!parentActivity.getAutoplay()) {
                submitWord()
            }
            else {
                Toast.makeText(parentActivity, "autoplay is enabled, can't submit", Toast.LENGTH_SHORT).show();
            }
        }
        var autoplay = vieww.findViewById<Button>(R.id.autoplay).setOnClickListener{

            val manager = AutoplayGuessManager.getInstance()
            if (!parentActivity.getAutoplay()) {
                //if autoplay is not enabled, start it
                manager!!.enqueueJob(parentActivity, true, "nnnnn", "22222")
                parentActivity.setAutoplay(true)
            }
        }

        return vieww
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment GameFragment.
         */
        @JvmStatic
        fun newInstance() =
            GameFragment()
    }
}
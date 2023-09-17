package com.example.empyapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.util.Locale
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val tag = "main"
    private val rows = mutableListOf<LetterRow>()
    private lateinit var word:String
    private var gameOver = false
    private lateinit var gameFragment:GameFragment;
    private var autoplay = false

    /**
     * When our AutoplayGuessManager has a recommended guess,
     * this listener is called
     *  @param the word to submit
     */
    fun recieveRecomendation(rec: String) {
        val runnable = WordSubmitter(this,rec)
        runOnUiThread(runnable)
    }

    /**
     * returns the current GameFragment associated with the current activity
     */
    fun getGameFragment():GameFragment {
        return this.gameFragment
    }

    /**
     * returns the status of autoplay
     * @return true for enabled, false for not
     */
    fun getAutoplay():Boolean {
        return this.autoplay
    }

    /**
     * sets the autoplay to the given value
     * @param value boolean, true if enabled, false for not
     */
    fun setAutoplay(value:Boolean) {
        this.autoplay = value
    }

    /**
     *  Gets the rows that represents the users guesses
     *  @return the rows
     */
    fun rows(): MutableList<LetterRow> {
        return rows
    }

    /**
     *  Creates a row for the user to type in in the array
     */
    fun createRow(): LetterRow {
        val row = LetterRow(this)
        rows.add(row)

        return row
    }

    /**
     * Gets the string that was selected
     * @return the correct word
     */
    fun getCorrectWord(): String {
        return this.word
    }

    /**
     * @returns true if the game is over, false if not
     */
    fun getGameOver():Boolean {
        return this.gameOver
    }

    /**
     * Sets the game over flag to the given value
     * @param value
     */
    fun setGameOver(value:Boolean) {
        this.gameOver = value
    }

    /**
     * changes the fragment to invalidInput
     */
    fun gotoInvalid() {
        val invalidInputFragment: Fragment = InvalidInputFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainerView3, invalidInputFragment).commit()
    }

    /**
     * starts the game fragment
     */
    fun gotoGame() {
        val gameFragment: GameFragment = GameFragment.newInstance()
        this.gameFragment = gameFragment
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragmentContainerView3, gameFragment).commit()
    }

    /**
     * called when the activity is started
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createRow()
        lateinit var words: MutableList<String>

        thread(start = true) {
            /*
            *User
                Load screen -- You should implement a load screen. The application should start by displaying a "Loading..." text. During this time, the file that contains all the words is loaded in memory, and a random word is selected to be the word to be guessed. Ensure that the "Loading..." text is displayed for 3 seconds or until the file is loaded (whichever takes longer). You can access a complete list of 5 letter words here Download 5 letter words here.
                kotlin app dev

                ChatGPT
                To implement a load screen in a Kotlin Android app, you can use a combination of Handler, Thread, and AsyncTask to load the word file and display the "Loading..." text for at least 3 seconds or until the file is loaded. Here's a step-by-step guide on how to do this:

                First, download the list of 5-letter words from the provided link and include it in your project's assets folder. Save it as a text file (e.g., words.txt).

                Create a layout file for your load screen (e.g., activity_load_screen.xml) with a TextView to display the "Loading..." text.

                In your LoadScreenActivity, set the content view to the activity_load_screen.xml layout.

                Create a function to load the word file and select a random word from it. This should be done in a background thread to avoid blocking the main UI thread. You can use Kotlin's AsyncTask for this purpose.

                Use a Handler to delay the transition to the next screen (e.g., the game screen) for at least 3 seconds or until the file is loaded.

                Here's a sample implementation:

                kotlin
                Copy code
                import android.content.res.AssetManager
                import android.os.AsyncTask
                import android.os.Bundle
                import android.os.Handler
                import android.support.v7.app.AppCompatActivity
                import android.widget.TextView
                import java.io.BufferedReader
                import java.io.IOException
                import java.io.InputStreamReader
                import java.util.*

                class LoadScreenActivity : AppCompatActivity() {

                    private lateinit var loadingText: TextView

                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContentView(R.layout.activity_load_screen)

                        loadingText = findViewById(R.id.loadingText)

                        // Load the word file and transition to the next screen after 3 seconds or when the file is loaded.
                        LoadWordsTask().execute(assets)
                    }

                    inner class LoadWordsTask : AsyncTask<AssetManager, Void, String>() {

                        override fun doInBackground(vararg params: AssetManager): String {
                            val assetManager = params[0]
                            try {
                                // Load the list of words from the assets folder
                                val inputStream = assetManager.open("words.txt")
                                val reader = BufferedReader(InputStreamReader(inputStream))
                                val words = ArrayList<String>()

                                var line: String?
                                while (reader.readLine().also { line = it } != null) {
                                    words.add(line!!)
                                }

                                // Select a random word from the loaded list
                                val random = Random()
                                val randomWord = words[random.nextInt(words.size)]

                                return randomWord
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            return ""
                        }

                        override fun onPostExecute(result: String) {
                            // Display the loaded word (you can do something with it)
                            // For now, we'll just transition to the next screen after 3 seconds
                            val handler = Handler()
                            handler.postDelayed({
                                // Start the next activity (e.g., game activity)
                                // Intent intent = new Intent(LoadScreenActivity.this, GameActivity.class);
                                // startActivity(intent);

                                // Finish the current activity to prevent going back to it
                                finish()
                            }, 3000) // 3 seconds delay
                        }
                    }
                }
                In this code, we load the list of words from the words.txt file in the assets folder, select a random word, and then use a Handler to delay the transition to the next screen for 3 seconds. You can replace the comment with the actual code to start the game activity when ready.




                User
                heres my implementation
                ChatGPT
                I see that you want to provide your implementation for review. However, I don't have access to external files or your specific implementation. If you have a specific question or need assistance with a particular aspect of your implementation, please share the relevant code or describe the issue you're facing, and I'll be happy to provide guidance or suggestions.




                User
                package com.example.empyapplication

                import android.os.Bundle
                import android.util.Log
                import android.view.View
                import android.widget.LinearLayout
                import androidx.appcompat.app.AppCompatActivity
                import androidx.fragment.app.Fragment
                import androidx.fragment.app.FragmentActivity
                import java.io.BufferedReader
                import kotlin.concurrent.thread

                class MainActivity : AppCompatActivity() {
                    private val tag = "main"
                    private val rows = mutableListOf<LetterRow>()



                    fun rows(): MutableList<LetterRow> {
                        return rows
                    }

                    fun createRow(): LetterRow {
                        val row = LetterRow(this)
                        rows.add(row)

                        return row
                    }


                    /*
                    * changes the fragment to invalid
                    */
                    fun gotoInvalid() {
                        val invalidInputFragment: Fragment = InvalidInputFragment.newInstance("", "");
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView3, invalidInputFragment).commit()
                    }

                    fun gotoGame() {
                        val gameFragment: Fragment = GameFragment.newInstance("", "");
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView3, gameFragment).commit()
                    }

                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContentView(R.layout.activity_main)

                        createRow()
                        lateinit var words: MutableList<String>

                        thread(start = true) {
                            val raw = resources.openRawResource(R.raw.words);
                            words = mutableListOf<String>()
                            val br = BufferedReader(raw.reader())
                            br.forEachLine {
                                words.add(it)
                            }
                        }
                        thread(start = true) {
                            Thread.sleep(3000)
                            Log.d("Done","asdasdasd")



                        }

                    }


                }

                ChatGPT
                It seems you've shared your MainActivity code. This code appears to be related to an Android application, and it includes functionality for handling fragments, managing letter rows, and loading words from a raw resource file.

                Here are a few observations and recommendations for your code:

                Multithreading: You are using Kotlin's thread function to load words from a raw resource file and add a delay. It's essential to be cautious when working with threads in Android to avoid issues like race conditions or UI updates on the wrong thread. If you need to update the UI after loading the words, you should use the runOnUiThread method.

                Initialization: In your onCreate method, you create a row using the createRow method. However, you don't seem to be using this row in your layout or fragment transactions. Make sure that you add this row to your layout or use it as needed in your app's logic.

                Fragment Transactions: You have methods gotoInvalid and gotoGame for replacing fragments. Ensure that you have corresponding FragmentContainerView elements in your layout XML file with the id attributes fragmentContainerView3 to host these fragments.

                Resource Management: When you load resources like the raw word list, it's a good practice to close the resources after you're done with them. You can use a use block for this purpose:

                kotlin
                Copy code
                resources.openRawResource(R.raw.words).use { raw ->
                    words = mutableListOf<String>()
                    val br = BufferedReader(raw.reader())
                    br.forEachLine {
                        words.add(it)
                    }
                }
                Logging: You've included a Log.d statement to log "Done" after a 3-second delay. Make sure you're using this log for debugging purposes, and you can remove it when it's no longer needed.

                Remember to use proper exception handling and error checking, especially when dealing with resources, to ensure a robust and reliable application. Additionally, consider how you plan to use the loaded words in your application and implement the corresponding logic accordingly.




                User
                how do i ensure that the thread runs for at least 3 sec
                ChatGPT
                To ensure that a thread runs for at least 3 seconds, you can use a combination of thread sleep and a timestamp comparison. Here's how you can modify your code to achieve this:

                kotlin
                Copy code
                thread(start = true) {
                    val startTime = System.currentTimeMillis()

                    // Your background tasks here
                    // For example, loading data or performing operations

                    // Calculate how much time has passed since starting the thread
                    val elapsedTime = System.currentTimeMillis() - startTime

                    // Check if at least 3 seconds have passed
                    val minimumExecutionTime = 3000 // 3 seconds in milliseconds
                    if (elapsedTime < minimumExecutionTime) {
                        // Sleep for the remaining time to ensure it runs for at least 3 seconds
                        val remainingTime = minimumExecutionTime - elapsedTime
                        Thread.sleep(remainingTime)
                    }

                    // Now you can continue with any tasks after the 3-second delay
                    Log.d("Done", "asdasdasd")
                }
                In this code, we record the start time before running your background tasks. Afterward, we calculate the elapsed time. If the elapsed time is less than 3 seconds, we calculate the remaining time needed to reach 3 seconds and use Thread.sleep to pause the thread for that duration. This ensures that the thread runs for at least 3 seconds before proceeding with any further tasks.
            */

            val startTime = System.currentTimeMillis()
            val raw = resources.openRawResource(R.raw.words);

            words = mutableListOf<String>()
            val br = BufferedReader(raw.reader())
            br.forEachLine {
                words.add(it.uppercase(Locale.getDefault()))
            }
            raw.close()
            word = words.random()
            AutoplayGuessManager.getInstance(words, this)
            Log.d("word", "the random word was $word")
            val elapsedTime = System.currentTimeMillis() - startTime
            // Check if at least 3 seconds have passed
            val minimumExecutionTime = 3000 // 3 seconds in milliseconds
            if (elapsedTime < minimumExecutionTime) {
                // Sleep for the remaining time to ensure it runs for at least 3 seconds
                val remainingTime = minimumExecutionTime - elapsedTime
                Thread.sleep(remainingTime)
                gotoGame()
            }



        }


    }


}

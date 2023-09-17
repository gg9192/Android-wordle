package com.example.empyapplication

import android.renderscript.RSInvalidStateException
import android.util.Log
import androidx.compose.ui.hapticfeedback.HapticFeedback

/**
 * This runnable serves as a template for runnable for threads
 * that removes possibilities that are inconsistent for feedback
 * received
 */
class WordPrunerTask: Runnable {
    private val main:MainActivity // a reference to the main activity
    private val notify:Boolean // do we notify the main class that we have a new guess
    private val feedback:String // the feedback we received on our guess
    private val guess:String // the word we guessed
    private var words:MutableList<String> // the remaining candidate words
    private val jobManager:AutoplayGuessManager // the original job manager
    private val allFeedbacks:MutableList<String> //Something used to debug the concurrency
    private val isFirst:Boolean //if this is the first word, we want to guess “salet”
        // see https://www.makeuseof.com/best-wordle-starter-words/#:~:text=Salet,the%20word%20of%20the%20day.

    constructor(main:MainActivity, words:MutableList<String>, notify:Boolean, feedback: String,
                guess:String, jobManager: AutoplayGuessManager, allFeedbacks: MutableList<String>,
        isFirst:Boolean) {
        this.main = main
        this.notify = notify
        this.feedback = feedback
        this.guess = guess
        this.words = words
        this.jobManager = jobManager
        this.allFeedbacks = allFeedbacks
        this.isFirst = isFirst
    }

    /**
     * tests if the curWord should be in the list
     * based on feedback on our guess, return true if so.
     */
    private fun testWord(curWord:String, feedback:String, guess:String):Boolean {
        var s = HashSet<Char>()
        val correct = main.getCorrectWord()
        for (ch in curWord) {
            s.add(ch)
        }
        Log.d("testWord", "current: " + curWord + ", ")
        for (i in 0..4) {
            val guessChar = guess[i]
            val fc = feedback[i]
            if (fc == 'c' && correct[i] != curWord[i]) {
                return false
            }
            if (fc == 'i' && (!s.contains(guessChar))) {
                return false
            }
            if (fc == 'n' && s.contains(guessChar)) {
                return false
            }

        }
        return true
    }

    /**
     * This scores a word, higher score is better
     * the idea here is greedy. We want to maximise information gained.
     * @param word the word to score
     */
    private fun scoreWord(word:String): Int {
        var total = 0
        for (ch in word) {
            if (!(jobManager.getSeen().contains(ch))) {
                total += 1
            }
        }
        return total
    }

    /**
     * gets the next guess
     * @return the next guess
     */
    private fun getGuess():String {
        var max = 0
        var res = this.words[0]
        for (str in words) {
            val score = scoreWord(str)
            if (score > max) {
                res = str
                max = score
            }
        }
        return res
    }

    /**
     * remove all words inconsistent with feedback
     * @return the new wordList
     */
    fun pruneWords():MutableList<String> {
        Log.d("pruner, ", jobManager.getFeedback().toString())
        val correct = this.main.getCorrectWord()
        val list = mutableListOf<String>()
        for (str in this.words) {
            if (testWord(str,this.feedback, this.guess)) {
                list.add(str)
            }
            else {
                if (correct == str) {
                    throw Exception("removed the correct word in autoplay")
                }
            }
        }
        return list
    }

    /**
     * When the thread is run, it removes words inconsistent with the feedback received
     * Then, it provides a guess if notify flag is true
     */
    override fun run() {
        val startTime = System.currentTimeMillis()
        val newWords = pruneWords()
        var elapsedTime = System.currentTimeMillis() - startTime
        val lenDiff = this.words.size - newWords.size
        this.words = newWords
        jobManager.setWords(newWords)
        jobManager.setThread()

        Log.d("prunerThread", "prunner 1 iteration in $elapsedTime ms, removed $lenDiff words!")
        Log.d("prunerThread", "just pruned for" + allFeedbacks.toString())
        if (notify) {
            //we need the result
            val guess = getGuess()

            val timeDiff = System.currentTimeMillis() - startTime
            Log.d("timeDiff", timeDiff.toString())
            //make the computer act like it's thinking like a human
            if (timeDiff < 3000) {
                Thread.sleep(3000 - timeDiff)
            }
            this.words.remove(guess)
            jobManager.setWords(newWords)
            if (isFirst) {
                main.recieveRecomendation("SALET")
            }
            else {
                main.recieveRecomendation(guess)
            }
            jobManager.setThread()
            jobManager.runJob()
            elapsedTime = System.currentTimeMillis() - startTime
            Log.d("pruner", "completely finished 1 itteration in " + elapsedTime)
        }
    }
}
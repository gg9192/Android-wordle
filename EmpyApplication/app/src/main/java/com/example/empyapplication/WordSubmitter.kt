package com.example.empyapplication

/**
 * If we don't update the UI on the UI thread, it
 * throws an exception. This class serves as a
 * template for processing UI update requests.
 * This passed to the main thread for running
*/
class WordSubmitter:Runnable {
    val main:MainActivity
    val submit:String

    /**
    * @param main the main activity
    * @param word the word to submit
    */
    constructor(main:MainActivity, word:String) {
        this.main = main
        this.submit = word
    }

    /**
     * This will modify the last row and submit the word
     */
    override fun run() {
        main.rows().last().setWord(submit)
        this.main.getGameFragment().submitWord()
    }
}
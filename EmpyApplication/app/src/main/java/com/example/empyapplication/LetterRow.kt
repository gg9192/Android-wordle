package com.example.empyapplication

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.empyapplication.MainActivity

/**
 * represents a single row of letters in the game
 */
class LetterRow : LinearLayoutCompat {
    val letters = mutableListOf<LetterEditor>()
    var finalized = false //if true, no more edits can happen on the row

    /** Creates a row of letters
     * @param context the main activity
     */
    constructor(context: Context) : super(context) {
        this.layoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
        )
        this.orientation = LinearLayoutCompat.HORIZONTAL

        var letterWidgets = mutableListOf<EditText>()

        //adds each letter editor
        val rowIndex = 0
        for (i in 0..4) {
            var letterEditor = LetterEditor(context, i, letters)
            letters.add(letterEditor)
            this.addView(letterEditor)
        }

    }

    /**
     * Changes the UI to display the given word to the user on this row
     * @param str the word to display
     */
    fun setWord(str:String) {
        for (i in 0..4) {
            var le = letters[i]
            val charseq:CharSequence = str[i] + ""
            le.setText(charseq, TextView.BufferType.EDITABLE)
        }

    }



    /**
    * changes the colors of the current row based on feedback string given
    * the index of the letter is the index of the letterEditor array
    * c: correct letter at correct index
    * i: correct letter at incorrect index
    * n: character does not exist
    */
    fun showFeedback(feedback: String) {

        Log.d("showfeed", "made it here")
        for (i in 0..4) {
            /*  User
                add a boarder
                ChatGPT
                To add a border to an AppCompatEditText, you can create a custom drawable XML resource that defines the appearance of the border and set it as the background of the AppCompatEditText. Here's how to do it:

                Create a Drawable XML Resource: Create an XML file in your res/drawable directory (you can name it, for example, edittext_border.xml) and define the border appearance in this XML resource. Here's an example of a simple border:
                xml
                Copy code
                <?xml version="1.0" encoding="utf-8"?>
                <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
                    <!-- Background -->
                    <item>
                        <shape android:shape="rectangle">
                            <solid android:color="@android:color/white" /> <!-- Background color -->
                            <stroke
                                android:width="2dp" <!-- Border width -->
                                android:color="@android:color/black" /> <!-- Border color -->
                        </shape>
                    </item>
                    <!-- EditText Content -->
                    <item android:top="2dp" android:left="2dp" android:right="2dp" android:bottom="2dp">
                        <shape android:shape="rectangle">
                            <solid android:color="@android:color/white" /> <!-- Content background color -->
                        </shape>
                    </item>
                </layer-list>
                In this example, we create a drawable that has a white background with a black border of 2dp width around it. You can customize the android:color and android:width attributes to achieve the desired appearance.

                Set the Drawable as Background: In your XML layout file or programmatically, set the drawable you created as the background for your AppCompatEditText.

                XML Layout:

                xml
                Copy code
                <androidx.appcompat.widget.AppCompatEditText
                    android:id="@+id/myEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Enter text"
                    android:background="@drawable/edittext_border" <!-- Reference the drawable -->
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                />
                Programmatically (Java or Kotlin):

                java
                Copy code
                import androidx.appcompat.widget.AppCompatEditText;
                import android.graphics.drawable.Drawable;

                // Inside your activity or fragment...
                AppCompatEditText myEditText = findViewById(R.id.myEditText);
                Drawable borderDrawable = getResources().getDrawable(R.drawable.edittext_border);
                myEditText.setBackground(borderDrawable);
                In both cases, make sure to reference the correct drawable resource name (@drawable/edittext_border) if it differs in your project.

                This will add a border around your AppCompatEditText. You can further customize the appearance of the border by adjusting the values in the drawable XML resource to match your design requirements.




                User
                make it simpler
                ChatGPT
                Certainly! If you want to add a simple border around an AppCompatEditText with a solid color, you can do so by setting the backgroundTint attribute and using a shape drawable for the border.

                Here's a simplified example:

                Create a Drawable XML Resource:

                Create an XML file in your res/drawable directory (e.g., border.xml) and define the border appearance. For a simple border, you can use a shape drawable:

                xml
                Copy code
                <!-- res/drawable/border.xml -->
                <shape xmlns:android="http://schemas.android.com/apk/res/android">
                    <solid android:color="@android:color/white" /> <!-- Background color -->
                    <stroke
                        android:width="2dp" <!-- Border width -->
                        android:color="@android:color/black" /> <!-- Border color -->
                </shape>
                Set the Drawable as Background:

                In your XML layout file, set the drawable as the background for your AppCompatEditText:

                xml
                Copy code
                <androidx.appcompat.widget.AppCompatEditText
                    android:id="@+id/myEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Enter text"
                    android:background="@drawable/border" <!-- Reference the drawable -->
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                />
                This simplified example creates a white background with a black border of 2dp width around the AppCompatEditText. You can adjust the android:color and android:width attributes in the border.xml file to customize the appearance further.

                This method avoids complex layer-lists and reduces the code to a minimal setup for adding a border to your AppCompatEditText.




                User
                do it programatically
                ChatGPT
                To add a simple border around an AppCompatEditText programmatically in Java, you can create a ShapeDrawable and set it as the background of the AppCompatEditText. Here's how to do it:

                java
                Copy code
                import androidx.appcompat.widget.AppCompatEditText;
                import android.graphics.drawable.ShapeDrawable;
                import android.graphics.drawable.shapes.RectShape;

                // Inside your activity or fragment...
                AppCompatEditText myEditText = findViewById(R.id.myEditText);

                // Create a ShapeDrawable for the border
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());

                // Set the border color and width
                shapeDrawable.getPaint().setColor(getResources().getColor(android.R.color.black)); // Border color
                shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
                shapeDrawable.getPaint().setStrokeWidth(2); // Border width in pixels

                // Set the background drawable for the EditText
                myEditText.setBackground(shapeDrawable);
                In this code:

                We create a ShapeDrawable with a RectShape, which represents a simple rectangle shape.

                We set the border color using shapeDrawable.getPaint().setColor(). You can replace android.R.color.black with any color you desire.

                We set the border style to Paint.Style.STROKE and the border width to 2 pixels using shapeDrawable.getPaint().setStyle() and shapeDrawable.getPaint().setStrokeWidth().

                Finally, we set the ShapeDrawable as the background of the AppCompatEditText using myEditText.setBackground(shapeDrawable).

                This code programmatically adds a border around your AppCompatEditText without the need for XML resources. You can further customize the border color and width to match your requirements.




                User
                change is editable
                ChatGPT
                If you want to make an AppCompatEditText non-editable (i.e., read-only), you can use the setEnabled and setFocusable methods to achieve this programmatically. Here's how you can make it non-editable:

                java
                Copy code
                import androidx.appcompat.widget.AppCompatEditText;

                // Inside your activity or fragment...
                AppCompatEditText myEditText = findViewById(R.id.myEditText);

                // Make the EditText non-editable
                myEditText.setEnabled(false); // Disables editing
                myEditText.setFocusable(false); // Removes focus and interaction
                myEditText.setCursorVisible(false); // Hides the cursor
                With these lines of code, the AppCompatEditText will become non-editable, and the user won't be able to interact with it or modify its content. Additionally, the cursor will be hidden, making it visually clear that it's read-only.

                If you ever need to make it editable again programmatically, you can simply call setEnabled(true), setFocusable(true), and setCursorVisible(true) to re-enable editing and interaction.*/
            val le = letters[i]
            le.setEnabled(false); // Disables editing
            le.setFocusable(false); // Removes focus and interaction
            le.setCursorVisible(false); // Hides the cursor
            le.setTextColor(ColorStateList.valueOf(Color.BLACK))
            if (feedback[i] == 'c') {
                /* ChatGPT
                I have AppCompatEditText
                ChatGPT
                Great! The AppCompatEditText is a component in the Android AppCompat library that provides a backward-compatible version of the EditText widget, allowing you to use modern Android features while maintaining compatibility with older Android versions.

                You can use the AppCompatEditText in your Android app layout XML files or dynamically in code. Here's a basic example of how to use it in XML layout:

                xml
                Copy code
                <androidx.appcompat.widget.AppCompatEditText
                    android:id="@+id/myEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Enter text"
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    />
                In this example, we've added an AppCompatEditText widget to the layout. You can customize its attributes like id, layout_width, layout_height, and hint according to your needs.

                In your Java or Kotlin code, you can reference this AppCompatEditText by its ID and interact with it programmatically. For example, you can get the text entered by the user:

                java
                Copy code
                import androidx.appcompat.widget.AppCompatEditText;

                // Inside your activity or fragment...
                AppCompatEditText myEditText = findViewById(R.id.myEditText);

                String userInput = myEditText.getText().toString();
                Make sure to import the correct package for AppCompatEditText in your Java or Kotlin file.

                You can also set listeners and perform actions based on user input, validate input, and much more with AppCompatEditText. If you have any specific questions or need assistance with a particular use case, please provide more details, and I'll be happy to help!




                User
                I need to implement something when the user presses backspace
                ChatGPT
                To implement functionality when the user presses the Backspace key in an AppCompatEditText, you can set up a TextWatcher and listen for changes in the text content. Within the TextWatcher, you can check if the Backspace key has been pressed and then take action accordingly.

                Here's an example of how to do this in Kotlin:

                kotlin
                Copy code
                import android.os.Bundle
                import android.text.Editable
                import android.text.TextWatcher
                import androidx.appcompat.app.AppCompatActivity
                import androidx.appcompat.widget.AppCompatEditText

                class MainActivity : AppCompatActivity() {

                    private lateinit var editText: AppCompatEditText

                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContentView(R.layout.activity_main)

                        editText = findViewById(R.id.myEditText)

                        editText.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                // This method is called before the text changes.
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                // This method is called during the text change.
                            }

                            override fun afterTextChanged(s: Editable?) {
                                // This method is called after the text changes.
                                val text = s.toString()
                                if (text.isEmpty()) {
                                    // Backspace was pressed because the text is now empty.
                                    // Implement your desired functionality here.
                                }
                            }
                        })
                    }
                }
                In this example, we add a TextWatcher to the AppCompatEditText. Inside the afterTextChanged method, we check if the text inside the EditText is empty. If it is empty, it means the Backspace key was pressed, and you can implement your desired functionality inside the if statement.

                Remember to replace R.id.myEditText with the actual ID of your AppCompatEditText if it's different in your layout XML.




                User
                make every file on my remote match my local repo
                ChatGPT
                To make every file on your remote repository match your local repository, you can follow these general steps. Please note that these steps are for Git, assuming you are using Git as your version control system:

                Pull Latest Changes: Before pushing your changes to the remote repository, it's a good practice to first ensure that your local repository is up-to-date with the remote. Run the following commands in your local repository:

                bash
                Copy code
                git fetch origin
                git checkout <branch-name>
                git merge origin/<branch-name>
                Replace <branch-name> with the name of the branch you want to synchronize.

                Commit Local Changes: If you have made any changes in your local repository that you want to push to the remote, commit those changes using the following commands:

                bash
                Copy code
                git add .
                git commit -m "Your commit message"
                Push to Remote: Once you have committed your changes, you can push them to the remote repository:

                bash
                Copy code
                git push origin <branch-name>
                Replace <branch-name> with the name of your branch.

                Verify Remote Matches Local: After pushing, you can verify that your remote repository now matches your local repository by checking the remote's status:

                bash
                Copy code
                git status
                It should report that your branch is up-to-date with the remote.

                Keep in mind that these steps assume you are the only person working on this branch. If there are multiple contributors, you may encounter merge conflicts that need to be resolved before pushing. In such cases, communication with your team is crucial.

                Also, make sure that you have the necessary permissions to push changes to the remote repository.

                Remember to replace <branch-name> with the actual name of the branch you are working on.

                Please take extra caution when syncing your remote repository with your local one, especially if you are working on a shared project, to avoid unintentional data loss or conflicts.




                User
                all files and directories
                ChatGPT
                If you want to ensure that all files and directories in your remote repository match your local repository, you can follow these steps:

                Fetch Latest Changes from Remote: First, make sure your local repository is up-to-date with the latest changes from the remote repository. Run the following commands:

                bash
                Copy code
                git fetch origin
                This command will fetch the latest changes from the remote repository without merging them into your local branch.

                Reset Your Local Branch to Match the Remote: To make your local branch exactly match the remote branch (including all files and directories), you can use the git reset command. This command moves the branch pointer to the same commit as the remote branch. Run the following command:

                bash
                Copy code
                git reset --hard origin/<branch-name>
                Replace <branch-name> with the name of your branch.

                Warning: Be very cautious when using git reset --hard, as it will remove any uncommitted changes in your local branch. Make sure you have committed or stashed your changes before running this command.

                Push the Changes: After resetting your local branch to match the remote, you can push these changes to the remote repository to make them match. Run the following command:

                bash
                Copy code
                git push origin <branch-name> --force
                The --force option is necessary because you're rewriting the commit history of the branch, and you want to force-push these changes to the remote repository.

                After completing these steps, your remote repository should now exactly match your local repository in terms of files and directories. Be aware that this operation will reset the branch on the remote repository as well, so ensure you have the necessary permissions and communicate with your team if you're working on a shared project.




                User
                AppCompatEditText change color
                ChatGPT
                To change the text color of an AppCompatEditText widget in Android, you can use the android:textColor attribute in your layout XML or set the text color programmatically in your Java or Kotlin code. Here's how to do both:

                Using XML Layout (XML Attribute)
                In your XML layout file, you can set the text color using the android:textColor attribute like this:

                xml
                Copy code
                <androidx.appcompat.widget.AppCompatEditText
                    android:id="@+id/myEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Enter text"
                    android:textColor="@color/your_text_color" <!-- Change to your desired color resource -->
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    />
                In this example, @color/your_text_color should be replaced with the color resource you want to use. You can define color resources in your res/values/colors.xml file.

                Programmatically (Java or Kotlin)
                To change the text color programmatically in your Java or Kotlin code, you can use the setTextColor method on your AppCompatEditText instance:

                java
                Copy code
                import androidx.appcompat.widget.AppCompatEditText;
                import android.graphics.Color;

                // Inside your activity or fragment...
                AppCompatEditText myEditText = findViewById(R.id.myEditText);
                myEditText.setTextColor(Color.RED); // Change to your desired color
                In this example, we're setting the text color to red using the Color.RED constant. You can replace it with any color you desire, either by specifying a color code (e.g., Color.parseColor("#RRGGBB")) or by referencing a color resource.

                Remember that text color changes will affect the entire text inside the AppCompatEditText. If you want to change the color of specific portions of the text (e.g., different parts of the text in different colors), you can use SpannableString and SpannableStringBuilder along with ForegroundColorSpan to achieve that level of customization.




                User
                change the color of the box
                ChatGPT
                To change the background color of the AppCompatEditText (the "box" you mentioned), you can use the android:background attribute in your XML layout or set the background color programmatically in your Java or Kotlin code.

                Using XML Layout (XML Attribute):
                In your XML layout file, you can set the background color using the android:background attribute like this:

                xml
                Copy code
                <androidx.appcompat.widget.AppCompatEditText
                    android:id="@+id/myEditText"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Enter text"
                    android:background="@color/your_background_color" <!-- Change to your desired color resource -->
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    />
                In this example, @color/your_background_color should be replaced with the color resource you want to use for the background.

                Programmatically (Java or Kotlin):
                To change the background color programmatically in your Java or Kotlin code, you can use the setBackgroundTintList method on your AppCompatEditText instance:

                java
                Copy code
                import androidx.appcompat.widget.AppCompatEditText;
                import android.content.res.ColorStateList;
                import android.graphics.Color;

                // Inside your activity or fragment...
                AppCompatEditText myEditText = findViewById(R.id.myEditText);
                myEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED)); // Change to your desired color
                In this example, we're setting the background color to red using ColorStateList.valueOf(Color.RED). You can replace it with any color you desire, either by specifying a color code or by referencing a color resource.

                Keep in mind that changing the background color will alter the appearance of the entire AppCompatEditText box. If you want to have more advanced customization, such as rounded corners or gradients, you may need to create a custom background drawable XML resource and set it as the background for the AppCompatEditText.










    */
                le.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN))
            }
            else if (feedback[i] == 'i') {
                le.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW))
            }
            else {
                //has to be incorrect
                le.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY))
            }


        }
    }

    /**
     * Gets the word that's currently in the this row
     */
    fun getWord(): String {
        var sb = StringBuilder()
        for (le in letters) {
            sb.append(le.getText())
        }
        return sb.toString()
    }
}
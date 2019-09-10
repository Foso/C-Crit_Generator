package de.jensklingenberg.ccritdemoproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.jensklingenberg.ccrit.NativeSecret
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    @NativeSecret("topSecret")
    external fun stringFromJNI(): String

    @NativeSecret("topSecret2")
    external fun secondSecretString(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}

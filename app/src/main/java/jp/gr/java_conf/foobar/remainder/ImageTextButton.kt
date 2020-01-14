package jp.gr.java_conf.foobar.remainder

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by keita on 2016/07/11.
 */
class ImageTextButton : LinearLayout {

    private var listener: OnClickListener? = null

    private lateinit var button: ImageButton
    private lateinit var text: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs,
                R.styleable.ImageTextButton)

        val layout = LayoutInflater.from(context).inflate(R.layout.image_text_button, this)

        button = layout.findViewById(R.id.button)
        button.setImageDrawable(a.getDrawable(R.styleable.ImageTextButton_image))

        text = layout.findViewById(R.id.text)

        if (a.getText(0) != null) {
            if (a.getText(0) != "") {
                text.visibility = View.VISIBLE
                text.text = a.getText(0)
            }
        }

        a.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setText(s: String) {
        text.text = s
        text.visibility = View.VISIBLE
    }

    fun setImage(id: Int) {
        button.setImageResource(id)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (listener != null) {
                button.isPressed = true
                listener!!.onClick(this)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP && (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER || event.keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (listener != null) {
                button.isPressed = true
                listener!!.onClick(this)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }


}

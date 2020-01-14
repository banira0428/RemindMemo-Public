package jp.gr.java_conf.foobar.remainder.legacy

import java.util.Calendar

/**
 * Created by keita on 2016/08/24.
 */
class Memo(internal var memo: String) {
    internal var id: Int = 0
    internal var remind: Boolean = false

    init {
        id = Calendar.getInstance().get(Calendar.MINUTE) * 100 + Calendar.getInstance().get(Calendar.SECOND)
        remind = true
    }

}

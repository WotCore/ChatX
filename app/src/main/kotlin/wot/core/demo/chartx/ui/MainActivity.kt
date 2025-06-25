package wot.core.demo.chartx.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import wot.core.demo.chartx.R
import wot.core.demo.chartx.databinding.MainActivityBinding

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/27
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity).initView()
    }

    private fun MainActivityBinding.initView() {
        sampleChatView.apply {
            val third = DataUtils.chatData1()
            setNewData(
                Triple(0, 0, third),
                Triple(0, 1, DataUtils.chatData1()),
            )
        }
    }

}


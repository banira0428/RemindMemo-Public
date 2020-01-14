package jp.gr.java_conf.foobar.remainder

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import jp.gr.java_conf.foobar.remainder.databinding.ActivityMainBinding
import jp.gr.java_conf.foobar.remainder.legacy.Memo
import jp.gr.java_conf.foobar.remainder.preference.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), RecyclerAdapter.Listener, NavigationView.OnNavigationItemSelectedListener {


    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initAd()

        migrateFromLegacy()

        viewModel.textMemo.observe(this, Observer {
            binding.buttonAdd.isEnabled = it?.isNotEmpty() ?: false
        })

        viewModel.remindMemoList.observe(this, Observer {
            mAdapter.remindMemoList = it
        })

        viewModel.historyList.observe(this, Observer {
            binding.editMemo.setAdapter(ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, it.map { it.content }))
        })

        mAdapter = RecyclerAdapter(this, this)
        binding.recyclerView.setHasFixedSize(true) // アイテムは固定サイズ
        binding.recyclerView.adapter = mAdapter

        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, android.R.string.ok, android.R.string.cancel)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {

                AlertDialog.Builder(this@MainActivity, R.style.MyAlertDialogStyle)
                        .setTitle(getString(R.string.action_delete_history))
                        .setMessage(getString(R.string.msg_delete_history))
                        .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                            viewModel.clearHistory()
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .create().show()

                return true
            }
            R.id.action_export -> {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, viewModel.remindMemoList.value?.map { it.title }?.joinToString("\n"))
                }

                startActivity(intent)

                return true
            }
            R.id.action_import -> {

                val view = layoutInflater.inflate(R.layout.dialog_edit_memo, null)

                val editText = view.findViewById<EditText>(R.id.edit_memo).apply {
                    requestFocus()
                }

                view.findViewById<TextInputLayout>(R.id.textInputLayout_memo).hint = getString(R.string.msg_import_memo)

                AlertDialog.Builder(this@MainActivity, R.style.MyAlertDialogStyle)
                        .setTitle(getString(R.string.action_import))
                        .setView(view)
                        .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                            viewModel.onClickImport(editText.text.toString())
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .create().show()

                return true
            }
            R.id.action_about -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onClickContent(remindMemo: RemindMemo) {

        val view = layoutInflater.inflate(R.layout.dialog_edit_memo, null)

        val editText = view.findViewById<EditText>(R.id.edit_memo).apply {
            setText(remindMemo.title)
            maxLines = 1
            requestFocus()
        }

        AlertDialog.Builder(this@MainActivity, R.style.MyAlertDialogStyle)
                .setTitle(getString(R.string.action_update_memo))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                    viewModel.onClickUpdate(RemindMemo.replaceTitle(remindMemo, editText.text.toString()))
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create().show()

    }

    override fun onClickToggleRemind(remindMemo: RemindMemo) {
        viewModel.onClickToggleRemind(remindMemo)
    }

    override fun onClickDelete(id: Int) {
        viewModel.onClickDelete(id)
    }

    private fun initAd() {

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("DA539D38B08126EBEF7E059DCA26831C")
                .addTestDevice("4C3BA6538C8F304A33859DC20F66316E")
                .addTestDevice("BDB57B5078A79B87345E711A52F0F995").build()
        binding.adView.loadAd(adRequest)

    }

    private fun migrateFromLegacy() { //todo 数ヶ月後に削除
        val sp = applicationContext.getSharedPreferences("memo", MODE_PRIVATE)
        val gson = Gson()
        var i = 0

        val list = arrayListOf<Memo>()

        while (true) {
            if (gson.fromJson(sp.getString("memo$i", ""),
                            Memo::class.java) != null) {

                list.add(gson.fromJson(
                        sp.getString("memo$i", ""),
                        Memo::class.java))
                i++
            } else {
                break
            }
        }

        viewModel.convertToRoom(list)

        sp.edit().apply {
            clear()
            apply()
        }
    }
}

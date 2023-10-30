package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Participant

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val participantList: MutableList<Participant> = mutableListOf()

    private val participantAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            participantList.map { _participant ->
                _participant.name
            }
        )
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.title = resources.getString(R.string.main_activity_toolbar_title)

        amb.participantLv.adapter = participantAdapter

        amb.addParticipantFab.setOnClickListener() {
            launchParticipantActivity()
        }

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val participant = result.data?.getParcelableExtra<Participant>(EXTRA_PARTICIPANT)
                participant?.let { _participant ->
                    participantList.add(_participant)
                    participantAdapter.add(_participant.name)
                    participantAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addParticipantMi -> {
                launchParticipantActivity()
                true
            }
            else -> false
        }
    }

    private fun launchParticipantActivity() {
        carl.launch(Intent(this, ParticipantActivity::class.java))
    }

}
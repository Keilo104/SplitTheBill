package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.adapter.ParticipantAdapter
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Participant
import br.edu.scl.ifsp.ads.splitthebill.model.ParticipantListManager

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val participantListManager: ParticipantListManager by lazy {
        ParticipantListManager()
    }

    private val participantAdapter: ParticipantAdapter by lazy {
        ParticipantAdapter(
            this,
            participantListManager
        )
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.title = resources.getString(R.string.main_activity_toolbar_title)
        updateTotalAmountSubtitle()

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
                    participantListManager.addOrUpdateParticipant(_participant)
                    updateTotalAmountSubtitle()
                    participantAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.participantLv)
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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.removeParticipantMi -> {
                true
            }
            R.id.editParticipantMi -> {
                true
            }
            else -> true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.participantLv)
    }

    private fun updateTotalAmountSubtitle() {
        supportActionBar?.subtitle =
            "${resources.getString(R.string.main_activity_toolbar_subtitle)} " +
                    "${participantListManager.getTotalPurchaseAmount().format(2)} "
    }

    private fun launchParticipantActivity() {
        carl.launch(Intent(this, ParticipantActivity::class.java))
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

}
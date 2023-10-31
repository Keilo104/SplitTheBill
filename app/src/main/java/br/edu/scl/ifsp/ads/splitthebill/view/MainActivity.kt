package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.adapter.ParticipantAdapter
import br.edu.scl.ifsp.ads.splitthebill.controller.ParticipantController
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.VIEW_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Participant

class MainActivity : AppCompatActivity() {
    // View Binding
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Adapter
    private val participantAdapter: ParticipantAdapter by lazy {
        ParticipantAdapter(
            this,
            participantController
        )
    }

    // Controller
    private val participantController: ParticipantController by lazy {
        val participantController = ParticipantController(this)
        participantController.syncParticipantList()
    }

    // Activity Result Launcher
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
                    participantController.addOrEditParticipant(_participant)
                    updateTotalAmountSubtitle()
                    participantAdapter.notifyDataSetChanged()
                }
            }
        }

        amb.participantLv.setOnItemClickListener { parent, view, position, id ->
            val participant = participantController.getParticipantAt(position)
            val viewParticipantIntent = Intent(this, ParticipantActivity::class.java)
                .putExtra(EXTRA_PARTICIPANT, participant)
                .putExtra(VIEW_PARTICIPANT, true)

            startActivity(viewParticipantIntent)
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
                participantController.removeParticipant(position)
                participantAdapter.notifyDataSetChanged()
                updateTotalAmountSubtitle()
                Toast.makeText(this,
                    resources.getString(R.string.toast_remove_participant),
                    Toast.LENGTH_SHORT
                ).show()
                true
            }

            R.id.editParticipantMi -> {
                val participant = participantController.getParticipantAt(position)
                val editParticipantIntent = Intent(this, ParticipantActivity::class.java)
                editParticipantIntent.putExtra(EXTRA_PARTICIPANT, participant)
                carl.launch(editParticipantIntent)
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
                    "${participantController.getTotalPurchaseAmount().format(2)} "
    }

    private fun launchParticipantActivity() {
        carl.launch(Intent(this, ParticipantActivity::class.java))
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

}
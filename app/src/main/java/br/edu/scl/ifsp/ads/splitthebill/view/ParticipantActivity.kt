package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.util.MoneyTextWatcher
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityParticipantBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.INVALID_PARTICIPANT_ID
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.VIEW_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Participant

class ParticipantActivity : AppCompatActivity() {
    private val apb: ActivityParticipantBinding by lazy {
        ActivityParticipantBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        setSupportActionBar(apb.toolbarIn.toolbar)
        supportActionBar?.title = resources.getString(R.string.participant_activity_title_add)
        supportActionBar?.subtitle = resources.getString(R.string.participant_activity_subtitle)

        apb.amountSpent1Et.addTextChangedListener(MoneyTextWatcher(apb.amountSpent1Et))
        apb.amountSpent2Et.addTextChangedListener(MoneyTextWatcher(apb.amountSpent2Et))
        apb.amountSpent3Et.addTextChangedListener(MoneyTextWatcher(apb.amountSpent3Et))

        val receivedParticipant = intent.getParcelableExtra<Participant>(EXTRA_PARTICIPANT)
        receivedParticipant?.let { _receivedParticipant ->
            supportActionBar?.title = resources.getString(R.string.participant_activity_title_edit)
            val viewParticipant: Boolean = intent.getBooleanExtra(VIEW_PARTICIPANT, false)

            with(apb) {
                if (viewParticipant) {
                    supportActionBar?.title = resources.getString(R.string.participant_activity_title_view)
                    supportActionBar?.subtitle = ""
                    nameEt.isEnabled = false
                    itemBought1Et.isEnabled = false
                    amountSpent1Et.isEnabled = false
                    itemBought2Et.isEnabled = false
                    amountSpent2Et.isEnabled = false
                    itemBought3Et.isEnabled = false
                    amountSpent3Et.isEnabled = false
                    saveBt.visibility = View.GONE

                    val hexPrimary = resources.getColor(R.color.primary_theme_color)
                    cancelBt.setBackgroundColor(hexPrimary)
                    cancelBt.text = resources.getString(R.string.participant_activity_return)
                }

                nameEt.setText(_receivedParticipant.name)
                itemBought1Et.setText(_receivedParticipant.itemBought1)
                amountSpent1Et.setText(_receivedParticipant.amountSpent1.format(2))
                itemBought2Et.setText(_receivedParticipant.itemBought2)
                amountSpent2Et.setText(_receivedParticipant.amountSpent2.format(2))
                itemBought3Et.setText(_receivedParticipant.itemBought3)
                amountSpent3Et.setText(_receivedParticipant.amountSpent3.format(2))
            }
        }

        with(apb) {
            saveBt.setOnClickListener {
                var amountSpent1 = 0.0
                var amountSpent2 = 0.0
                var amountSpent3 = 0.0

                if (itemBought1Et.text.toString().isNotEmpty() &&
                    amountSpent1Et.text.toString().isNotEmpty()) {
                    amountSpent1 = amountSpent1Et.text.toString().toDouble()
                }

                if (itemBought2Et.text.toString().isNotEmpty() &&
                    amountSpent2Et.text.toString().isNotEmpty()) {
                    amountSpent2 = amountSpent2Et.text.toString().toDouble()
                }

                if (itemBought3Et.text.toString().isNotEmpty() &&
                    amountSpent3Et.text.toString().isNotEmpty()) {
                    amountSpent3 = amountSpent3Et.text.toString().toDouble()
                }

                val participant = Participant(
                    id = receivedParticipant?.id?: INVALID_PARTICIPANT_ID,
                    name = nameEt.text.toString(),
                    itemBought1 = itemBought1Et.text.toString(),
                    amountSpent1 = amountSpent1,
                    itemBought2 = itemBought2Et.text.toString(),
                    amountSpent2 = amountSpent2,
                    itemBought3 = itemBought3Et.text.toString(),
                    amountSpent3 = amountSpent3,
                    totalAmountSpent = amountSpent1 + amountSpent2 + amountSpent3,
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_PARTICIPANT, participant)
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            cancelBt.setOnClickListener {
                val resultIntent = Intent()
                setResult(RESULT_CANCELED, resultIntent)
                finish()
            }
        }
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}
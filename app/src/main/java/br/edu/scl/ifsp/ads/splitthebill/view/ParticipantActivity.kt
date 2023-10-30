package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityParticipantBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PARTICIPANT
import br.edu.scl.ifsp.ads.splitthebill.model.Participant
import java.util.Random

class ParticipantActivity : AppCompatActivity() {
    private val apb: ActivityParticipantBinding by lazy {
        ActivityParticipantBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        setSupportActionBar(apb.toolbarIn.toolbar)
        supportActionBar?.title = "alo"

        with(apb) {
            saveBt.setOnClickListener {
                val participant = Participant(
                    id = generateId(),
                    name = nameEt.text.toString(),
                    amountSpent = amountSpentEt.text.toString().toFloat(),
                    itemsBought = itemsBoughtEt.text.toString()
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_PARTICIPANT, participant)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

    }

    private fun generateId(): Int = Random(System.currentTimeMillis()).nextInt()
}
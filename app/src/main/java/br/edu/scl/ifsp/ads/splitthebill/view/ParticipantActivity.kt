package br.edu.scl.ifsp.ads.splitthebill.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityParticipantBinding

class ParticipantActivity : AppCompatActivity() {
    private val apb: ActivityParticipantBinding by lazy {
        ActivityParticipantBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        setSupportActionBar(apb.toolbarIn.toolbar)
        supportActionBar?.title = "alo"

    }
}
package com.michael.viatoapp.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.michael.viatoapp.R
import com.michael.viatoapp.fragments.CityOverviewFragment


class CityOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_overview)

        // Check if the fragment container is available and the fragment is not already added
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, CityOverviewFragment())
                .commit()
        }

        val buttonNavigate: Button = findViewById(R.id.button_go_to)
        buttonNavigate.setOnClickListener {
            val intent = Intent(this, MoreInfoActivity::class.java)

            startActivity(intent)
        }
    }

    private fun onCityOverviewClicked(): Boolean{
        supportFragmentManager.commit {
            replace(R.id.fragment_content, CityOverviewFragment())
        }
        return true
    }
}
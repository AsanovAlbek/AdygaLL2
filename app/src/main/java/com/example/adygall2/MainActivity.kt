package com.example.adygall2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.adygall2.fragments.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CONTAINER = R.id.nav_host_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val phraseFragment = PhraseFragment(
            object : OnContinueBtnListener {
                override fun onClick() {
                    finish()
                }
            }
        )

        val sentenceFragment = SentenceBuildQuestion(
            object : OnContinueBtnListener {
                override fun onClick() {
                    goToFragment(phraseFragment)
                }
            }
        )

        val threeWordsQuestion = ThreeWordsQuestion(
            object : OnContinueBtnListener {
                override fun onClick() {
                    goToFragment(sentenceFragment)
                }
            }
        )

        val fourImageQuestion = FourImageQuestion(
            object : OnContinueBtnListener {
                override fun onClick() {
                    goToFragment(threeWordsQuestion)
                }
            }
        )

        val homePage = HomePage(
            object : OnContinueBtnListener {
                override fun onClick() {
                    goToFragment(fourImageQuestion)
                }
            }
        )

        val auth = Authorize(
            object : OnContinueBtnListener {
                override fun onClick() {
                    goToFragment(homePage)
                }
            }
        )

        addFragment(auth)
    }

    private fun goToFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(CONTAINER, fragment).commit()
    }

    private fun addFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().add(CONTAINER, fragment).commit()
    }
}
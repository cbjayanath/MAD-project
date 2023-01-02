package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton

class WalkThrough_Slider : AppCompatActivity() {
    private lateinit var onboardingItemAdapter: OnboardingItemAdapter
    private lateinit var indicatorsContainer : LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_through_slider)
        setOnboardignItem()
        setupIndicators()
        setCurrentIndicator(0)

        findViewById<MaterialButton>(R.id.buttonGetStarted).setOnClickListener{
            navigateToHome()
        }

    }

    @SuppressLint("WrongViewCast")
    private fun setOnboardignItem(){
        onboardingItemAdapter= OnboardingItemAdapter(
            listOf(
                OnboardingItem(
                   onboardingImage = R.drawable.img_03,

                    title = "Welcome to NexTrip!",
                    description = "Save time, Leisure More.."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.img_04,
                    title = "Travel While Planning",
                    description = "Just forget pre plannings\n" + "NexTrip offers you to Travel While Planning!"

                ),
                OnboardingItem(
                    onboardingImage= R.drawable.img_05,
                    title = "Enjoy Leisure forget Todos",
                    description = "Don't worry about Things\n" + "We are here to remind you Everything's"
                )
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onBoardingViewPage) //if error comes see this
        onboardingViewPager.adapter = onboardingItemAdapter

        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
               }
            })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode=
            RecyclerView.OVER_SCROLL_NEVER
        findViewById<ImageView>(R.id.imageNext).setOnClickListener{
            if (onboardingViewPager.currentItem + 1 <onboardingItemAdapter.itemCount){
                onboardingViewPager.currentItem += 1
            }else {
                navigateToHome()
            }
            findViewById<TextView>(R.id.textSkip).setOnClickListener{
                navigateToHome()
            }
//            findViewById<MaterialButton>(R.id.buttonGetStarted).setOnClickListener{
//                navigateToHome()
//            }
        }
    }
    private fun navigateToHome(){
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }
    private fun setupIndicators(){
        indicatorsContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingItemAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams=layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }
    private fun setCurrentIndicator(position: Int){
        val childCount = indicatorsContainer.childCount
        for(i in 0 until childCount){
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i==position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            }else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }

}
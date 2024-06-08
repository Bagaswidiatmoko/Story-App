package com.dicoding.picodiploma.loginwithanimation.view.detailstory

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyDetail = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_DETAIL, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY_DETAIL)
        }

        if (storyDetail != null) {
            with(binding) {
                Glide.with(this@StoryDetailActivity).load(storyDetail.photoUrl).into(ivDetailPhoto)
                tvDetailName.text = storyDetail.name
                tvDetailDescription.text = storyDetail.description
            }
        }
    }


    companion object {
        const val KEY_DETAIL = "key_detail"
    }
}
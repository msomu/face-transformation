package com.msomu.example.face

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.msomu.glide.facetransformation.FaceCrop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadImages("https://images.pexels.com/photos/39657/model-fashion-attractive-female-39657.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
    }

    private fun loadImages(url : String){
        Glide.with(this)
            .load(url)
            .into(imageViewOriginal)

        Glide.with(this)
            .load(url)
            .transform(CenterCrop())
            .into(imageViewCenterCrop)

        Glide.with(this)
            .load(url)
            .transform(FaceCrop())
            .into(imageViewFaceCrop)
    }
}
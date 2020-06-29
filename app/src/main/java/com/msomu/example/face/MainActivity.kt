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
        loadImages(getString(R.string.sample_image_url))
        button.setOnClickListener {
            loadImages(editTextTextUrl.text.toString())
            editTextTextUrl.setText("")
        }
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
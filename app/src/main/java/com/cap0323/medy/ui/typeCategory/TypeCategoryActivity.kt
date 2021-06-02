package com.cap0323.medy.ui.typeCategory

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cap0323.medy.R
import com.cap0323.medy.databinding.ActivityTypeCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


class TypeCategoryActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityTypeCategoryBinding
    private val typeCategoryViewModel: TypeCategoryViewModel by viewModels()
    private lateinit var adapter: TypeCategoryAdapter
    private lateinit var adapterBottomSheetCategory: BottomSheetCategoryAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypeCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()

        setUpRecylerViewMain()
        setUpRecylerBottomSheet()
        displayingAllData()

        val extras = intent.extras
        if (extras != null) {
            val charCategory = extras.getString(EXTRA_ID)
            if (charCategory != null) {
                typeCategoryViewModel.getCategoryByChar(charCategory)
                typeCategoryViewModel.indicationByChar.observe(this, {
                    adapterBottomSheetCategory.setBottomSheetAdapter(it)
                    Log.d("Testing api indication", it.toString())
                })
                bottomSheetSetUp()
            }
        }

    }

    private fun setUpRecylerViewMain() {
        binding.apply {
            val orientation = resources.configuration.orientation
            if (orientation == SCREEN_ORIENTATION_PORTRAIT) {
                binding.rvCategory.layoutManager = GridLayoutManager(this@TypeCategoryActivity, 2)
            } else {
                binding.rvCategory.layoutManager = GridLayoutManager(this@TypeCategoryActivity, 4)
            }
            binding.rvCategory.setHasFixedSize(true)
            adapter = TypeCategoryAdapter(this@TypeCategoryActivity)
            rvCategory.adapter = adapter
        }
    }

    private fun setUpRecylerBottomSheet() {
        binding.apply {
            btmSheet.rvBtmSheet.layoutManager = LinearLayoutManager(this@TypeCategoryActivity)
            adapterBottomSheetCategory = BottomSheetCategoryAdapter(this@TypeCategoryActivity)
            btmSheet.rvBtmSheet.adapter = adapterBottomSheetCategory
        }
    }

    private fun bottomSheetSetUp() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.btmSheet.bottomSheet)
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.btmSheet.cancelBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun displayingAllData() {
        adapter.setCategory(typeCategoryViewModel.getAllCategory())
    }

    private fun statusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.color_btn)
        }
    }
}
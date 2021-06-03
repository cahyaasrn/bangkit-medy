package com.cap0323.medy.ui.typeIndication

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cap0323.medy.R
import com.cap0323.medy.databinding.ActivityTypeIndicationBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class TypeIndicationActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
        const val alphabet = "alphabet"
    }

    private lateinit var binding: ActivityTypeIndicationBinding
    private val typeIndicationViewModel: TypeIndicationViewModel by viewModels()
    private lateinit var adapter: TypeIndicationAdapter
    private lateinit var adapterBottomSheetIndication: BottomSheetIndicationAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypeIndicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        setUpRecylerViewMain()
        setUpRecylerBottomSheet()
        displayingAllData()


        supportActionBar?.apply {
            title = "Select by Alphabet"
            setDisplayHomeAsUpEnabled(true)
        }


        val extras = intent.extras
        if (extras != null) {
            val charCategory = extras.getString(EXTRA_ID)
            val alphabet = extras.getString(alphabet)

            binding.btmSheet.title.text = alphabet.toString()

            if (charCategory != null) {
                typeIndicationViewModel.getCategoryByChar(charCategory)
                typeIndicationViewModel.indicationByChar.observe(this, {
                    adapterBottomSheetIndication.setBottomSheetAdapter(it)
                })
                bottomSheetSetUp()
            }
        }

        typeIndicationViewModel.noData.observe(this, {
            if (it) {
                dataNotFound("visible")
            } else {
                dataNotFound("gone")
            }
        })

        typeIndicationViewModel.isLoading.observe(this, {
            if (it) {
                binding.apply {
                    btmSheet.rvBtmSheet.visibility = View.GONE
                    btmSheet.shimmer.visibility = View.VISIBLE
                    btmSheet.imgBtmSheet.visibility = View.VISIBLE
                    btmSheet.shimmer.startShimmer()
                }
            } else {
                binding.apply {
                    btmSheet.rvBtmSheet.visibility = View.VISIBLE
                    btmSheet.shimmer.stopShimmer()
                    btmSheet.imgBtmSheet.visibility = View.GONE
                    btmSheet.shimmer.visibility = View.GONE
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpRecylerViewMain() {
        binding.apply {
            val orientation = resources.configuration.orientation
            if (orientation == SCREEN_ORIENTATION_PORTRAIT) {
                rvTypeIndication.layoutManager = GridLayoutManager(this@TypeIndicationActivity, 2)
            } else {
                rvTypeIndication.layoutManager = GridLayoutManager(this@TypeIndicationActivity, 4)
            }
            adapter = TypeIndicationAdapter(this@TypeIndicationActivity)
            rvTypeIndication.adapter = adapter
        }
    }

    private fun setUpRecylerBottomSheet() {
        binding.apply {
            btmSheet.rvBtmSheet.layoutManager = LinearLayoutManager(this@TypeIndicationActivity)
            adapterBottomSheetIndication = BottomSheetIndicationAdapter(this@TypeIndicationActivity)
            btmSheet.rvBtmSheet.adapter = adapterBottomSheetIndication
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
        adapter.setCategory(typeIndicationViewModel.getAllIndication())
    }

    private fun statusBarColor() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.color_btn)
    }

    private fun dataNotFound(status: String) {
        when (status) {
            "visible" -> binding.btmSheet.noData.noDataDialog.visibility = View.VISIBLE
            "gone" -> binding.btmSheet.noData.noDataDialog.visibility = View.GONE
        }
    }
}
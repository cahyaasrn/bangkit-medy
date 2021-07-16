package com.cap0323.medy.ui.typecategory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cap0323.medy.R
import com.cap0323.medy.data.local.entity.TypeIndicationEntity
import com.cap0323.medy.databinding.ItemMedicineTypeIndicationBinding

class TypeCategoryAdapter(private val context: Context) :
    RecyclerView.Adapter<TypeCategoryAdapter.IndicationViewHolder>() {

    private val listIndication = ArrayList<TypeIndicationEntity>()

    fun setCategory(indication: List<TypeIndicationEntity>) {
        this.listIndication.clear()
        this.listIndication.addAll(indication)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndicationViewHolder {
        val itemsMedicineBinding =
            ItemMedicineTypeIndicationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return IndicationViewHolder(itemsMedicineBinding)
    }

    override fun onBindViewHolder(holder: IndicationViewHolder, position: Int) {
        holder.bind(listIndication[position])
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.open_center
            )
        )

    }

    override fun getItemCount(): Int = listIndication.size

    class IndicationViewHolder(val binding: ItemMedicineTypeIndicationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(indication: TypeIndicationEntity) {
            binding.apply {
                tvIndication.text = indication.charIndication
                Glide.with(root)
                    .load(indication.urlImage).into(imgIndication)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, TypeCategoryActivity::class.java)
                    intent.putExtra(TypeCategoryActivity.EXTRA_ID, indication.typeIndication)
                    intent.putExtra(TypeCategoryActivity.alphabet, indication.charIndication)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
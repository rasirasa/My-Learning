package com.ssrdi.co.id.myradboox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.fragmentreseller.HomeFragment
import com.ssrdi.co.id.myradboox.model.VoucherItemResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import kotlinx.android.synthetic.main.item_hero.view.*
import retrofit2.Response

class VoucherAdapter(
    private val voucher: List<VoucherItemResponse>,
    private val adapterOnClick: (VoucherItemResponse) -> Unit
) : RecyclerView.Adapter<VoucherAdapter.VoucherHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addData(dataViews :List<VoucherItemResponse>){
        dataViews.map {
            voucher.add(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): VoucherHolder {
        return VoucherHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hero, viewGroup, false)
        )
    }

    override fun getItemCount(): Int = voucher.size

    override fun onBindViewHolder(holder: VoucherHolder, position: Int) {
        holder.bindVoucher(voucher[position])
    }

    inner class VoucherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindVoucher(voucher: VoucherItemResponse) {
            itemView.apply {
                txtUsername.text = voucher.username
                txtProfile.text = voucher.profile
                txtStatus.text = voucher.status
//                Glide.with(this)
//                    .load(hero.image)
//                    .placeholder(R.drawable.ic_launcher_background)
//                        .centerCrop()
//                    .into(imgHeroes)
////                Picasso.get().load(hero.image).into(imgHeroes)

                setOnClickListener {
                    adapterOnClick(voucher)
                }
            }
        }
    }
}


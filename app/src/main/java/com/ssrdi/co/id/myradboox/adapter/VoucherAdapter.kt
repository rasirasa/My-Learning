package com.ssrdi.co.id.myradboox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.fragmentreseller.HomeFragment
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import kotlinx.android.synthetic.main.item_hero.view.*
import retrofit2.Response

class VoucherAdapter(private val voucher: List<VoucherResponse.DataObject>,
                     private val adapterOnClick: (VoucherResponse.DataObject) -> Unit) : RecyclerView.Adapter<VoucherAdapter.VoucherHolder>() {
    private lateinit var mAdapter: VoucherAdapter
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): VoucherHolder {
        return VoucherHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hero, viewGroup, false))
    }

    override fun getItemCount(): Int = voucher.size

    override fun onBindViewHolder(holder: VoucherHolder, position: Int) {
        holder.bindVoucher(voucher[position])
    }

    inner class VoucherHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindVoucher(voucher: VoucherResponse.DataObject) {
            itemView.apply {
                txtHeroName.text = voucher.profile
                txtUsername.text = voucher.username
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


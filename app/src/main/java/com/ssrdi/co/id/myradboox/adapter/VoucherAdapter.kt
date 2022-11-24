package com.ssrdi.co.id.myradboox.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.databinding.ProgressLoadingBinding
import com.ssrdi.co.id.myradboox.fragmentreseller.HomeFragment
import com.ssrdi.co.id.myradboox.model.VoucherItemResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.readmore.Constant
import kotlinx.android.synthetic.main.item_hero.view.*
import retrofit2.Response

class VoucherAdapter(
    private val voucher: MutableList<VoucherItemResponse?>,
    private val adapterOnClick: (VoucherItemResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addData(dataViews: List<VoucherItemResponse>) {
        voucher.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): VoucherItemResponse? {
        return voucher[position]
    }

    fun addLoadingView() {
        Handler().post {
            voucher.add(null)
            notifyItemInserted(voucher.size - 1)
        }
    }

    fun removeLoadingView() {
        if (voucher.size != 0) {
            voucher.removeAt(voucher.size - 1)
            notifyItemRemoved(voucher.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hero, parent, false)
            VoucherHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_loading, parent, false)
            val binding = ProgressLoadingBinding.bind(view)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                binding.progressbar.indeterminateDrawable.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
            }else {
                binding.progressbar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
            }
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }


    override fun getItemCount(): Int = voucher.size


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


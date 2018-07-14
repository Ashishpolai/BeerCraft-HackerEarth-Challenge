package com.example.asis.thoughtworksproj.customadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asis.thoughtworksproj.MyUtils;
import com.example.asis.thoughtworksproj.R;
import com.example.asis.thoughtworksproj.model.BeerDatas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by praka on 12/24/2017.
 */

public class CustomBeerAdapter extends RecyclerView.Adapter<CustomBeerAdapter.CustomViewHolder> {

    private List<BeerDatas> dataList;
    private Context context;
    private Set<String> cartBeerIdSets = new HashSet<>();
    private  OnUpdateOfCart myRefreshCall;

    public interface OnUpdateOfCart {
        public void refreshCartCount();
    }

    public CustomBeerAdapter(Context context, List<BeerDatas> dataList){
        this.context = context;
        this.dataList = dataList;
        cartBeerIdSets= MyUtils.getCartValuesSets(context);
        myRefreshCall = (OnUpdateOfCart) context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtBeerName, txtBeerStyle, txtBeerAlcoholCont;
        ImageView btnAddToCart;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtBeerName = mView.findViewById(R.id.title);
            txtBeerStyle = mView.findViewById(R.id.beer_style);
            txtBeerAlcoholCont = mView.findViewById(R.id.alcohol_cont);
            btnAddToCart = mView.findViewById(R.id.btn_add_to_cart);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_beer_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.txtBeerName.setText(dataList.get(position).getmName());
        holder.txtBeerStyle.setText(dataList.get(position).getmBeerStyle());
        holder.txtBeerAlcoholCont.setText("Alcohol Content : "+dataList.get(position).getmAlcoholCont());

        if(cartBeerIdSets.size()!=0){
            if(cartBeerIdSets.contains(dataList.get(position).getmId())){//Already in cart
                holder.btnAddToCart.setImageResource(R.mipmap.remove_from_cart);
            }
            else{//Not in cart
                holder.btnAddToCart.setImageResource(R.mipmap.add_to_cart);
            }
        }

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartBeerIdSets.size()!=0){
                    if(cartBeerIdSets.contains(dataList.get(position).getmId())){//Already in cart
                        cartBeerIdSets.remove(dataList.get(position).getmId());
                        holder.btnAddToCart.setImageResource(R.mipmap.add_to_cart);
                    }
                    else{//Not in cart
                        cartBeerIdSets.add(dataList.get(position).getmId());
                        holder.btnAddToCart.setImageResource(R.mipmap.remove_from_cart);
                    }
                }
                else{//Nothing in cart
                    cartBeerIdSets.add(dataList.get(position).getmId());
                    holder.btnAddToCart.setImageResource(R.mipmap.remove_from_cart);
                }
                MyUtils.saveCartValuesSets(context, cartBeerIdSets);
                myRefreshCall.refreshCartCount();
            }
        });

        Log.d("asisi", ""+cartBeerIdSets);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

package aww.delp.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aww.delp.R;
import aww.delp.activities.DetailMapActivity;
import aww.delp.activities.DetailsActivity;
import aww.delp.helpers.DealBusinessMatcher;
import aww.delp.models.groupon.Deal;
import aww.delp.models.yelp.Business;

public class DealCardAdaptor extends RecyclerView.Adapter<DealCardAdaptor.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Deal deal;
        public Business business;
        public View view;

        public ImageView iv_dealImage;
        public TextView tv_title;
        public TextView tv_location;
        public TextView tv_buyCount;
        public TextView tv_originalPrice;
        public TextView tv_price;
        public TextView tv_rating;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            iv_dealImage = (ImageView)view.findViewById(R.id.iv_dealImage);
            tv_title = (TextView)view.findViewById(R.id.tv_title);
            tv_location = (TextView)view.findViewById(R.id.tv_location);
            tv_buyCount = (TextView)view.findViewById(R.id.tv_buyCount);
            tv_originalPrice = (TextView)view.findViewById(R.id.tv_originalPrice);
            tv_price = (TextView)view.findViewById(R.id.tv_price);
            tv_rating = (TextView)view.findViewById(R.id.tv_yelpRating);
        }
    }

    private List<Deal> deals;
    private DealBusinessMatcher dealBusinessMatcher;

    public DealCardAdaptor(Context context, List<Deal> deals) {
        this.deals = deals;
        this.dealBusinessMatcher = new DealBusinessMatcher(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.deal = deals.get(position);
        holder.tv_title.setText(holder.deal.getTitle());
        holder.tv_location.setText(holder.deal.getRedemptionLocation());
        holder.tv_buyCount.setText(holder.deal.getSoldQuantityMessage());
        holder.tv_originalPrice.setText(holder.deal.getOptions().get(0).getValue().toString());
        holder.tv_price.setText(holder.deal.getOptions().get(0).getPrice().toString());
        Picasso.with(holder.view.getContext()).load(holder.deal.getLargeImageUrl()).into(holder.iv_dealImage);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailMapActivity.class);
                intent.putExtra("location", holder.deal.getFirstOption().getFirstLocation());
//                intent.putExtra(DetailsActivity.ARGS_DEAL_UUID, holder.deal.getUuid());
                context.startActivity(intent);
            }
        });

        dealBusinessMatcher.matchDeal(holder.deal, new DealBusinessMatcher.Handler() {
            @Override
            public void onMatchYelpBusinessCompleted(Deal deal, Business business) {
                if(business != null) {
                    holder.business = business;
                    holder.tv_rating.setText(business.getRating());
                } else {
                    holder.tv_rating.setText("Yelp Rating Not Available");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }
}

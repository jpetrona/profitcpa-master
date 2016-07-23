package com.profits.cpa.checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.profits.cpa.Constants;
import com.profits.cpa.MainActivity;
import com.profits.cpa.R;
import com.profits.cpa.User;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

	private static final int TYPE_ITEM = 1;
	static User UserProfile;
	Context context;
	MainActivity Activity;
	private JSONArray jsonArray;

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		int Holderid;
		protected ImageView checkoutIcon;
		protected TextView checkoutName;
		protected TextView checkoutDesc;
		protected TextView checkoutPoint;
		Context contxt;
		MainActivity mActivity;

		public ViewHolder(View itemView, int ViewType, Context c, MainActivity Activ) {
			super(itemView);
			contxt = c;
			mActivity = Activ;
			itemView.setClickable(true);
			itemView.setOnClickListener(this);

			UserProfile = ((MainActivity) mActivity).getUserProfile();

			checkoutIcon = (ImageView) itemView.findViewById(R.id.checkout_icon);
			checkoutName = (TextView) itemView.findViewById(R.id.checkout_name);
			checkoutDesc = (TextView) itemView.findViewById(R.id.checkout_desc);
			checkoutPoint = (TextView) itemView.findViewById(R.id.checkout_point);

			Holderid = 1;

		}

		@Override
		public void onClick(View v) {
		}
	}

	CheckoutAdapter(JSONArray jsonArray, Context passedContext, MainActivity Activ) {
		this.jsonArray = jsonArray;
		Activity = Activ;
		this.context = passedContext;
	}

	@Override
	public CheckoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_row, parent, false);
		ViewHolder vhItem = new ViewHolder(v, viewType, context, Activity);

		return vhItem;

	}

	@Override
	public void onBindViewHolder(CheckoutAdapter.ViewHolder holder, int position) {
		if (holder.Holderid == 1) {
			JSONObject json;
			try {
				json = jsonArray.getJSONObject(position);

				String name = null, points = null, desc = null, imgFile = null;

				name = json.getString("name");
				points = json.getString("points");
				desc = json.getString("desc");
				imgFile = json.getString("img_file");

				holder.checkoutName.setText(name);
				holder.checkoutPoint.setText(points);
				holder.checkoutDesc.setText(desc);

				Picasso.with(context).load(Constants.BASE_URL + "ic_giftcard/" + imgFile)
						.placeholder(R.drawable.ic_launcher)
						.into(holder.checkoutIcon);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getItemCount() {
		return jsonArray.length();
	}

	@Override
	public int getItemViewType(int position) {
		return TYPE_ITEM;
	}
}
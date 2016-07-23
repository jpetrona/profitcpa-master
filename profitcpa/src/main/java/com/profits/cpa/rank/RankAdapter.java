package com.profits.cpa.rank;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.profits.cpa.MainActivity;
import com.profits.cpa.R;
import com.profits.cpa.User;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	static User UserProfile;
	private JSONArray jsonArray;
	Context context;
	MainActivity Activity;

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		int Holderid;
		protected TextView Username;
		protected TextView Credits;
		protected TextView Refs;
		protected ImageView pic;
		Context contxt;
		MainActivity mActivity;

		public ViewHolder(View itemView, int ViewType, Context c, MainActivity Activ) {
			super(itemView);
			contxt = c;
			mActivity = Activ;
			itemView.setClickable(true);
			itemView.setOnClickListener(this);

			UserProfile = ((MainActivity) mActivity).getUserProfile();
			if (ViewType == TYPE_ITEM) {

				Username = (TextView) itemView.findViewById(R.id.Username);
				pic = (ImageView) itemView.findViewById(R.id.country);
				Credits = (TextView) itemView.findViewById(R.id.Credits);
				Holderid = 1;
			} else {

				Holderid = 0;
			}

		}

		@Override
		public void onClick(View v) {
		}
	}

	RankAdapter(JSONArray jsonArray, Context passedContext, MainActivity Activ) {
		this.jsonArray = jsonArray;
		Activity = Activ;
		this.context = passedContext;
	}

	@Override
	public RankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_row, parent, false);
			ViewHolder vhItem = new ViewHolder(v, viewType, context, Activity);

			return vhItem;

		} else if (viewType == TYPE_HEADER) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_header, parent, false);
			ViewHolder vhHeader = new ViewHolder(v, viewType, context, Activity);

			return vhHeader;

		}

		return null;
	}

	@Override
	public void onBindViewHolder(RankAdapter.ViewHolder holder, int position) {
		if (holder.Holderid == 1) {
			JSONObject json;
			try {
				json = jsonArray.getJSONObject(position - 1);

				String username = null, country = null, credits = null;

				username = json.getString("id");
				country = json.getString("country");
				credits = json.getString("credits");

				if (username.equalsIgnoreCase(UserProfile.getname())) {
					holder.Username.setTextColor(context.getResources().getColor(R.color.textbg));
				}

				holder.Username.setText(username);
				holder.Credits.setText(credits);
				int resid = getId(country.toLowerCase(), R.drawable.class);
				holder.pic.setImageResource(resid);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getItemCount() {
		return jsonArray.length() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	public static int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			throw new RuntimeException("No resource ID found for: " + resourceName + " / " + c, e);
		}
	}
}
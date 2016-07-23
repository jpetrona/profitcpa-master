package com.profits.cpa;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {

	User UserProfile;
	private JSONArray dataSource;
	Context context;
	MainActivity Activity;

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		protected TextView Message;
		protected ImageView Icon;
		Context contxt;
		MainActivity mActivity;

		public ViewHolder(View itemView, int ViewType, Context c, MainActivity Activ) {
			super(itemView);
			contxt = c;
			mActivity = Activ;
			itemView.setClickable(true);
			itemView.setOnClickListener(this);

			Message = (TextView) itemView.findViewById(R.id.Message);
			Icon = (ImageView) itemView.findViewById(R.id.Icon);
		}

		@Override
		public void onClick(View v) {
		}
	}

	public NotifAdapter(JSONArray jsonArray, Context passedContext, MainActivity Activ) {
		dataSource = jsonArray;
		Activity = Activ;
		this.context = passedContext;
	}

	@Override
	public NotifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_row, parent, false);

		ViewHolder vhItem = new ViewHolder(v, viewType, context, Activity);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(NotifAdapter.ViewHolder holder, int position) {

		try {

			JSONObject json = dataSource.getJSONObject(position);

			holder.Message.setText(json.getString("message"));

			String Icon = json.getString("icon");
			String Read = json.getString("read");

			int resid1;
			int notiftype = Integer.parseInt(Icon);
			int readx = Integer.parseInt(Read);

			if (notiftype == 0) {
				if (readx == 0) {
					resid1 = getId("mycredit", R.drawable.class);
				} else {
					resid1 = getId("mycreditdark", R.drawable.class);
				}
			} else if (notiftype == 1) {
				if (readx == 0) {
					resid1 = getId("myreferral", R.drawable.class);
				} else {
					resid1 = getId("myreferraldark", R.drawable.class);
				}
			} else if (notiftype == 2) { //
				if (readx == 0) {
					resid1 = getId("mypromo", R.drawable.class);
				} else {
					resid1 = getId("mypromodark", R.drawable.class);
				}
			} else if (notiftype == 3) {
				if (readx == 0) {
					resid1 = getId("mydailylogin", R.drawable.class);
				} else {
					resid1 = getId("mydailylogindark", R.drawable.class);
				}
			} else {
				if (readx == 0) {
					resid1 = getId("mycredit", R.drawable.class);
				} else {
					resid1 = getId("mycreditdark", R.drawable.class);
				}
			}

			holder.Icon.setImageResource(resid1);

		} catch (Throwable t) {

			Log.e("Creative Error", t.getLocalizedMessage());
		}

	}

	@Override
	public int getItemCount() {
		return dataSource.length();
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
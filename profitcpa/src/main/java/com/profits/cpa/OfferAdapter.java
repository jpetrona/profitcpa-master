package com.profits.cpa;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	User UserProfile;
	private List<OfferEntity> offerList;

	Context context;
	MainActivity Activity;

	public OfferAdapter(List<OfferEntity> offerList, Context passedContext, MainActivity Activ) {
		this.offerList = offerList;
		Activity = Activ;
		this.context = passedContext;
		this.UserProfile = Activ.getUserProfile();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		int Holderid;
		protected TextView Name;
		protected TextView Description;
		protected TextView Credits;
		protected TextView Category;
		protected TextView Cost;
		protected ImageView pic;
		protected String appid;
		protected RatingBar RatingBar;
		protected Button AssignButton;
		protected Button DeleteButton;
		protected ViewFlipper viewFlipper;
		protected Spinner dynamicSpinner;
		protected String URL;
		Context contxt;
		MainActivity mActivity;

		public ViewHolder(View itemView, int ViewType, Context c, MainActivity Activ) {
			super(itemView);
			contxt = c;
			mActivity = Activ;
			itemView.setClickable(true);
			itemView.setOnClickListener(this);

			if (ViewType == TYPE_ITEM) {

				Name = (TextView) itemView.findViewById(R.id.Name);
				Category = (TextView) itemView.findViewById(R.id.category);
				Cost = (TextView) itemView.findViewById(R.id.cost);
				pic = (ImageView) itemView.findViewById(R.id.Icon);
				Description = (TextView) itemView.findViewById(R.id.Description);
				Credits = (TextView) itemView.findViewById(R.id.Credits);
				Holderid = 1;
			} else {
				viewFlipper = (ViewFlipper) itemView.findViewById(R.id.viewflipper);
				Holderid = 0;
			}

		}

		@Override
		public void onClick(View v) {
			OfferDialog();
		}

		public void OfferDialog() {
			new AlertDialog.Builder(mActivity).setTitle(Name.getText()).setMessage(Description.getText())
					.setPositiveButton("START", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (URL.equalsIgnoreCase("x")) {
								Intent share = new Intent(mActivity, NativeXActivity.class);
								share.putExtra("UserProfile", mActivity.UserProfile);
								mActivity.startActivity(share);

							} else {
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(URL));
								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								contxt.startActivity(i);

							}
						}
					})

					.setIcon(pic.getDrawable()).show();
		}

	}

	@Override
	public OfferAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row, parent, false);

			ViewHolder vhItem = new ViewHolder(v, viewType, context, Activity);

			return vhItem;

		} else if (viewType == TYPE_HEADER) {

			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_header, parent, false);

			ViewHolder vhHeader = new ViewHolder(v, viewType, context, Activity);

			return vhHeader;

		}
		return null;

	}

	@Override
	public void onBindViewHolder(OfferAdapter.ViewHolder holder, int position) {
		if (holder.Holderid == 1) {
			OfferEntity oe = offerList.get(position);
			if (position == 1) {

				Log.e("Position =1 ", "true");
				holder.Name.setText("NativeX");
				holder.Description.setText("Completed offers from NativeX");
				holder.Credits.setText("100%");
				holder.URL = "x";
				Picasso.with(context).load(R.drawable.ic_nativex).placeholder(R.drawable.ic_launcher)
						.transform(new RoundedTransformation(10, 4)).into(holder.pic);
				holder.Category.setText("Install");

			} else {
				if (!TextUtils.isEmpty(oe.getOfferImage())) {
					Picasso.with(context).load(oe.getOfferImage()).placeholder(R.drawable.ic_launcher)
							.transform(new RoundedTransformation(10, 4)).into(holder.pic);
				}

				holder.Name.setText(oe.getOfferName());
				holder.Description.setText(oe.getOfferDesc());
				holder.Credits.setText(oe.getPayoutCost());
				holder.URL = oe.getUrl(UserProfile.getid());
				holder.Category.setText("Install");
			}

		} else {
			String[] items = new String[] { "Chai Latte", "Green Tea", "Black Tea" };

			holder.viewFlipper.setAutoStart(true);
			holder.viewFlipper.setFlipInterval(6000);
			holder.viewFlipper.startFlipping();
			holder.viewFlipper.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent share = new Intent(android.content.Intent.ACTION_SEND);
					share.setType("text/plain");
					share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="
							+ Activity.getApplicationContext().getPackageName());

					Activity.startActivity(Intent.createChooser(share, "Share your Invitation link"));
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return offerList.size() - 1;
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

}
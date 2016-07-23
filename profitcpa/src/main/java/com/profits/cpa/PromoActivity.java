package com.profits.cpa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PromoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promo);
		ProfitCPAApplication.getInstance().trackScreenView("PromoActivity");
		Intent intent = getIntent();
		int action = intent.getIntExtra("action", 0);
		String img = intent.getStringExtra("img");
		final String link = intent.getStringExtra("link");
		ImageView imagex = (ImageView)findViewById(R.id.image);
		Button buttonx = (Button)findViewById(R.id.button);
		if (action==0){
			 Intent intentx = new Intent(PromoActivity.this, MainActivity.class);
     		 startActivity(intentx);
     		 finish();
		}
		else {
			 Picasso.with(this)
	         .load(img)
	         .into(imagex);
			 
			if (action==2){// Show button if this a promo
				buttonx.setVisibility(View.VISIBLE);
			}
			else { // Hide Button if this is a shutdown
				buttonx.setVisibility(View.GONE);	
			}
		}
		imagex.setOnClickListener(new OnClickListener() {
			 @Override
			public void onClick(View view) {
				 Intent i = new Intent(Intent.ACTION_VIEW);
				 i.setData(Uri.parse(link));
				 startActivity(i); 
			 }
			});
		
		buttonx.setOnClickListener(new OnClickListener() {
			 @Override
			public void onClick(View view) {
//				 Intent intentx = new Intent(PromoActivity.this, MainActivity.class);
//	     		 startActivity(intentx);
	     		 finish(); 
			 }
			});
		
		
	}
}

package com.flapps.mobile.android.flapps;

import com.flapps.mobile.android.flapps.R;

import java.io.IOException;
import java.util.ArrayList;

import com.flapps.mobile.android.flapps.config.ApiConfig;
import com.flapps.mobile.android.flapps.data.AppStatus;
import com.flapps.mobile.android.flapps.data.DataModel;
import com.flapps.mobile.android.flapps.data.DataSync;
import com.flapps.mobile.android.flapps.data.MenuConfigBean;
import com.flapps.mobile.android.flapps.utils.Globals;
import com.flapps.mobile.android.flapps.utils.ScreenSlidePageFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;

public class IntroActivity extends FragmentActivity {

	private static final int NUM_PAGES = 4;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		overridePendingTransition(R.anim.swipe_left_in, R.anim.swipe_left_out);
		setContentView(R.layout.activity_intro);
		getActionBar().hide();

        final Button signInButton = (Button)findViewById(R.id.intro_btn_login_1);

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener(){
			 
             @Override
             public void onPageScrollStateChanged(int position) {}
             @Override
             public void onPageScrolled(int arg0, float arg1, int arg2) {}
             @Override
             public void onPageSelected(int position) {
            	 IntroActivity.this.switchDots( position );

                 if ( position == 3 ) {
                     signInButton.animate()
                             .alpha(0f)
                             .setDuration(500)
                             .setListener(new AnimatorListenerAdapter() {
                                 @Override
                                 public void onAnimationEnd(Animator animation) {
                                     signInButton.setVisibility(View.GONE);
                                 }
                             });
                 } else {
                     if ( signInButton.getVisibility() != View.VISIBLE ) {
                         signInButton.setAlpha(0f);
                         signInButton.setVisibility(View.VISIBLE);
                         signInButton.animate()
                                 .alpha(1f)
                                 .setDuration(500)
                                 .setListener(null);
                     }
                 }
             }

         });
		
	}

    @Override
    protected void onResume() {
        if ( Globals.scrollToSignIn ) mPager.setCurrentItem( 3, false );
        super.onResume();
    }

    @Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	public void toAccount(View v) {
		Intent intent = new Intent(getApplicationContext(),
				AccountActivity.class);
		startActivity(intent);
	//	IntroActivity.this.finish();
	}

	public void toLogin(View v) {

        mPager.setCurrentItem( 3, true );
	}
	
	public void switchDots( int position ) {
		
		ImageView isd = (ImageView) findViewById(R.id.info_swipe_dots);
		switch (position) {
		case 0:
			isd.setImageResource(R.drawable.tutorial_swipe_1);
		break;
		case 1:
			isd.setImageResource(R.drawable.tutorial_swipe_2);
		break;
		case 2:
			isd.setImageResource(R.drawable.tutorial_swipe_3);
		break;
		case 3:
			isd.setImageResource(R.drawable.tutorial_swipe_4);
		break;
		}
		//Toast.makeText(IntroActivity.this, ""+position, Toast.LENGTH_LONG).show();
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(
				android.support.v4.app.FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {

			ScreenSlidePageFragment sspf = new ScreenSlidePageFragment();
			sspf.setPage(position);
			return sspf;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
	
	public void login(View v) {
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.pbHeaderProgress);
		pb.setVisibility(View.VISIBLE);
		
		EditText te = (EditText) findViewById(R.id.login_domain);
		Globals.domain = te.getText().toString();

		te = (EditText) findViewById(R.id.login_login);
		Globals.login = te.getText().toString();

		te = (EditText) findViewById(R.id.login_pass);
		Globals.password = te.getText().toString();
		
		te = (EditText) findViewById(R.id.login_server);
		Globals.server = te.getText().toString();
		
		AppStatus as = new AppStatus(IntroActivity.this);
		as.setCredentials(Globals.domain, Globals.login, Globals.password);

		//new DataConnector(IntroActivity.this).execute("loginintro", "");
		new DataSync(IntroActivity.this).execute("login", "");
	}
	
	public void logged(String s) {
		
		//Toast.makeText(IntroActivity.this, "returned: "+s, Toast.LENGTH_SHORT).show();
		if ( "".equals(s.trim())) {
			Toast.makeText(IntroActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
			
		} else {
			//DataModel dm = new DataModel(IntroActivity.this);
			try {
				DataModel.saveFile( IntroActivity.this, s );
				deleteFile(Globals.absencesfile);
				deleteFile(Globals.timerecordfile);
				deleteFile(Globals.expensesfile);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MenuConfigBean mcb = new MenuConfigBean();
			try {
				mcb = new ApiConfig(IntroActivity.this).getMenuConfig();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<String> menuitems = mcb.getItems();
			if ( menuitems.contains("attendanceChecker") ) {
				
				Intent intent = new Intent(getApplicationContext(), ClockInOutActivity.class);
				startActivity(intent);
				IntroActivity.this.finish();
			} else {
	
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				IntroActivity.this.finish();
			}
		}
		ProgressBar pb = (ProgressBar) findViewById(R.id.pbHeaderProgress);
		pb.setVisibility(View.INVISIBLE);
	}
	

}

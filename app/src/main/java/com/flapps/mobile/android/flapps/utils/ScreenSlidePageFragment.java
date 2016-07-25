package com.flapps.mobile.android.flapps.utils;

import com.flapps.mobile.android.flapps.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScreenSlidePageFragment extends Fragment {
	
	private int page = 0;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		int layout = 0;
		switch( page ) {
			case 0: layout = R.layout.activity_intro1; break;
			case 1: layout = R.layout.activity_intro2; break;
			case 2: layout = R.layout.activity_intro3; break;
			case 3: layout = R.layout.activity_intro4; break;
		}
		
        ViewGroup rootView = (ViewGroup) inflater.inflate(
        		layout, container, false);

        return rootView;
    }

}

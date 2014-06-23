package com.ideabag.playtunes.fragment;

import com.ideabag.playtunes.R;
import com.ideabag.playtunes.activity.MainActivity;
import com.ideabag.playtunes.adapter.SongsAllAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

public class SongsFragment extends ListFragment {
	
    private MainActivity mActivity;
    
	SongsAllAdapter adapter;
	
	
	@Override public void onAttach( Activity activity ) {
		super.onAttach( activity );
		
		mActivity = ( MainActivity ) activity;
		
	}
    
	@Override public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		
		ActionBar bar =	( ( ActionBarActivity ) getActivity() ).getSupportActionBar();
		
    	
    	adapter = new SongsAllAdapter( getActivity() );
    	
    	
    	
    	setListAdapter( adapter );
    	
    	getListView().setItemsCanFocus( true );
    	
    	bar.setTitle( "Songs" );
		//bar.setSubtitle( cursor.getCount() + " songs" );
    	
    	getView().setBackgroundColor( getResources().getColor( android.R.color.white ) );
    	
	}
		
	@Override public void onResume() {
		super.onResume();
		
		
	}
		
	@Override public void onPause() {
		super.onPause();
		
		
	}
	
	@Override public void onListItemClick( ListView l, View v, int position, long id ) {
		
		android.util.Log.i( "Tapped Song", "" + position );
		// Play the song
		/*
		Intent servicePlay = new Intent( getActivity(), MusicPlayerService.class );
		
		servicePlay.setAction(  getString( R.string.action_play )  );
		
		servicePlay.putExtra( "position", position );
		servicePlay.putExtra( "command", CommandUtils.COMMAND_SONGS_CODE );
		
		getActivity().startService( servicePlay );
		*/
		
	}
	
	
}
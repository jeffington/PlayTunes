package com.ideabag.playtunes.fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ideabag.playtunes.R;
import com.ideabag.playtunes.activity.MainActivity;
import com.ideabag.playtunes.adapter.PlaylistsAllAdapter;
import com.ideabag.playtunes.dialog.CreatePlaylistDialogFragment;
import com.ideabag.playtunes.dialog.PlaylistMenuDialogFragment;
import com.ideabag.playtunes.dialog.SongMenuDialogFragment;
import com.ideabag.playtunes.util.MergeAdapter;
import com.ideabag.playtunes.util.TrackerSingleton;
import com.ideabag.playtunes.view.AllPlaylistsDialogBuilder;

import android.app.Activity;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PlaylistsAllFragment extends ListFragment {
	
	public static final String TAG = "All Playlists Fragment";
	
	PlaylistsAllAdapter playslistsAdapter;
	MergeAdapter adapter;
	
	MainActivity mActivity;
	
	//AllPlaylistsDialogBuilder dialogBuilder;
	
	
	ContentObserver playlistsChanged = new ContentObserver( new Handler() ) {
		
        @Override public void onChange( boolean selfChange ) {
            
            mActivity.runOnUiThread( new Runnable() {

				@Override public void run() {
					
					playslistsAdapter.requery();
					playslistsAdapter.notifyDataSetChanged();
					
				}
            	
            });
            
            super.onChange( selfChange );
            
        }

	};
	
	@Override public void onAttach( Activity activity ) {
			
		super.onAttach( activity );
		
		mActivity = ( MainActivity ) activity;
		
	}
    
	@Override public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		
		ActionBar bar =	( ( ActionBarActivity ) getActivity()).getSupportActionBar();
		
		adapter = new MergeAdapter();
		playslistsAdapter = new PlaylistsAllAdapter( getActivity(), playlistMenuClickListener );
		

    	
    	LayoutInflater inflater = mActivity.getLayoutInflater();
    	LinearLayout starredPlaylist = ( LinearLayout ) inflater.inflate( R.layout.list_item_playlist_starred, null );
    	
    	starredPlaylist.setTag( R.id.tag_playlist_id, mActivity.PlaylistManager.createStarredIfNotExist() );
    	
    	( ( TextView ) starredPlaylist.findViewById( R.id.BadgeCount ) ).setText( "" + mActivity.PlaylistManager.getStarredCursor().getCount() );
		
    	
    	bar.setTitle( getString( R.string.playlists_plural ) );
    	mActivity.actionbarTitle = bar.getTitle();
    	mActivity.actionbarSubtitle = null;
		bar.setSubtitle( null );
    	
    	setHasOptionsMenu( true );
    	
    	getView().setBackgroundColor( getResources().getColor( android.R.color.white ) );
		getListView().setDivider( getResources().getDrawable( R.drawable.list_divider ) );
		getListView().setDividerHeight( 1 );
		getListView().setSelector( R.drawable.list_item_background );
		
		//adapter.addView( mActivity.AdContainer, false );
		adapter.addView( starredPlaylist );
		adapter.addAdapter( playslistsAdapter );
		
    	setListAdapter( adapter );
    	
	}
	
	
	
	@Override public void onResume() {
		super.onResume();
		
		//dialogBuilder = new AllPlaylistsDialogBuilder( mActivity, mActivity.PlaylistManager );
		
		getActivity().getContentResolver().registerContentObserver( MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, true, playlistsChanged );
		
		Tracker t = TrackerSingleton.getDefaultTracker( mActivity.getBaseContext() );

	    // Set screen name.
	    // Where path is a String representing the screen name.
		t.setScreenName( TAG );
		
	    // Send a screen view.
		t.send( new HitBuilders.AppViewBuilder().build() );
		
		t.send( new HitBuilders.EventBuilder()
    	.setCategory( "playlist" )
    	.setAction( "show" )
    	.setLabel( TAG )
    	.setValue( adapter.getCount() )
    	.build());
		
		//mActivity.AdView.resume();
		
	}
		
	@Override public void onPause() {
		super.onPause();
		
		//dialogBuilder = null;
		
		getActivity().getContentResolver().unregisterContentObserver( playlistsChanged );
		
		//mActivity.AdView.pause();
		
	}
	
	@Override public void onDestroy() {
		super.onDestroy();
		
		//getListView().removeHeaderView( mActivity.AdContainer );
		
	}
	
	@Override public void onDestroyView() {
	    super.onDestroyView();
	    
	    setListAdapter( null );
	    
	}
	
	@Override public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate( R.menu.menu_playlists_all, menu );
		
	}
	
	@Override public boolean onOptionsItemSelected( MenuItem item ) {
 
        super.onOptionsItemSelected(item);
        
        int id = item.getItemId();
        
        if ( id == R.id.MenuPlaylistsAdd ) {
        	
        	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        	
        	DialogFragment newFragment = new CreatePlaylistDialogFragment();
        	
            newFragment.show(ft, "dialog");
        	
        	
        }
        
        return true;
        
	}
	
	@Override public void onListItemClick( ListView l, View v, int position, long id ) {
		
		String playlist_id = (String) v.getTag( R.id.tag_playlist_id );
		
		PlaylistsOneFragment playlistFragment = new PlaylistsOneFragment();
		
		playlistFragment.setMediaID( playlist_id );
		
		mActivity.transactFragment( playlistFragment );
		
	}
	
	public View.OnClickListener playlistMenuClickListener = new View.OnClickListener() {
		
		@Override public void onClick(View v) {
			
			//int id = v.getId();
			
			ViewGroup list_item = ( ViewGroup ) v.getParent();
			String playlist_id = ( String ) list_item.getTag( R.id.tag_playlist_id);
			
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        	
			PlaylistMenuDialogFragment newFragment = new PlaylistMenuDialogFragment();
			newFragment.setMediaID( playlist_id );
        	
            newFragment.show( ft, "dialog" );
			
		}
		
	};

	
}
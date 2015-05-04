package com.example.mycontactlist;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class ContactListActivity extends ListActivity {

	boolean isDeleting = false;
	ContactAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
//		initListButton();
		initMapButton();
		initSettingsButton();
		initDeleteButton();
		initAddContactButton();

		// Add code from Listing 6.3 Simple List Activation Code P119
		ContactDataSource ds = new ContactDataSource(this);
		ds.open();
		// Retrieve names
		//ArrayList<String> names = ds.getContactName();

		// Retrieve Contact objects - copied from onResume method below.
		// This wasn't mentioned in the textbook...
		String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
		String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
		final ArrayList<Contact> contacts = ds.getContacts(sortBy, sortOrder);
		ds.close();

		// Added this code from Listing 6.8 - Does this work?
		// Comment out this code from Listing 6.13 Modified onItemClick method
		//setListAdapter(new ContactAdapter(this, contacts));

		// Add two lines below instead from Listing 6.13 Modified onItemClick method
		adapter = new ContactAdapter(this, contacts);
		setListAdapter(adapter);

		// Add code from Listing 6.4 Code to Respond to an Item Click P121
		// Modified with code from Listing 6.8 Selected Item Click P127
		ListView listview = getListView();
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

				Contact selectedContact = contacts.get(position);
				if (isDeleting) {
					adapter.showDelete(position, itemClicked, ContactListActivity.this, selectedContact);
				}
				else {
					Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
					intent.putExtra("contactid", selectedContact.getContactID());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
		String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

		ContactDataSource ds = new ContactDataSource(this);
		ds.open();
		final ArrayList<Contact> contacts = ds.getContacts(sortBy, sortOrder);
		ds.close();
		
		if (contacts.size() > 0) {

			adapter = new ContactAdapter(this, contacts);
			setListAdapter(adapter);
			ListView listView = getListView();
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View itemClicked,
						int position, long id) {
					Contact selectedContact = contacts.get(position);
					if (isDeleting) {
						adapter.showDelete(position, itemClicked, ContactListActivity.this, selectedContact);
					}
					else {
						Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
						intent.putExtra("contactid", selectedContact.getContactID());
						startActivity(intent);
					}
				}
			});
		}
		else {
			Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
			startActivity(intent);
		}
	}
	
	private void initAddContactButton() {
		Button newContact = (Button) findViewById(R.id.buttonAdd);
		newContact.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
    			startActivity(intent);
            }
        });
	}

	private void initDeleteButton() {
		final Button deleteButton = (Button) findViewById(R.id.buttonDelete);
		deleteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				if (isDeleting) {
					deleteButton.setText("Delete");
					isDeleting = false;

				    adapter.notifyDataSetChanged();
				}
				else {
					deleteButton.setText("Done Deleting");
					isDeleting = true;
				}
            }
        });
	}

	
//	private void initListButton() {
//        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
//        list.setEnabled(false);
//	}
	
	private void initMapButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}
	private void initSettingsButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}


}

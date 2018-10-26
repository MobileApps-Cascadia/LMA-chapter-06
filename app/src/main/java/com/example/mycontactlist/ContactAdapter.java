package com.example.mycontactlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> items;
    private Context adapterContext;
    public int colorCounter = 0;

    public ContactAdapter(Context context, ArrayList<Contact> items) {
            super(context, R.layout.list_item, items);
            adapterContext = context;
            this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	try {
            Contact contact = items.get(position);
            
            if (v == null) {
            		LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            		v = vi.inflate(R.layout.list_item, null);
            }

            TextView contactName = (TextView) v.findViewById(R.id.textContactName);
            TextView contactNumber = (TextView) v.findViewById(R.id.textPhoneNumber);
            TextView contactCellNumber = (TextView) v.findViewById(R.id.textCellNumber);
            TextView contactStreetAddress = (TextView) v.findViewById(R.id.textStreetAddress);
            TextView contactCity = (TextView) v.findViewById(R.id.textCity);
            TextView contactState = (TextView) v.findViewById(R.id.textState);
            TextView contactZipCode = (TextView) v.findViewById(R.id.textZipCode);
        	Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
            	
        	contactName.setText(contact.getContactName());
        	contactStreetAddress.setText(contact.getStreetAddress() +", ");
        	contactCity.setText(contact.getCity() + ", ");
        	contactState.setText(contact.getState() + ". ");
        	contactZipCode.setText(contact.getZipCode());
        	contactNumber.setText("Home: " + contact.getPhoneNumber());
        	contactCellNumber.setText("Cell: " + contact.getCellNumber());
            b.setVisibility(View.INVISIBLE);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		e.getCause();
    	}
            return v;
    }
    
    public void showDelete(final int position, final View convertView, final Context context, final Contact contact) {
    	View v = convertView;
    	final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);

    	if (b.getVisibility()==View.INVISIBLE) {
    		b.setVisibility(View.VISIBLE);
    		b.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				hideDelete(position, convertView, context);
    				items.remove(contact);
    				deleteOption(contact.getContactID(), context);
    			}

    		});
    	}
    	else {
			hideDelete(position, convertView, context);
    	}
    }

	private void deleteOption(int contactToDelete, Context context) {
		ContactDataSource db = new ContactDataSource(context);
		db.open();
		db.deleteContact(contactToDelete);
		db.close();	
		this.notifyDataSetChanged();
	}
 
	private void hideDelete(int position, View convertView, Context context) {
      View v = convertView;
      final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
	  b.setVisibility(View.INVISIBLE);
	  b.setOnClickListener(null);
 }

}

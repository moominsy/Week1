package com.example.tab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TAB1//
    Button btnShowContacts1;
    Button btnShowContacts2;
    int check = 0;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ArrayList<String> numberlist = new ArrayList<>();
    //TAB2//

    //TAB3//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TAB1//

        numberlist.add("01012345678");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, numberlist) ;

        ListView listview = (ListView) findViewById(R.id.listView) ;
        listview.setAdapter(adapter) ;
        btnShowContacts1 = (Button) findViewById(R.id.btnShowContact1);

        btnShowContacts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestContactPermission();
                ArrayList<Contact> contactlist = getContacts();
                ArrayList<String> numberlist1 = getnumberlist(contactlist);
                for(int i = 0; i <numberlist1.size(); i++){
                    numberlist.add(numberlist1.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        });

        //TAB2//

        //TAB3//

        //TAB//
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs) ;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition() ;
                changeView(pos) ;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }) ;
    }

    //TAB1//

    private ArrayList<Contact> getContacts() {
        //TODO get contacts code here
        Toast.makeText(this, "Get contacts ....", Toast.LENGTH_LONG).show();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER, // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(contactCursor.getString(1));
                acontact.setName(contactCursor.getString(2));
                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }
        return contactlist;
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private ArrayList<String> getnumberlist(ArrayList<Contact> contactlist){
        ArrayList<String> numberlist = new ArrayList<String>();
        if(contactlist.isEmpty()){
            numberlist.add("01012345678");
        }
        else{
            for(int i = 0; i < contactlist.size(); i++){
                numberlist.add(contactlist.get(i).getPhonenum());
            }
        }
        return numberlist;
    }

    //TAB2//

    //TAB3//

    //TAB//
    private void changeView(int index) {
        LinearLayout TAB1 = (LinearLayout) findViewById(R.id.TAB1) ;

        switch (index) {
            case 0 :
                TAB1.setVisibility(View.VISIBLE);
                break ;
            case 1 :
                TAB1.setVisibility(View.INVISIBLE);
                break ;
            case 2 :
                TAB1.setVisibility(View.INVISIBLE);
                break ;

        }
    }
}
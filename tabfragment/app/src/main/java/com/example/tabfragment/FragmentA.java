package com.example.tabfragment;

import android.annotation.TargetApi;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentA extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentA() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentA.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentA newInstance(String param1, String param2) {
        FragmentA fragment = new FragmentA();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Button btnShowContacts1;
    Button add;
    Button delete;
    Button modify;
    Context mcontext;

    int check = 0;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ArrayList<String> numberlist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<Long> idlist = new ArrayList<>();
    ArrayList<Uri> urllist = new ArrayList<>();
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    HashMap<String, String> item = new HashMap<>();


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mcontext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_a, container, false);

        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), list, android.R.layout.simple_list_item_2, new String[] {"name", "number"}, new int[]{android.R.id.text1, android.R.id.text2} ) ;
        //ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, namelist);
        ListView listview = (ListView) rootView.findViewById(R.id.listView) ;
        listview.setAdapter(adapter) ;

        btnShowContacts1 = (Button) rootView.findViewById(R.id.btnShowContact1);
        add = (Button) rootView.findViewById(R.id.add);
        delete = (Button) rootView.findViewById(R.id.delete);

        btnShowContacts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestContactPermission();
                ArrayList<Contact> contactlist = getContacts();
                ArrayList<String> namelist1 = getnamelist(contactlist);
                ArrayList<String> numberlist1 = getnumberlist(contactlist);
                ArrayList<Uri> urllist1 = geturllist(contactlist);
                ArrayList<Long> idlist1 = getidlist(contactlist);
                namelist.clear();
                numberlist.clear();
                urllist.clear();
                idlist.clear();
                list.clear();
                for(int i = 0; i <max(namelist.size(),namelist1.size()); i++) {
                    namelist.add(namelist1.get(i));
                    numberlist.add(numberlist1.get(i));
                    urllist.add(urllist1.get(i));
                    idlist.add(idlist1.get(i));
                }
                for(int i = 0; i <numberlist.size(); i++) {
                    item = new HashMap<>();
                    item.put("name", namelist.get(i));
                    item.put("number", numberlist.get(i));
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT, Uri.parse("content://contacts/people"));
                startActivity(intent);
            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(Intent.ACTION_INSERT, Uri.parse(String.valueOf(urllist.get(0))));
                startActivity(intent);

                 */
                deleteContact(mcontext, idlist.get(0) );
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {



                Intent editIntent = new Intent(Intent.ACTION_EDIT);
                editIntent.setDataAndType(urllist.get(position), ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                editIntent.putExtra("finishActivityOnSaveCompleted", true);
                startActivity(editIntent);

            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View v, int position, long id) {

                Intent editIntent = new Intent(Intent.ACTION_VIEW);
                editIntent.setDataAndType(urllist.get(position), ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                editIntent.putExtra("finishActivityOnSaveCompleted", true);
                startActivity(editIntent);
                return false;
            }
        });




        return rootView;
    }

    private int max(int size, int size1) {
        if(size < size1 ){
            return size1;
        }
        else return size;
    }

    private ArrayList<Contact> getContacts() {
        //TODO get contacts code here
        Toast.makeText(this.getActivity(), "Get contacts ....", Toast.LENGTH_LONG).show();

        //
        int lookupKeyIndex;
        int idIndex;
        String currentLookupKey;
        long currentId;
        Uri selectedContactUri;
        //

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.CommonDataKinds.Phone.NUMBER, // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts._ID}; // 연락처 이름

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = getActivity().getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        //
        if (contactCursor.moveToFirst()) {

            int idindex = contactCursor.getColumnIndex("_id");

            do {
                Contact acontact = new Contact();
                //acontact.setPhotoid(contactCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                acontact.setPhonenum(contactCursor.getString(1));
                acontact.setName(contactCursor.getString(2));



                lookupKeyIndex = contactCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                currentLookupKey = contactCursor.getString(lookupKeyIndex);
                idIndex = contactCursor.getColumnIndex(ContactsContract.Contacts._ID);
                currentId = contactCursor.getLong(idIndex);
                selectedContactUri = ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey);
                acontact.setURL(selectedContactUri);

                int id = contactCursor.getInt(idindex);
                acontact.setPhotoid(currentId);


                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }
        return contactlist;
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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
                    ActivityCompat.requestPermissions(this.getActivity(),
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
                    Toast.makeText(this.getActivity(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private ArrayList<String> getnumberlist(ArrayList<Contact> contactlist){
        ArrayList<String> numberlist = new ArrayList<String>();
        if(contactlist.isEmpty()){
            numberlist.add("연락처가 비었습니다");
        }
        else{
            for(int i = 0; i < contactlist.size(); i++){
                numberlist.add(contactlist.get(i).getPhonenum());
            }
        }
        return numberlist;
    }

    private ArrayList<String> getnamelist(ArrayList<Contact> contactlist){
        ArrayList<String> namelist = new ArrayList<String>();
        if(contactlist.isEmpty()){
            namelist.add("01000000000");
        }
        else{
            for(int i = 0; i < contactlist.size(); i++){
                namelist.add(contactlist.get(i).getName());
            }
        }
        return namelist;
    }

    private ArrayList<Long> getidlist(ArrayList<Contact> contactlist){
        ArrayList<Long> idlist = new ArrayList<Long>();
        if(contactlist.isEmpty()){
            idlist.add(0L);
        }
        else{
            for(int i = 0; i < contactlist.size(); i++){
                idlist.add(contactlist.get(i).getPhotoid());
            }
        }
        return idlist;
    }

    private ArrayList<Uri> geturllist(ArrayList<Contact> contactlist){
        ArrayList<Uri> urllist = new ArrayList<Uri>();
        if(contactlist.isEmpty()){
        }
        else{
            for(int i = 0; i < contactlist.size(); i++){
                urllist.add(contactlist.get(i).getURL());
            }
        }
        return urllist;
    }
    private void deleteContact(Context context, long rawContactId) {
        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,
                ContactsContract.RawContacts.CONTACT_ID + " = " + rawContactId,
                null);
    }

}
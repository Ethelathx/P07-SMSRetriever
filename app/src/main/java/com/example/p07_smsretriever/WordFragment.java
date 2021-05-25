package com.example.p07_smsretriever;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordFragment extends Fragment {

    Button btnRetrieve,btnRetrieveAll;
    EditText etWord;
    TextView tvResult;
    public WordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_word, container, false);
        btnRetrieve = view.findViewById(R.id.btnRetrieveTextFrag1);
        etWord = view.findViewById(R.id.etInput1);
        tvResult = view.findViewById(R.id.tvFrag1);
        btnRetrieveAll = view.findViewById(R.id.buttonRetrieveAll);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int permissionCheck = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 0);
                    return;
                }

                Uri uri = Uri.parse("content://sms");

                String filter = "body LIKE ?";
                String word = etWord.getText().toString();
                String[] filterArgs = {"%" + word + "%"};

                String[] reqCols = new String[]{"date","address","body","type"};

                ContentResolver cr = getActivity().getContentResolver();

                Cursor cursor = cr.query(uri, reqCols, filter,filterArgs,null);
                String smsBody = "";
                if(cursor.moveToFirst()){
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat.format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox: ";
                        } else {
                            type = "Sent: ";
                        }
                        smsBody += type + " " + address+ "\n at " + date +"\n\"" + body +"\"\n\n";
                    } while(cursor.moveToNext());
                }
                tvResult.setText(smsBody);
            }
        });

        btnRetrieveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);

                if(permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String []{Manifest.permission.READ_SMS},0);
                    return;
                }
                Uri uri = Uri.parse("content://sms");

                String [] reqCols = new String[]{"date","address","body","type"};
                String[] array = etWord.getText().toString().split(",");
                ContentResolver cr =  getActivity().getContentResolver();
                String filter = "body LIKE ?";
                String[] filterArgs = new String[array.length];
                String smsBody = "";
                System.out.println(array.length);
                for(int i = 0 ;i<array.length;i++){
                    String name = "%"+array[i]+"%";
                    if(i > 0){
                        filter+="OR body LIKE?";
                    }
                    filterArgs[i]="%"+array[i].toString()+"%";

                }
                Cursor cursor = cr.query(uri,reqCols,filter,filterArgs,null);
                if(cursor.moveToFirst()){
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat.format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        address = address.replaceAll("\\D+", "");
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " "+ address+"\n"+" at "+date +"\n\"" +body +"\"\n\n";
                    }while(cursor.moveToNext()) ;

                }

                tvResult.setText(smsBody);
            }


        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, Do the read SMS as if the btnRetrieve is clicked
                    btnRetrieve.performClick();
                } else {
                    // permission was denied, notify user
                    Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
package com.example.android.interactivewithotherapps;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.Normalizer;

public class MainActivity extends AppCompatActivity {
    EditText et;
    final int REQUEST_CODE_CONTACTS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText)findViewById(R.id.editText);
    }

    public void mail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
       // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse
        // ("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        startActivity(emailIntent);
    }

    public void web(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(createWebURL(et.getText()
                .toString())));
        startActivity(intent);
    }
    public void tel(View view){
        String telNum = createTelNumString(et.getText().toString());
        Uri numUri = Uri.parse("tel:" + telNum);
        Intent intent = new Intent(Intent.ACTION_DIAL,numUri);
        startActivity(intent);
    }
    public void map(View view){
        Uri location = Uri.parse("geo:0,0?q=" + Uri.encode("北海道札幌市厚別区大谷地東7"));
        Intent intent = new Intent(Intent.ACTION_VIEW,location);
        startActivity(intent);
    }
    public void cal(View view){

    }

    public void getContact(View view){
        Uri contactUri = Uri.parse("content://contacts");
        Intent intent = new Intent(Intent.ACTION_PICK,contactUri);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent,REQUEST_CODE_CONTACTS);
    }

    public String createTelNumString(String telNum){
        telNum = Normalizer.normalize(telNum, Normalizer.Form.NFKC);
        if(telNum.contains("-")||telNum.contains("ー")){
            telNum = telNum.replace("-","");
            telNum = telNum.replace("ー","");
        }
        if(telNum.matches("[0-9]*")) {
            return telNum;
        }else{
            showIllegalInputToast();
            return null;
        }
    }
    public String createWebURL(String webString){
        if(!webString.contains("http://")){
            webString = "http://" + webString;
        }
        return  Normalizer.normalize(webString, Normalizer.Form.NFKC);

    }


    public void showIllegalInputToast(){
        Toast.makeText(this,"入力テキストを確認してください",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CONTACTS && resultCode == RESULT_OK){
            Uri returnedUri = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(returnedUri,null,null,null,null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNum = cursor.getString(index);
            Toast.makeText(this,phoneNum,Toast.LENGTH_LONG).show();
        }
    }
}

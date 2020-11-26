package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Uri uri;
    String amount = "PRE_DEFINED_MONEY";
    String name = "RECEIVER_FULL_NAME";
    String upiId = "RECEIVER_GOOGLE_PAY_BUSINESS_UPI_ID";
    String transactionNote = "pay test";
    String status = "";
    String packageName = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void payclick(View view){
        uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
        payWithGpay();
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                .appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithGpay() {
        if(isAppInstalled(this, packageName)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        }
        else{
            Toast.makeText(MainActivity.this, "Google pay not installed",Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isAppInstalled(Context context, String packageName){
        try{
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            status = data.getStringExtra("Status").toLowerCase();
        }

        if((RESULT_OK == resultCode) && status.equals("success")){
            Toast.makeText(MainActivity.this, "Transaction Successful", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Transaction Failed", Toast.LENGTH_LONG).show();
        }
    }
}
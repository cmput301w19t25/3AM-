package comnickdchee.github.a3am.Barcode;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import comnickdchee.github.a3am.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends Activity implements ZXingScannerView.ResultHandler {

    private int CAMERA_PERMISSION_CODE = 1;
    private ZXingScannerView mScannerView;
    private static final String TAG = "BarcodeScanner";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        if (ContextCompat.checkSelfPermission(BarcodeScanner.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

        }
        else
        {
            requestCameraPermission();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume


    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.stopCamera();
        finish();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent output = new Intent();
        output.putExtra("isbn", rawResult.getText());
        setResult(RESULT_OK, output);
        Log.d(TAG, "handleResult: Sent ISBN");
        finish();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }



    private void requestCameraPermission (){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))
        {
            // This is needed if we need to give an extra explanation
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Need camera permission for Barcode Scanner")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(BarcodeScanner.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create().show();

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {
                Toast.makeText(this,"Permission Denied for Camera",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }
}

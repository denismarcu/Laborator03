package ro.pub.cs.systems.eim.lab03.phonedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Toast;

public class PhoneDialerActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private ImageButton callButton;
    private ImageButton rejectButton;
    private ImageButton backspaceButton, contactsButton;
    private Button genericButton;

    private BackspaceButtonListener backspaceButtonClickListener = new BackspaceButtonListener();
    private class BackspaceButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String myNumber = phoneNumber.getText().toString();
            if (myNumber.length() > 0) {
                myNumber = myNumber.substring(0, myNumber.length() - 1);
                phoneNumber.setText(myNumber);
            }
        }
    }

    private RejectButtonListener rejectButtonClickListener = new RejectButtonListener();
    private class RejectButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private CallButtonListener callButtonClickListener = new CallButtonListener();
    private class CallButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.PERMISSION_REQUEST_CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
                startActivity(intent);
            }
        }
    }

    private GenericButtonClickListener genericButtonClickListener = new GenericButtonClickListener();
    private class GenericButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phoneNumber.setText(phoneNumber.getText().toString() + ((Button) view).getText().toString());
        }
    }

    private ContactsButtonClickListener contactsButtonClickListener = new ContactsButtonClickListener();
    private class ContactsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            String phoneNum = phoneNumber.getText().toString();
            if (phoneNum.length() > 0) {
                Intent intent = new Intent("contactsmanager.lab04.eim.systems.cs.pub.ro.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("PHONE_NUMBER_KEY", phoneNum);
                startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);

       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        phoneNumber = (EditText) findViewById(R.id.phone_number_edit_text);

        backspaceButton = (ImageButton) findViewById(R.id.backspace_image_button);
        backspaceButton.setOnClickListener(backspaceButtonClickListener);

        callButton = (ImageButton) findViewById(R.id.call_image_button);
        callButton.setOnClickListener(callButtonClickListener);

        rejectButton = (ImageButton) findViewById(R.id.button_reject);
        rejectButton.setOnClickListener(rejectButtonClickListener);

        contactsButton = (ImageButton) findViewById(R.id.contacts_button);
        contactsButton.setOnClickListener(contactsButtonClickListener);

        for (int i = 0; i < Constants.buttonIds.length; i++) {
            genericButton = (Button) findViewById(Constants.buttonIds[i]);
            genericButton.setOnClickListener(genericButtonClickListener);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                Toast.makeText(this, "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

}

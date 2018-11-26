package com.example.jap96.myshopdemo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;


public class RegistActivity extends AppCompatActivity {
    private static final String DB_FILE = "Members.db",
                                DB_TABLE ="members";
    private SQLiteDatabase MemberDb;
    private EditText mEdtAccount,
                     mEdtPassword,
                     mEdtIdentity,
                     mEdtEmail;
    private Button btnRegistSure;
    Number shoppoint = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        MemberDbOpenHelper memberDbOpenHelper = new MemberDbOpenHelper(getApplicationContext(),DB_FILE,null,1);
        MemberDb = memberDbOpenHelper.getWritableDatabase();
        //Create and/or open a database that will be used for reading and writing.


        mEdtAccount =(EditText) findViewById(R.id.edtAccount);
        mEdtPassword =(EditText) findViewById(R.id.edtPassword00);
        mEdtIdentity =(EditText) findViewById(R.id.edtIdentity);
        mEdtEmail =(EditText) findViewById(R.id.edtEmail);

        btnRegistSure = (Button) findViewById(R.id.btnRegistSure);
        btnRegistSure.setOnClickListener(btnRegistSureOnClick);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemberDb.close();
    }

    private View.OnClickListener btnRegistSureOnClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override

        public void onClick(View v) {
            ContentValues newRow = new ContentValues();
            newRow.put("mb_account", mEdtAccount.getText().toString());
            newRow.put("mb_password", mEdtPassword.getText().toString());
            newRow.put("mb_Identity", mEdtIdentity.getText().toString());
            newRow.put("mb_email", mEdtEmail.getText().toString());
            newRow.put("mb_point", String.valueOf(shoppoint));
            newRow.put("mb_registdate", new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) );
            MemberDb.insert(DB_TABLE, null,newRow);

            RegistDialog mtAltDlg = new RegistDialog(RegistActivity.this);
            mtAltDlg.setTitle("註冊成功");
            mtAltDlg.setButton(DialogInterface.BUTTON_POSITIVE,"是",finish);
            mtAltDlg.show();
        }
    };
    private DialogInterface.OnClickListener finish = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    };


}

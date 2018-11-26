package com.example.jap96.myshopdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {
    private SQLiteDatabase MemberDb;
    private static final String DB_FILE = "Members.db",
            DB_TABLE2 ="history",
            DB_TABLE = "members";

    private Button mBtnLogIn, mBtnRegist, mBtnForPassword;

    private EditText mEdtAccount,
            mEdtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdtAccount = (EditText) this.findViewById(R.id.edtLoginAccount);
        mEdtPassword = (EditText) this.findViewById(R.id.edtLoginPassword);

        mBtnRegist = (Button) this.findViewById(R.id.btnRegist);
        mBtnLogIn = (Button) this.findViewById(R.id.btnLogIn);
        mBtnForPassword = (Button) this.findViewById(R.id.btnForPassword);

        mBtnLogIn.setOnClickListener(btnLogInOnClick);
        mBtnRegist.setOnClickListener(btnRegistOnClick);
        mBtnForPassword.setOnClickListener(btnForPasswordOnClick);

        MemberDbOpenHelper memberDbOpenHelper = new MemberDbOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        MemberDb = memberDbOpenHelper.getWritableDatabase();
        Cursor cursor1 = MemberDb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                DB_TABLE + "'", null);
        Cursor cursor2 = MemberDb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                DB_TABLE2 + "'", null);
        if (cursor1 != null) {
            if (cursor1.getCount() == 0) { //haven't table in db
                MemberDb.execSQL(" CREATE TABLE " + DB_TABLE + " (" +
                        "member_id INTEGER PRIMARY KEY," +
                        "mb_account TEXT NOT NULL," +
                        "mb_password TEXT NOT NULL," +
                        "mb_Identity VARCHAR2(10) NOT NULL," +
                        "mb_email TEXT NOT NULL," +
                        "mb_point NUMBER NOT NULL," +
                        "mb_registdate datetime );");
            }

            }cursor1.close();
        if (cursor2 != null) {
            if (cursor2.getCount() == 0) {
                MemberDb.execSQL(" CREATE TABLE " + DB_TABLE2 + " (" +
                        "his_id INTEGER PRIMARY KEY," +
                        "his_memberid TEXT NOT NULL," +
                        "his_date datetime NOT NULL," +
                        "his_thecost NUMBER NOT NULL," +
                        "his_usepoint NUMBER NOT NULL," +
                        "his_finalcost NUMBER NOT NULL," +
                        "his_pointchange NUMBER NOT NULL,"+
                        "his_nowpoint NUMBER NOT NULL );");
            }

        }cursor2.close();
    }



    private View.OnClickListener btnLogInOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Account = mEdtAccount.getText().toString();
            String Password = mEdtPassword.getText().toString();

            //Create and/or open a database that will be used for reading and writing.
            Cursor account = MemberDb.rawQuery("select * from " +
                    DB_TABLE + " where mb_account ='" +
                    Account + "'", null);
            Cursor password = MemberDb.rawQuery("select * from " +
                    DB_TABLE + " where mb_account ='" +
                    Account + "'" + "and mb_password='" +
                    Password + "'", null);

            if (account.getCount() != 0) {          //have accout
                if (password.getCount() != 0) {         //password true
                    Intent it = new Intent();
                    it.setClass(LoginActivity.this, MainActivity.class);

                    Bundle bundle = new Bundle();      //bundle the user's information
                    String main_account = Account;
                    String main_password = Password;
                    bundle.putString("main_account", main_account);
                    bundle.putString("main_password", main_password);
                    it.putExtras(bundle);

                    startActivity(it);
                } else {    //password err
                    Toast toast = Toast.makeText(LoginActivity.this, R.string.Passworderr, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mEdtPassword.setText("");
                }

            } else if (account.getCount() == 0) {//無帳號
                Toast toast = Toast.makeText(LoginActivity.this, R.string.Accounterr, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                mEdtAccount.setText("");
                mEdtPassword.setText("");
            }
            account.close();
            password.close();
        }
    };

    private View.OnClickListener btnRegistOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(LoginActivity.this, RegistActivity.class);
            startActivity(it);

        }
    };
    private View.OnClickListener btnForPasswordOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(LoginActivity.this, ForPassword.class);
            startActivity(it);
        }
    };


    @Override
    protected void onResume() {
        SharedPreferences SP = getSharedPreferences("playE",MODE_PRIVATE);
        SP.edit().putInt("playE", 1).commit();  //set the game's pic for new one
        super.onResume();
    }


}


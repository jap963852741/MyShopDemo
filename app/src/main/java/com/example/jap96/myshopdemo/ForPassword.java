package com.example.jap96.myshopdemo;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForPassword extends AppCompatActivity {
    private SQLiteDatabase MemberDb;
    private static final String DB_FILE = "Members.db",
            DB_TABLE = "members";
    private EditText mEdtForPassAccount,
            mEdtForPassIdentity;
    private Button mBtnForPassSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_password);

        mEdtForPassAccount = (EditText) findViewById(R.id.edtForPassAccount);
        mEdtForPassIdentity = (EditText) findViewById(R.id.edtForPassIdentity);
        mBtnForPassSure = (Button) findViewById(R.id.btnForPassSure);
        mBtnForPassSure.setOnClickListener(btnForPasswordOnClick);
    }

    private View.OnClickListener btnForPasswordOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Account = mEdtForPassAccount.getText().toString();
            String Identity = mEdtForPassIdentity.getText().toString();

            MemberDbOpenHelper friendDbOpenHelper = new MemberDbOpenHelper(getApplicationContext(), DB_FILE, null, 1);
            MemberDb = friendDbOpenHelper.getWritableDatabase();

            Cursor password = MemberDb.rawQuery("select mb_password from " +
                    DB_TABLE + " where mb_account ='" +
                    Account + "'" + "and mb_Identity='" +
                    Identity + "'" , null);

            if (password.getCount() != 0) {//有帳號
                password.moveToFirst();
                RegistDialog mtAltDlg = new RegistDialog(ForPassword.this);
                mtAltDlg.setTitle("您的密碼是");
                mtAltDlg.setMessage(password.getString(0));
                password.close();
                mtAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "確認", aa);
                mtAltDlg.show();

            } else {
                Toast toast = Toast.makeText(ForPassword.this, R.string.Informationerr, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                password.close();
            }

        }


        private DialogInterface.OnClickListener aa = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };


    };
}


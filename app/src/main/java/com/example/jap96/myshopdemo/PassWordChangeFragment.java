package com.example.jap96.myshopdemo;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class PassWordChangeFragment extends Fragment {
    private SQLiteDatabase MemberDb;
    private static final String DB_FILE = "Members.db",
            DB_TABLE = "members";
    private Button mBtnChangePassword;
    private EditText mEdtOldPassword,
            mEdtNewPassword00,mEdtNewPassword01;
    public PassWordChangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_pass_word_change, container, false);
        // Inflate the layout for this fragment
        MemberDbOpenHelper memberDbOpenHelper = new MemberDbOpenHelper(getActivity(), DB_FILE, null, 1);
        MemberDb = memberDbOpenHelper.getWritableDatabase();
        mEdtOldPassword =(EditText) view.findViewById(R.id.edtOldPassWord);
        mEdtNewPassword00 =(EditText) view.findViewById(R.id.edtChangePassWord00);
        mEdtNewPassword01 =(EditText) view.findViewById(R.id.edtChangePassWord01);

        mBtnChangePassword = (Button) view.findViewById(R.id.btnChangePassWord);
        mBtnChangePassword.setOnClickListener(btnChangeOnClick);

        return view;
    }



    private View.OnClickListener btnChangeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            OldPassWordSure();

        }
    };

    private void OldPassWordSure() {  // 1 --> true 0-->false
        Bundle bundle = getActivity().getIntent().getExtras();
        String main_account = bundle.getString("main_account");
        String main_password = bundle.getString("main_password");
        Cursor passwordcur = MemberDb.rawQuery("select * from " +
                DB_TABLE + " where mb_account = '"+ main_account +"'"+
                " and mb_password='"+ main_password +"'", null);
        passwordcur.moveToFirst();
        String password = passwordcur.getString(2);
        Log.d("密碼",password);
        String OldPassWord = mEdtOldPassword.getText().toString();
        Log.d("密碼",OldPassWord);
        if(OldPassWord.equals(password) ){
            NewPassWordCheck();
        }else {
            Toast toast = Toast.makeText(getActivity(), R.string.Passworderr, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            mEdtOldPassword.setText("");

        }
        passwordcur.close();
    }

    private void NewPassWordCheck() {
        String NewPassWord00 = mEdtNewPassword00.getText().toString();
        String NewPassWord01 = mEdtNewPassword01.getText().toString();

        if(NewPassWord00.equals(NewPassWord01) ){
            ChangePassWord();
        }else {
            Toast toast = Toast.makeText(getActivity(), R.string.NewPassworderr, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            mEdtNewPassword00.setText("");
            mEdtNewPassword01.setText("");

        }


    }
    private void ChangePassWord() {
        String OldPassWord = mEdtOldPassword.getText().toString();
        String NewPassWord00 = mEdtNewPassword00.getText().toString();

        Bundle bundle = getActivity().getIntent().getExtras();
        String main_account = bundle.getString("main_account");
        String main_password = bundle.getString("main_password");
        Cursor passwordcur = MemberDb.rawQuery("select * from " +
                DB_TABLE + " where mb_account = '"+ main_account +"'"+
                " and mb_password='"+ main_password +"'", null);
        passwordcur.moveToFirst();
        int id = passwordcur.getInt(0);
        if (OldPassWord != null && NewPassWord00 != null) {
            Log.d("有沒有近來","有");
            ContentValues newRow = new ContentValues();
            passwordcur.moveToFirst();
            newRow.put("mb_account", passwordcur.getString(1));
            newRow.put("mb_password", NewPassWord00);
            newRow.put("mb_Identity", passwordcur.getString(3));
            newRow.put("mb_email", passwordcur.getString(4));
            newRow.put("mb_point", passwordcur.getString(5));
            newRow.put("mb_registdate", passwordcur.getString(6));
            MemberDb.update(DB_TABLE,newRow,"member_id" +"="+id  , null);

            RegistDialog mtAltDlg = new RegistDialog(getActivity());
            mtAltDlg.setTitle("密碼已更改");
            mtAltDlg.setButton(DialogInterface.BUTTON_POSITIVE,"確認",aa);
            mtAltDlg.show();
        }
        passwordcur.close();

    }
    private DialogInterface.OnClickListener aa = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mEdtOldPassword.setText("");
            mEdtNewPassword00.setText("");
            mEdtNewPassword01.setText("");
            Intent it = new Intent();
            it.setClass(getContext(), LoginActivity.class);
            startActivity(it);

        }
    };
}

package com.example.jap96.myshopdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private SQLiteDatabase MemberDb;
    private static final String DB_FILE = "Members.db",
            DB_TABLE = "members";
    private TextView mEdtForPassAccount;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        MemberDbOpenHelper memberDbOpenHelper = new MemberDbOpenHelper(getActivity(), DB_FILE, null, 1);
        MemberDb = memberDbOpenHelper.getWritableDatabase();
        mEdtForPassAccount = (TextView) view.findViewById(R.id.PassAccount);

        // Inflate the layout for this fragment
        showResult();

        return  view;

    }

    private void showResult() {
        Bundle bundle = getActivity().getIntent().getExtras();
        String main_account = bundle.getString("main_account");
        String main_password = bundle.getString("main_password");
        Cursor password = MemberDb.rawQuery("select * from " +
                DB_TABLE + " where mb_account = '"+ main_account +"'"+
                " and mb_password='"+ main_password +"'", null);

        if (password != null) {
            if (password.getCount() != 0) {
                password.moveToFirst();
                mEdtForPassAccount.setText( "\n"+"會員編號:" + password.getString(0) + "\n"
                        + "申請日期:" + password.getString(6) + "\n"
                        +"\n"+ "目前點數:" + password.getString(5) + "\n");
            }

        }password.close();


    }
}

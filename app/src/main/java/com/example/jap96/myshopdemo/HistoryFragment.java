package com.example.jap96.myshopdemo;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private SQLiteDatabase MemberDb;
    private static final String DB_FILE = "Members.db",
            DB_TABLE2 ="history",
            DB_TABLE = "members";

    private TextView history;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_history, container, false);

        history = (TextView) view.findViewById(R.id.abc);

        MemberDbOpenHelper memberDbOpenHelper = new MemberDbOpenHelper(getActivity(), DB_FILE, null, 1);
        MemberDb = memberDbOpenHelper.getWritableDatabase();

        Bundle bundle = getActivity().getIntent().getExtras();
        String main_account = bundle.getString("main_account");
        String main_password = bundle.getString("main_password");
        Cursor password = MemberDb.rawQuery("select * from " +
                DB_TABLE + " where mb_account = '"+ main_account +"'"+
                " and mb_password='"+ main_password +"'", null);
        password.moveToFirst();
        String id = password.getString(0);
        password.close();

        Cursor account = MemberDb.rawQuery("select * from " +
                DB_TABLE2 + " where his_memberid ='" +
                id + "'", null);

        if (account != null) {
            if (account.getCount() != 0) {
                history.setText("         "+"購買日期"+"           "+
                        "金額"+"   "+"用點"+" "+
                        "扣點"+" "+"增點"+" "+
                        "目前點數" +"\n");
                account.moveToLast();
                history.append(account.getString(2)+" "+
                        " $ "+account.getString(3)+"    "+account.getString(4)+"   "+
                        account.getString(5)+"   "+account.getString(6)+"          "+
                        account.getString(7)+"\n");
                while (account.moveToPrevious()){
                    history.append(account.getString(2)+" "+
                            " $ "+account.getString(3)+"    "+account.getString(4)+"   "+
                            account.getString(5)+"   "+account.getString(6)+"          "+
                            account.getString(7)+"\n");
                }
            }

        }account.close();


        return view;
    }

}

package com.example.jap96.myshopdemo;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {


    public ShopFragment() {
        // Required empty public constructor
    }
    private EditText MemberNum,Thecost,UsePoint;
    private TextView Nowpoint,FinalCost;
    private Button Nowpointsure,Checkcost,Changetopoint;
    private SQLiteDatabase MemberDb;
    private static final String DB_FILE = "Members.db",
            DB_TABLE2 ="history",
            DB_TABLE = "members";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        MemberDbOpenHelper friendDbOpenHelper = new MemberDbOpenHelper(getActivity(), DB_FILE, null, 1);
        MemberDb = friendDbOpenHelper.getWritableDatabase();
        MemberNum = (EditText) view.findViewById(R.id.edtMemberNum);
        Nowpoint = (TextView) view.findViewById(R.id.txtNowpoint);
        Nowpointsure = (Button) view.findViewById(R.id.pointsure);

        Thecost = (EditText) view.findViewById(R.id.thecost);
        UsePoint = (EditText) view.findViewById(R.id.theuseofpoint);
        Checkcost = (Button) view.findViewById(R.id.checkcost);
        FinalCost = (TextView) view.findViewById(R.id.finalcost);

        Changetopoint = (Button) view.findViewById(R.id.changetopoint);

        Nowpointsure.setOnClickListener(btnpointsureOnClick);
        Checkcost.setOnClickListener(btncheckcostOnClick);
        Changetopoint.setOnClickListener(btnchangetopointOnClick);
        return view;
    }

    private void pointsure(){
        String id = MemberNum.getText().toString();
        //Create and/or open a database that will be used for reading and writing.
        Cursor account = MemberDb.rawQuery("select * from " +
                DB_TABLE + " where member_id ='" +
                id + "'", null);
        account.moveToFirst();
        if (account.getCount() != 0) {  //有此編號
            Nowpoint.setText(account.getString(5));
        }else {
            Nowpoint.setText("資料錯誤");
        }
        account.close();
    }

    private void checkcost(){
        try{
            int thecost = Integer.parseInt(Thecost.getText().toString());
            int thepoint = Integer.parseInt(UsePoint.getText().toString());
            int total = thecost - thepoint ;
            FinalCost.setText(String.valueOf(total));
        } catch (NumberFormatException e){
            Toast toast = Toast.makeText(getActivity(), R.string.Informationerr, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Thecost.setText("");
            UsePoint.setText("");
        }
    }

    private void changetopoint(){
        String id = MemberNum.getText().toString();
        int a = Integer.parseInt(FinalCost.getText().toString());
        int subpoint = Integer.parseInt(UsePoint.getText().toString());
        int changepoint = (int) (a * 0.1 +0.5) - subpoint;  // +point

        Cursor account = MemberDb.rawQuery("select * from " +
                DB_TABLE + " where member_id ='" +
                id + "'", null);
        account.moveToFirst();
        int nowpoint = Integer.parseInt(account.getString(5));
        int finalpoint = nowpoint + changepoint;
        if (account.getCount() != 0) {  //有此編號
            ContentValues member = new ContentValues();
            ContentValues history = new ContentValues();
            account.moveToFirst();
            member.put("mb_account", account.getString(1));
            member.put("mb_password", account.getString(2));
            member.put("mb_Identity", account.getString(3));
            member.put("mb_email", account.getString(4));
            member.put("mb_point", finalpoint);
            member.put("mb_registdate", account.getString(6));
            history.put("his_memberid",account.getString(0));
            history.put("his_date",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date()));
            history.put("his_thecost",Thecost.getText().toString());
            history.put("his_usepoint",UsePoint.getText().toString());
            history.put("his_finalcost",FinalCost.getText().toString());
            history.put("his_pointchange",changepoint);
            history.put("his_nowpoint",finalpoint);
            MemberDb.update(DB_TABLE,member,"member_id" +"="+id  , null);
            MemberDb.insert(DB_TABLE2,null  , history);
        }
        account.close();
        pointsure();
        Thecost.setText("");
        UsePoint.setText("");
        FinalCost.setText("");
    }


    private View.OnClickListener btnpointsureOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pointsure();
        }
    };
    private View.OnClickListener btncheckcostOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            checkcost();
        }
    };
    private View.OnClickListener btnchangetopointOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changetopoint();
        }
    };

}

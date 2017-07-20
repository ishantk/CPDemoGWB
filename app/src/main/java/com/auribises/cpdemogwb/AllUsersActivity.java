package com.auribises.cpdemogwb;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @InjectView(R.id.listView)
    ListView listView;

    ContentResolver resolver;

    ArrayList<User> userList;
    UserAdapter adapter;

    User user;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        ButterKnife.inject(this);

        resolver = getContentResolver();

        retrieveUsers();
    }


    void retrieveUsers(){

        userList = new ArrayList<>();

        String[] projection = {Util.COL_ID,Util.COL_NAME,Util.COL_EMAIL,Util.COL_PASSWORD,Util.COL_GENDER,Util.COL_CITY};

        Cursor cursor = resolver.query(Util.USER_URI,projection,null,null,null);

        if(cursor != null){
            int i=0;
            String n="",e="",p="",g="",c="";

            while (cursor.moveToNext()){
                i = cursor.getInt(cursor.getColumnIndex(Util.COL_ID));
                n = cursor.getString(cursor.getColumnIndex(Util.COL_NAME));
                e = cursor.getString(cursor.getColumnIndex(Util.COL_EMAIL));
                p = cursor.getString(cursor.getColumnIndex(Util.COL_PASSWORD));
                g = cursor.getString(cursor.getColumnIndex(Util.COL_GENDER));
                c = cursor.getString(cursor.getColumnIndex(Util.COL_CITY));

                //User user = new User(i,n,e,p,g,c);
                //userList.add(user);

                userList.add(new User(i,n,e,p,g,c));
                // userList is representing the entire table
            }

            adapter = new UserAdapter(this,R.layout.list_item,userList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }

    }

    void showUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(user.getName());
        builder.setMessage(user.toString());
        builder.setPositiveButton("Done",null);
        builder.create().show();
    }


    void askForDeletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+user.getName());
        builder.setMessage("Are you Sure to delete the record ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUser();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }

    void deleteUser(){

        String where = Util.COL_ID+" = "+user.getId();
        int i = resolver.delete(Util.USER_URI,where,null);
        if(i>0){
            Toast.makeText(this,user.getName()+ " deleted from DB "+i,Toast.LENGTH_LONG).show();
            userList.remove(pos);
            adapter.notifyDataSetChanged();

        }


    }

    void showOptions(){
        String[] items = {"View User","Delete User","Update User"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        showUser();
                        break;

                    case 1:
                        askForDeletion();
                        break;

                    case 2:
                        Intent intent = new Intent(AllUsersActivity.this,SignUpActivity.class);
                        intent.putExtra("keyUser",user);
                        startActivity(intent);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pos = i;
        user = userList.get(i);
        showOptions();
    }
}

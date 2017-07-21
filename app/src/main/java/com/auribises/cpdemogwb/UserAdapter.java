package com.auribises.cpdemogwb;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishantkumar on 20/07/17.
 */

public class UserAdapter extends ArrayAdapter<User> {

    Context context;
    int resource;
    ArrayList<User> userList,tempList;

    public UserAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        userList = objects;

        tempList = new ArrayList<>();
        tempList.addAll(userList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        view = LayoutInflater.from(context).inflate(resource,parent,false);

        TextView txtName = (TextView)view.findViewById(R.id.textViewName);
        TextView txtEmail = (TextView)view.findViewById(R.id.textViewEmail);

        User user = userList.get(position);
        txtName.setText(user.getId()+" "+user.getName());
        txtEmail.setText(user.getEmail());

        return view;
    }

    public void filter(String str){

        userList.clear();

        if(str.length()==0){
            userList.addAll(tempList);
        }else{
//            for(int i=0;i<tempList.size();i++){
//
//            }

            for(User user : tempList){
                if(user.getName().toLowerCase().contains(str.toLowerCase())){
                    userList.add(user);
                }
            }
        }

        notifyDataSetChanged();
    }
}

package com.example.phrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private ImageView add_member;
    private ListView listView;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> listItem;
    ArrayList<String> keys;
    DocumentReference user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listView = getView().findViewById(R.id.listviewuser);
        listItem =new ArrayList();
        user= db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        keys=new ArrayList();

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());
                        UserProfile u= document.toObject(UserProfile.class);
                        user.collection("familyMembers").orderBy("timestamp").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("tag", document.getId() + " => " + document.getData());
                                                FamilyMember f = document.toObject(FamilyMember.class);
                                                keys.add(document.getId());

                                                listItem.add(f.getFirst_name()+" "+f.getLast_name());
                                                Log.d("list item Size",Integer.toString(listItem.size()));
                                            }
                                            if(!listItem.isEmpty()){
                                                //final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1 ,listItem);
                                                CustomListAdapter adapter = new CustomListAdapter();
                                                listView.setAdapter(adapter);
                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                        // TODO Auto-generated method stub
                                                        //String value = adapter.getItem(position);
                                                        String key = keys.get(position);
                                                        Toast.makeText(getContext(),key,Toast.LENGTH_SHORT).show();

                                                       Intent i = new Intent(getContext(),UserHome.class);
                                                       i.putExtra("key",key);
                                                       startActivity(i);
                                                    }
                                                });
                                            }


                                        } else {
                                            Log.d("tag", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                    } else {
                        Log.d("tag", "No such document");
                    }
                } else {
                    Log.d("tag", "get failed with ", task.getException());
                }
            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add_member = getView().findViewById(R.id.add_members);

        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),Add_Member.class);
                startActivity(i);
            }
        });

    }

    class CustomListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listItem.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            View view1 = getLayoutInflater().inflate(R.layout.list,null);
            final CircleImageView imageView = view1.findViewById(R.id.user_img);
            TextView textView = view1.findViewById(R.id.user_name);
            CollectionReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("familyMembers");
            f.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int j =0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("tag", document.getId() + " => " + document.getData());
                        if(j==i) {
                            //break;
                            //}
                            user.collection("familyMembers").document(document.getId())
                                    .collection("PersonalInfo").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            int k = 0;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("tag", document.getId() + " => " + document.getData());
                                                PersonalInfo info = document.toObject(PersonalInfo.class);
                                                //if(k==i) {
                                                Picasso.get().load(info.getImageUrl()).into(imageView);
                                                //}
                                                //k++;
                                            }
                                        }
                                    });
                        }
                        j++;
                    }
                }
            });
            textView.setText(listItem.get(i));
            return view1;

        }
    }
}

class  FamilyMember{
    private String first_name;
    private String last_name;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public  FamilyMember(){
    }
    public FamilyMember(String first_name,String last_name){
        this.first_name=first_name;
        this.last_name=last_name;

    }
}

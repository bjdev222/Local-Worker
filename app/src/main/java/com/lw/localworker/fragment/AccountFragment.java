package com.lw.localworker.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.lw.localworker.HelpActivity;
import com.lw.localworker.R;
import com.lw.localworker.model.UModel;
import com.lw.localworker.UProfileActivity;
import com.lw.localworker.WLoginActivity;
import com.lw.localworker.WProfileActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    LinearLayout ep,bw,help;
    Button login,logout;
    View view;
    TextView name,verify;




    FirebaseUser user ;
    String phoneNumber;
    final String[] url2 = {" "};
    Uri imageUri,mCropImageUri;
    StorageReference storageReference;
    CircleImageView profileImage;
    String profileUrl;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_account, container, false);
        initUI();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean Islogin = prefs.getBoolean("Islogin", false);
        if(Islogin) {

            user= FirebaseAuth.getInstance().getCurrentUser();
            phoneNumber = user.getPhoneNumber();
            login.setVisibility(View.GONE);
            ep.setVisibility(View.VISIBLE);
            bw.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);

            setData();




        }


        logout.setOnClickListener(this);
        login.setOnClickListener(this);
        bw.setOnClickListener(this);
        ep.setOnClickListener(this);
        help.setOnClickListener(this);

        return view;
    }

    private void setData() {

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserDetails").child(phoneNumber);

        final ProgressDialog progressDialog
                = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    UModel wModel = snapshot.getValue(UModel.class);

                    name.setText(wModel.getuName());


                    profileUrl=(wModel.getuProfileUrl()).replaceAll("^\"|\"$", "");
                    verify.setText("Profile is verified..");
                    verify.setTextColor(R.color.green);

                    Picasso.get().load(profileUrl).into(profileImage);

                    progressDialog.dismiss();


                }
                else {
                    progressDialog.dismiss();
                    name.setText(phoneNumber.substring(0,5)+"******"+phoneNumber.substring(11,13));
                    verify.setText("Profile is not verified..");
                    verify.setTextColor(R.color.red);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        login=view.findViewById(R.id.login);
        logout=view.findViewById(R.id.logout);
        ep=view.findViewById(R.id.ep);
        bw=view.findViewById(R.id.bw);
        name=view.findViewById(R.id.name);
        profileImage=view.findViewById(R.id.profileImage);
        verify=view.findViewById(R.id.verify);

        help=view.findViewById(R.id.help);

    }

    @Override
    public void onClick(View v) {
        if(v==logout)
        {
            logout();

        }
        if(v==login)

        {
            Intent intent = new Intent(getActivity(), WLoginActivity.class);
            startActivity(intent);
        }

        if(v==bw)
        {
            Intent intent = new Intent(getActivity(), WProfileActivity.class);
            startActivity(intent);

        }

        if(v==ep)
        {
            Intent intent = new Intent(getActivity(), UProfileActivity.class);
            startActivity(intent);

        }

        if(v==help)
        {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);

        }


    }

    private void logout() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.edit().putBoolean("Islogin", false).commit();

        //reloading fragment
        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }


}
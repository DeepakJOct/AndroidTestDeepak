package app.com.testapp.View.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.com.testapp.Controller.DataRepository;
import app.com.testapp.Listeners.ResultListener;
import app.com.testapp.Model.adapters.CommentsAdapter;
import app.com.testapp.MyApplication;
import app.com.testapp.R;
import app.com.testapp.Room.Controller.DatabaseClient;
import app.com.testapp.Room.Controller.LocalRepository;
import app.com.testapp.Room.models.MemberInfo;
import app.com.testapp.Sql.DatabaseHelper;
import app.com.testapp.Sql.SqlOperations;
import app.com.testapp.View.activities.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnJsonFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JsonDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JsonDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @BindView(R.id.btn_get_response)
    Button btnGetResponse;

    @BindView(R.id.rcv)
    RecyclerView rcv;

    @BindView(R.id.ed_search)
    EditText edSearch;

    private OnJsonFragmentInteractionListener mListener;
    private int insertCount = 0;
    List<MemberInfo> memberInfoList;
    CommentsAdapter commentsAdapter;

    public JsonDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JsonDataFragment newInstance() {
        JsonDataFragment fragment = new JsonDataFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jsondata, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        memberInfoList = SqlOperations.getDataFromSql(getContext());
        if(memberInfoList != null && memberInfoList.size() > 0) {
            edSearch.setVisibility(View.VISIBLE);
            commentsAdapter = new CommentsAdapter(getContext(), memberInfoList);
            rcv.setLayoutManager(new LinearLayoutManager(getContext()));
            rcv.setAdapter(commentsAdapter);
        } else {
            edSearch.setVisibility(View.GONE);
        }

        btnGetResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqlOperations.insertServerData(getContext());
                memberInfoList = SqlOperations.getDataFromSql(getContext());
                Log.d("sqlList->", memberInfoList.size() + "");
                if(memberInfoList != null) {
                    commentsAdapter = new CommentsAdapter(getContext(), memberInfoList);
                    rcv.setLayoutManager(new LinearLayoutManager(getContext()));
                    rcv.setAdapter(commentsAdapter);
                    edSearch.setVisibility(View.VISIBLE);
                }
            }

        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }

    void filter(String text){
        List<MemberInfo> temp = new ArrayList();
        for(MemberInfo m: memberInfoList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(m.getEmail().contains(text)) {
                temp.add(m);
            } else if(m.getName().contains(text)) {
                temp.add(m);
            } else if(m.getBody().contains(text)) {
                temp.add(m);
            }
        }
        //update recyclerview
        commentsAdapter.updateList(temp);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onJsonFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJsonFragmentInteractionListener) {
            mListener = (OnJsonFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnJsonFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        DatabaseClient.getInstance(getContext()).getAppDatabase().close();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnJsonFragmentInteractionListener {
        // TODO: Update argument type and name
        void onJsonFragmentInteraction(Uri uri);
    }
}

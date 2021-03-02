package com.example.fetch_rewards_coding_exercise;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(new Runnable() {
            @Override
            public void run() {
                HttpRequest request = new HttpRequest();
                String url = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
                String json = request.makeServiceCall(url);
                List<Data> fetchRewardsData = new ArrayList<>();

                if(json != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("name") == "null" || jsonObject.getString("name").isEmpty()) {
                                continue;
                            }
                            else {
                                int id = Integer.parseInt(jsonObject.getString("id"));
                                int listId = Integer.parseInt(jsonObject.getString("listId"));
                                String name = jsonObject.getString("name");
                                Data obj = new Data(id, listId, name);
                                fetchRewardsData.add(obj);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(fetchRewardsData, new Comparator<Data>() {
                            @Override
                            public int compare(Data o1, Data o2) {
                                int difference = o1.getListId() - o2.getListId();
                                if(difference == 0) {
//                                    return o1.getName().compareTo(o2.getName());
                                    return o1.getName().getNum() - o2.getName().getNum();
                                }
                                else {
                                    return difference;
                                }

                            }
                        });
                        updateUI(fetchRewardsData);
                    }
                });
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(divider);

        return view;
    }


    public void updateUI(List<Data> data) {
        if(mAdapter == null) {
            mAdapter = new DataAdapter(data);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private class DataHolder extends RecyclerView.ViewHolder {

        private Data mData;
        private TextView mListIdTextView;
        private TextView mNameTextView;

        public DataHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_data, parent, false));
            mListIdTextView = (TextView) itemView.findViewById(R.id.listId);
            mNameTextView = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(Data data) {
            mData = data;
            mListIdTextView.setText(Integer.toString(mData.getListId()));
            mNameTextView.setText(mData.getName().toString());
        }
    }

    private class DataAdapter extends RecyclerView.Adapter<DataHolder> {

        private List<Data> mData;
        public DataAdapter(List<Data> data) {
            mData = data;
        }
        @NonNull
        @Override
        public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DataHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DataHolder holder, int position) {
            Data data = mData.get(position);
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

    }

}

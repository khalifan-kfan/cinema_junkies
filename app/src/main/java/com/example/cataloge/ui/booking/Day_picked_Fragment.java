package com.example.cataloge.ui.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cataloge.R;
import com.example.cataloge.ui.booking.data.Days;

import java.util.List;

public class Day_picked_Fragment extends Fragment implements goClick {

    private SharedViewmodel viewmodel;
    private  DayRecyclerViewAdapter adapter;
    private  NavController move;

    public Day_picked_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        Context context = view.getContext();
        move = Navigation.findNavController(getActivity(),R.id.fragment);

        adapter = new DayRecyclerViewAdapter(this);
        // Set the adapter
            RecyclerView recyclerView =  view.findViewById(R.id.list);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       viewmodel=  new ViewModelProvider(requireActivity()).get(SharedViewmodel.class);

        viewmodel.getDays(viewmodel.getMovieID().getValue()).observe(requireActivity(), new Observer<List<Days>>() {
            @Override
            public void onChanged(List<Days> days) {
               // Toast.makeText(getContext(),viewmodel.getMovieID().getValue(),Toast.LENGTH_SHORT).show();
                adapter.setmValues(days);
            }
        });

    }

    @Override
    public void onNext(Days days, int price) {
       //post day id
        viewmodel.setPrice(price);
       viewmodel.setDay_id(days);
       move.navigate(R.id.action_navigation_days_to_navigation_cine_times);
    }


}
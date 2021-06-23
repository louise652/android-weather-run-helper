package com.android.runweather.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.runweather.R;
import com.android.runweather.interfaces.StartDragListener;
import com.android.runweather.utils.ItemMoveCallback;

import java.util.ArrayList;

/**
 * Adapter to display the weather condition preferences which the user can drag to reorder
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    public final ArrayList<String> data;

    private final StartDragListener mStartDragListener;

    public RecyclerViewAdapter(ArrayList<String> data, StartDragListener startDragListener) {
        mStartDragListener = startDragListener;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTitle.setText(data.get(position));

        holder.imageView.setOnTouchListener((v, event) -> {
            {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        data.add(toPosition, data.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.parseColor("#FEDBD0"));

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final View rowView;
        private final ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.weatherPrefItemTV);
            imageView = itemView.findViewById(R.id.dragIcon);
        }
    }
}
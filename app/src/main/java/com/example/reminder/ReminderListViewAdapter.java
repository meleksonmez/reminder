package com.example.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderListViewAdapter extends RecyclerView.Adapter<ReminderListViewAdapter.MyViewHolder> {

    ArrayList<ReminderNotes> mReminderNotes;
    LayoutInflater inflater;
    DBHelper dbHelper;

    public ReminderListViewAdapter(Context context, ArrayList<ReminderNotes> reminderNotes) {
        inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
        this.mReminderNotes = reminderNotes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listlayout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReminderNotes selectedReminder = (ReminderNotes) mReminderNotes.get(position);
        holder.setData(selectedReminder, position);
    }

    @Override
    public int getItemCount() {
        return mReminderNotes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EditText reminderTitle, reminderDetail, reminderTime, categoryTextView;
        Button delete, update;
        CheckBox isDone;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

            reminderTitle = (EditText) itemView.findViewById(R.id.reminderTitle_editText);
            reminderDetail = (EditText) itemView.findViewById(R.id.noteDetail_editText);
            reminderTime = (EditText) itemView.findViewById(R.id.time_editText);

            delete = (Button) itemView.findViewById(R.id.delete_button);
            update = (Button) itemView.findViewById(R.id.update_button);

            categoryTextView = (EditText) itemView.findViewById(R.id.category_editText);
            isDone = (CheckBox) itemView.findViewById(R.id.done_checkBox);

            delete.setOnClickListener(this);
            update.setOnClickListener(this);
            isDone.setOnClickListener(this);
        }

        public void setData(ReminderNotes selectedReminder, int position) {
            this.cardView.setCardBackgroundColor(NoteCategory.getColour(selectedReminder.getReminderCategory()));
            this.reminderTitle.setText(selectedReminder.getReminderTitle());
            this.reminderDetail.setText(selectedReminder.getReminderDetail());
            this.categoryTextView.setText(selectedReminder.getReminderCategory());
            this.reminderTime.setText(ReminderNotes.convertGregorianToDate(selectedReminder.getReminderTime()));
            this.isDone.setChecked(selectedReminder.isChecked());
        }

        @Override
        public void onClick(View v) {
            String message = "";

            if (v == delete) {
                message = deleteReminderNote(getLayoutPosition());
            }

            if (v == update) {
                message = updateReminderNote(getLayoutPosition());
            }

            if( v == isDone){
                message = setIsChecked(getLayoutPosition());
            }

            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
        }

        private String deleteReminderNote(int position) {
            dbHelper.deleteReminderNote(mReminderNotes.get(position));

            mReminderNotes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mReminderNotes.size());

            return "Not silindi.";
        }

        private String updateReminderNote(int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminderTime.getDrawingTime());

            mReminderNotes.get(position).setReminderTitle(this.reminderTitle.getText().toString());
            mReminderNotes.get(position).setReminderDetail(this.reminderDetail.getText().toString());
            mReminderNotes.get(position).setReminderCategory(this.categoryTextView.getText().toString());
            mReminderNotes.get(position).setReminderTime(calendar);

            dbHelper.updateReminderNote(mReminderNotes.get(position));

            setData(mReminderNotes.get(position), position);

            return "Not güncellendi.";
        }

        private String setIsChecked(int position) {
            mReminderNotes.get(position).setChecked(this.isDone.isChecked());

            dbHelper.isCheckedReminderNote(mReminderNotes.get(position));

            setData(mReminderNotes.get(position), position);

            return "Not güncellendi.";
        }
    }

}
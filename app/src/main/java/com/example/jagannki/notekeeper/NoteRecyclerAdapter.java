package com.example.jagannki.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{

    private final Context mContext;
    private final List<NoteInfo> mNoteInfo;
    private final LayoutInflater layoutInflater;

    public NoteRecyclerAdapter(Context mContext, List<NoteInfo> mNoteInfo) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.mNoteInfo = mNoteInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_note_list,parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteInfo note = mNoteInfo.get(position);
        holder.textCourse.setText(note.getCourse().getTitle());
        holder.textTitle.setText(note.getTitle());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mNoteInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView textCourse;
        public final TextView textTitle;
        public int position;
        public ViewHolder(View itemView) {
            super(itemView);
            textCourse = itemView.findViewById(R.id.text_course);
            textTitle = itemView.findViewById(R.id.text_title);
            this.position = position;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_Position,position);
                    mContext.startActivity(intent);
                }
            });

        }
    }





}

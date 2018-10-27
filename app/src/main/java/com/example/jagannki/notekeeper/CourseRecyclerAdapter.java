package com.example.jagannki.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{

    private final Context mContext;
    private final List<CourseInfo> mCourseInfo;
    private final LayoutInflater layoutInflater;

    public CourseRecyclerAdapter(Context mContext, List<CourseInfo> mNoteInfo) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.mCourseInfo = mNoteInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_note_list,parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseInfo course = mCourseInfo.get(position);
        holder.textCourse.setText(course.getTitle());

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mCourseInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView textCourse;

        public int position;
        public ViewHolder(View itemView) {
            super(itemView);
            textCourse = itemView.findViewById(R.id.text_course);

            this.position = position;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, mCourseInfo.get(position).getTitle(), Snackbar.LENGTH_LONG).show();
                }
            });

        }
    }





}

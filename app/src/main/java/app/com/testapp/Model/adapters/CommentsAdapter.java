package app.com.testapp.Model.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.com.testapp.Model.models.Comment;
import app.com.testapp.R;
import app.com.testapp.Room.models.MemberInfo;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private Context context;
    private List<MemberInfo> memberInfoList;

    public CommentsAdapter(Context context,
                           List<MemberInfo> memberInfoList) {
        this.context = context;
        this.memberInfoList = memberInfoList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(memberInfoList != null) {
            Log.d("CommentsListAdapter-->", memberInfoList.toArray() + "");
            final MemberInfo memberInfo = memberInfoList.get(position);
            Log.d("CommentOne-->", memberInfo.toString());
            holder.tvId.setText(memberInfo.getId() + "");
            holder.tvName.setText(memberInfo.getName());
            holder.tvEmail.setText(memberInfo.getEmail());
            holder.tvComment.setText(memberInfo.getBody());
        } else {
            Toast.makeText(context, "List is null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return memberInfoList.size();
    }

    public void updateList(List<MemberInfo> list) {
        this.memberInfoList = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView tvName;
        TextView tvEmail;
        TextView tvComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.id_email);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}

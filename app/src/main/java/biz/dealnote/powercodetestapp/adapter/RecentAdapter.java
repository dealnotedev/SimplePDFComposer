package biz.dealnote.powercodetestapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import biz.dealnote.powercodetestapp.R;
import biz.dealnote.powercodetestapp.model.PdfFile;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.Holder> {

    private List<PdfFile> files;
    private final ActionListener actionListener;

    public RecentAdapter(List<PdfFile> files, ActionListener actionListener) {
        this.files = files;
        this.actionListener = actionListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PdfFile file = files.get(position);
        holder.name.setText(file.getFile().getName());

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(holder.itemView.getContext().getApplicationContext());

        holder.size.setText(formatBytes(file.getFile().length()));
        holder.date.setText(dateFormat.format(file.getCreateDateTime()));
        holder.contentRoot.setOnClickListener(v -> actionListener.onRecentClick(file));
    }

    public interface ActionListener {
        void onRecentClick(PdfFile file);
    }

    private static String formatBytes(long bytes) {
        String retStr = "";

        // One binary gigabyte equals 1,073,741,824 bytes.
        if (bytes > 1073741824) {// Add GB
            long gbs = bytes / 1073741824;
            retStr += (Long.valueOf(gbs)).toString() + "GB ";
            bytes = bytes - (gbs * 1073741824);
        }

        // One MB - 1048576 bytes
        if (bytes > 1048576) {// Add GB
            long mbs = bytes / 1048576;
            retStr += (Long.valueOf(mbs)).toString() + "MB ";
            bytes = bytes - (mbs * 1048576);
        }

        if (bytes > 1024) {
            long kbs = bytes / 1024;
            retStr += (Long.valueOf(kbs)).toString() + "KB";
            bytes = bytes - (kbs * 1024);
        } else {
            retStr += (Long.valueOf(bytes)).toString() + " bytes";
        }

        return retStr;
    }

    public void setFiles(List<PdfFile> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {

        View contentRoot;
        TextView name;
        TextView size;
        TextView date;

        Holder(View itemView) {
            super(itemView);
            contentRoot = itemView.findViewById(R.id.content_root);
            name = itemView.findViewById(R.id.recent_name);
            size = itemView.findViewById(R.id.recent_size);
            date = itemView.findViewById(R.id.recent_date);
        }
    }
}
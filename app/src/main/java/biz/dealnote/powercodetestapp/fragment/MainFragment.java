package biz.dealnote.powercodetestapp.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.Collections;
import java.util.List;

import biz.dealnote.mvp.core.IPresenterFactory;
import biz.dealnote.mvp.ui.AbsPresenterFragment;
import biz.dealnote.powercodetestapp.R;
import biz.dealnote.powercodetestapp.activity.PdfCreateActivity;
import biz.dealnote.powercodetestapp.adapter.RecentAdapter;
import biz.dealnote.powercodetestapp.model.PdfFile;
import biz.dealnote.powercodetestapp.mvp.presenter.MainPresenter;
import biz.dealnote.powercodetestapp.mvp.view.IMainView;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class MainFragment extends AbsPresenterFragment<MainPresenter, IMainView>
        implements IMainView, RecentAdapter.ActionListener {

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecentAdapter recentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recentAdapter = new RecentAdapter(Collections.emptyList(), this);
        recyclerView.setAdapter(recentAdapter);

        root.findViewById(R.id.button_add).setOnClickListener(v -> getPresenter().fireAddClick());
        return root;
    }

    @Override
    public void savePresenterState(@NonNull MainPresenter presenter, @NonNull Bundle outState) {
        presenter.saveState(outState);
    }

    @Override
    public IPresenterFactory<MainPresenter> getPresenterFactory(@Nullable Bundle saveInstanceState) {
        return () -> new MainPresenter(saveInstanceState);
    }

    @Override
    protected String tag() {
        return MainFragment.class.getSimpleName();
    }

    @Override
    public void displayRecent(List<PdfFile> files) {
        if(recentAdapter != null){
            recentAdapter.setFiles(files);
        }
    }

    private void openPdfFile(File file){
        final Context app = getActivity().getApplicationContext();
        final Uri uri = FileProvider.getUriForFile(app, app.getApplicationContext().getPackageName() + ".fileprovider", file);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(app, R.string.error_activity_not_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void notifyRecentChanged() {
        if(recentAdapter != null){
            recentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyRecentAdded(int position) {
        if(recentAdapter != null){
            recentAdapter.notifyItemInserted(position);
        }
    }

    @Override
    public void startCreator() {
        startActivity(new Intent(getActivity(), PdfCreateActivity.class));
    }

    @Override
    public void onRecentClick(PdfFile file) {
        openPdfFile(file.getFile());
    }
}
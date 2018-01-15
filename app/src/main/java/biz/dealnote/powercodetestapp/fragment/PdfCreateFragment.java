package biz.dealnote.powercodetestapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import biz.dealnote.mvp.core.IPresenterFactory;
import biz.dealnote.mvp.ui.AbsPresenterFragment;
import biz.dealnote.powercodetestapp.R;
import biz.dealnote.powercodetestapp.mvp.presenter.PdfCreatePresenter;
import biz.dealnote.powercodetestapp.mvp.view.IPdfCreateView;
import biz.dealnote.powercodetestapp.utils.TextWatcherAdapter;
import biz.dealnote.powercodetestapp.view.InputTextDialog;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class PdfCreateFragment extends AbsPresenterFragment<PdfCreatePresenter, IPdfCreateView> implements IPdfCreateView {

    private static final int REQUEST_SELECT_PHOTO_FROM_GALLERY = 1;
    private static final int REQUEST_WRITE_STORAGE = 2;

    public static PdfCreateFragment newInstance() {
        Bundle args = new Bundle();
        PdfCreateFragment fragment = new PdfCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView pageNumView;
    private ImageView imageView;
    private TextView commentView;

    private View buttonAddPage;
    private View buttonCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pdf_create, container, false);

        pageNumView = root.findViewById(R.id.page_num);

        imageView = root.findViewById(R.id.image);
        commentView = root.findViewById(R.id.comment);
        commentView.addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                getPresenter().fireCommentEdit(s);
            }
        });

        root.findViewById(R.id.button_select_img).setOnClickListener(view -> onSelectImageClick());
        buttonAddPage = root.findViewById(R.id.button_add_page);
        buttonAddPage.setOnClickListener(view -> getPresenter().fireAddPageClick());

        buttonCreate = root.findViewById(R.id.button_create);
        buttonCreate.setOnClickListener(v -> onCreateClick());
        return root;
    }

    private void onCreateClick(){
        if(!hasWritePermission()){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            return;
        }

        showEnterFileNameDialog();
    }

    private boolean hasWritePermission(){
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_STORAGE && hasWritePermission()){
            showEnterFileNameDialog();
        }
    }

    private void showEnterFileNameDialog(){
        new InputTextDialog.Builder(getActivity())
                .setAllowEmpty(false)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setCallback(newValue -> getPresenter().fireCreateClick(newValue))
                .setTitleRes(R.string.file_name_title)
                .setHint(R.string.enter_file_name_hint)
                .setValue("document")
                .show();
    }

    private void onSelectImageClick(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_PHOTO_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_PHOTO_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            getPresenter().firePhotoFromGallerySelected(uri);
        }
    }

    @Override
    public void savePresenterState(@NonNull PdfCreatePresenter presenter, @NonNull Bundle outState) {
        presenter.saveState(outState);
    }

    @Override
    public IPresenterFactory<PdfCreatePresenter> getPresenterFactory(@Nullable Bundle saveInstanceState) {
        return () -> new PdfCreatePresenter(saveInstanceState);
    }

    @Override
    protected String tag() {
        return PdfCreateFragment.class.getSimpleName();
    }

    @Override
    public void displayImage(Uri uri) {
        if(imageView != null){
            if(uri == null){
                imageView.setImageDrawable(null);
            } else {
                imageView.setImageURI(uri);
            }
        }
    }

    @Override
    public void setButtonAddPageVisible(boolean visible) {
        if(buttonAddPage != null){
            buttonAddPage.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void setButtonCreateVisible(boolean visible) {
        if(buttonCreate != null){
            buttonCreate.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void diplayComment(String comment) {
        if(commentView != null){
            commentView.setText(comment);
        }
    }

    @Override
    public void displayPageNum(int num) {
        if (pageNumView != null && getContext() != null) {
            pageNumView.setText(getString(R.string.page_num_pattern, num));
        }
    }

    @Override
    public void displayCreationProgress(boolean creationNow) {
        if(creationNow){
            displayProgressDialog();
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void showError(Throwable t) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error_title)
                .setMessage(t.getMessage())
                .setPositiveButton(R.string.button_ok, null)
                .show();
    }

    private ProgressDialog mLoadingProgressDialog;

    private void displayProgressDialog() {
        dismissProgressDialog();

        mLoadingProgressDialog = new ProgressDialog(getActivity());
        mLoadingProgressDialog.setTitle(R.string.please_wait_title);
        mLoadingProgressDialog.setMessage(getString(R.string.file_creation_message));
        mLoadingProgressDialog.setCancelable(false);
        mLoadingProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (mLoadingProgressDialog != null) {
            if (mLoadingProgressDialog.isShowing()) {
                mLoadingProgressDialog.cancel();
            }

            mLoadingProgressDialog = null;
        }
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
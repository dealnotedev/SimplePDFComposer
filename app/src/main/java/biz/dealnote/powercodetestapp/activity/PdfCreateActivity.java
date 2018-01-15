package biz.dealnote.powercodetestapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import biz.dealnote.powercodetestapp.R;
import biz.dealnote.powercodetestapp.fragment.PdfCreateFragment;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class PdfCreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_create);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, PdfCreateFragment.newInstance())
                    .commit();
        }
    }
}
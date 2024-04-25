package com.skyncalci.ncalc.document;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skyncalci.R;
import com.skyncalci.calculator.activities.base.BaseActivity;
import com.skyncalci.ncalc.document.model.FunctionDocumentItem;

public class MarkdownDocumentActivity extends BaseActivity {

    public static final String EXTRA_ASSET_PATH = "MarkdownDocumentActivity.EXTRA_ASSET_PATH";
    public static final String EXTRA_DOCUMENT_NAME = "MarkdownDocumentActivity.EXTRA_DOCUMENT_NAME";

    public static void open(Fragment fragment, FunctionDocumentItem item) {
        Intent intent = new Intent(fragment.getContext(), MarkdownDocumentActivity.class);
        intent.putExtra(MarkdownDocumentActivity.EXTRA_ASSET_PATH, item.getAssetPath());
        intent.putExtra(MarkdownDocumentActivity.EXTRA_DOCUMENT_NAME, item.getName());
        fragment.startActivityForResult(intent, 0);
    }

    public static void open(Activity activity, FunctionDocumentItem item) {
        Intent intent = new Intent(activity, MarkdownDocumentActivity.class);
        intent.putExtra(MarkdownDocumentActivity.EXTRA_ASSET_PATH, item.getAssetPath());
        intent.putExtra(MarkdownDocumentActivity.EXTRA_DOCUMENT_NAME, item.getName());
        activity.startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment_content);
        setupToolbar();

        String path = getIntent().getStringExtra(EXTRA_ASSET_PATH);
        if (path == null) {
            return;
        }
        String title = getIntent().getStringExtra(EXTRA_DOCUMENT_NAME);
        setTitle(title);

        MarkdownDocumentFragment fragment = MarkdownDocumentFragment.newInstance(path);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commitAllowingStateLoss();
    }
}

package com.skyncalci.ncalc.document;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skyncalci.R;
import com.skyncalci.ncalc.document.model.FunctionDocumentItem;
import com.skyncalci.ncalc.utils.DLog;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MarkdownListDocumentFragment extends Fragment
        implements MarkdownListDocumentAdapter.OnDocumentClickListener {
    public static final String KEY_ASSET_PATHS = "MarkdownListDocumentFragment.KEY_ASSET_PATH";
    private static final String EXTRA_QUERY = "MarkdownListDocumentFragment.EXTRA_QUERY";
    @Nullable
    private static ArrayList<FunctionDocumentItem> documentItems;

    private MarkdownListDocumentAdapter adapter;
    private EditText searchView;


    public static MarkdownListDocumentFragment newInstance() {
        Bundle args = new Bundle();
        MarkdownListDocumentFragment fragment = new MarkdownListDocumentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static ArrayList<FunctionDocumentItem> loadDocumentStructure(@NonNull Context context) {
        if (documentItems != null && !documentItems.isEmpty()) {
            return documentItems;
        }
        try {
            InputStream in = context.getAssets().open("doc/help_functions_md_index.json");
            JSONObject root = new JSONObject(IOUtils.toString(in));

            JSONArray tutorialJSON = root.getJSONArray("children").getJSONObject(0).getJSONArray("children");
            ArrayList<FunctionDocumentItem> tutorials = new ArrayList<>();
            loadChildren(tutorialJSON, tutorials, "doc", false);
            Comparator<FunctionDocumentItem> comparator = new Comparator<FunctionDocumentItem>() {
                @Override
                public int compare(FunctionDocumentItem o1, FunctionDocumentItem o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };
            Collections.sort(tutorials, comparator);

            ArrayList<FunctionDocumentItem> documentItems = new ArrayList<>(tutorials);

            {
                for (int i = 0; i < tutorialJSON.length(); i++) {
                    JSONObject object = tutorialJSON.getJSONObject(i);
                    if (object.getString("name").equals("functions")) {
                        JSONArray functionJSON = object.getJSONArray("children");
                        ArrayList<FunctionDocumentItem> functions = new ArrayList<>();
                        loadChildren(functionJSON, functions, "doc/functions", false);
                        Collections.sort(functions, comparator);
                        documentItems.addAll(functions);
                    }
                }
            }
            MarkdownListDocumentFragment.documentItems = documentItems;
        } catch (Exception e) {
            e.printStackTrace();
            if (DLog.DEBUG) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>(documentItems);
    }

    /**
     * Structure: {"name": "fileName", "children": [ ... ] }
     */
    private static void loadChildren(JSONArray files, ArrayList<FunctionDocumentItem> documentItems,
                                     String parentPath, boolean recursive) throws JSONException {
        for (int i = 0; i < files.length(); i++) {
            JSONObject child = files.getJSONObject(i);
            String fileName = child.getString("name");
            if (fileName.equalsIgnoreCase("index.md")) {
                continue;
            }

            String name = fileName.replace("-", " ");
            name = name.replace(".md", "");
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

            String assetPath = parentPath + "/" + fileName;
            // check is directory
            if (child.has("children")) {
                if (recursive) {
                    loadChildren(child.getJSONArray("children"), documentItems, assetPath, recursive);
                }
            } else {
                String description = null;
                if (child.has("desc")) {
                    description = child.getString("desc");
                }
                documentItems.add(new FunctionDocumentItem(assetPath, name, description));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_documents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<FunctionDocumentItem> documentItems;
        //noinspection ConstantConditions
        @NonNull Context context = getContext();

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        SharedPreferences sharedPreferences=context.getSharedPreferences("happy",MODE_PRIVATE);
        AdView mAdView = view.findViewById(R.id.adView);
        if (sharedPreferences.contains("fr")){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }

        // Don't cost too much CPU
        documentItems = loadDocumentStructure(context);

        adapter = new MarkdownListDocumentAdapter(context, documentItems);
        adapter.setOnDocumentClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.edit_search_view);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onQueryTextChange(s.toString());
            }
        });

        if (savedInstanceState != null) {
            String query = savedInstanceState.getString(EXTRA_QUERY);
            if (query != null && !query.isEmpty()) {
                searchView.setText(query);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_QUERY, searchView.getText().toString());
    }

    @Override
    public void onDocumentClick(FunctionDocumentItem item) {
        MarkdownDocumentActivity.open(this, item);
    }

    private void onQueryTextChange(String newText) {
        adapter.query(newText);
    }
}

package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.PantryDao;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.util.NavHelper;
import com.example.smartpantry.util.Prefs;

public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {

    private PantryAdapter adapter;
    private PantryDao pantryDao;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryDao = AppDatabase.getInstance(this).pantryDao();
        emptyView = findViewById(R.id.text_empty);

        RecyclerView recycler = findViewById(R.id.recycler_pantry);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        recycler.setAdapter(adapter);

        pantryDao.getAllLive().observe(this, items -> {
            adapter.setItems(items);
            emptyView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        });

        findViewById(R.id.fab_add).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));

        NavHelper.setup(this, R.id.nav_pantry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setHighlightExpiring(Prefs.isExpiryAlertsOn(this));
        NavHelper.syncSelection(this, R.id.nav_pantry);
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.id);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove " + item.name + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) ->
                        AppDatabase.databaseExecutor.execute(() -> pantryDao.delete(item)))
                .setNegativeButton("Cancel", null)
                .show();
    }
}
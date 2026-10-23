package com.example.smartpantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.PantryDao;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.util.Format;
import com.example.smartpantry.util.Prefs;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;


public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";

    private TextInputLayout nameLayout;
    private TextInputLayout quantityLayout;
    private TextInputLayout expiryLayout;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextInputEditText expiryInput;
    private Spinner unitSpinner;
    private ArrayAdapter<CharSequence> unitAdapter;

    private PantryDao dao;
    private long itemId = -1;
    private Long expiryMillis = null;
    private boolean expiryChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dao = AppDatabase.getInstance(this).pantryDao();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        nameLayout = findViewById(R.id.layout_name);
        quantityLayout = findViewById(R.id.layout_quantity);
        expiryLayout = findViewById(R.id.layout_expiry);
        nameInput = findViewById(R.id.input_name);
        quantityInput = findViewById(R.id.input_quantity);
        expiryInput = findViewById(R.id.input_expiry);
        unitSpinner = findViewById(R.id.spinner_unit);

        unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.units, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            toolbar.setTitle("Edit Ingredient");
            loadExistingItem();
        } else {
            toolbar.setTitle("Add Ingredient");
            selectUnit(Prefs.getDefaultUnit(this));
        }

        expiryInput.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.button_clear_date).setOnClickListener(v -> {
            expiryMillis = null;
            expiryChanged = true;
            expiryInput.setText("");
        });
        findViewById(R.id.button_save).setOnClickListener(v -> save());
    }

    private void loadExistingItem() {
        AppDatabase.databaseExecutor.execute(() -> {
            final PantryItem item = dao.getById(itemId);
            runOnUiThread(() -> {
                if (item == null) {
                    finish();
                    return;
                }
                nameInput.setText(item.name);
                quantityInput.setText(Format.quantity(item.quantity));
                selectUnit(item.unit);
                if (item.expiryDate != null) {
                    expiryMillis = item.expiryDate;
                    expiryInput.setText(Format.date(item.expiryDate));
                }
            });
        });
    }

    private void selectUnit(String unit) {
        int position = unitAdapter.getPosition(unit);
        if (position >= 0) {
            unitSpinner.setSelection(position);
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        if (expiryMillis != null) {
            cal.setTimeInMillis(expiryMillis);
        }
        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar picked = Calendar.getInstance();
            picked.clear();
            picked.set(year, month, day);
            expiryMillis = picked.getTimeInMillis();
            expiryChanged = true;
            expiryInput.setText(Format.date(expiryMillis));
            expiryLayout.setError(null);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private static String textOf(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }


    private boolean validate() {
        nameLayout.setError(null);
        quantityLayout.setError(null);
        expiryLayout.setError(null);
        boolean valid = true;

        String name = textOf(nameInput);
        if (name.isEmpty()) {
            nameLayout.setError("Please enter an ingredient name");
            valid = false;
        } else if (name.length() < 2) {
            nameLayout.setError("Name must be at least 2 characters");
            valid = false;
        } else if (!name.matches(".*\\p{L}.*")) {
            nameLayout.setError("Name must contain letters");
            valid = false;
        }

        String quantityText = textOf(quantityInput).replace(',', '.');
        if (quantityText.isEmpty()) {
            quantityLayout.setError("Please enter a quantity");
            valid = false;
        } else {
            try {
                double quantity = Double.parseDouble(quantityText);
                if (quantity <= 0) {
                    quantityLayout.setError("Quantity must be greater than zero");
                    valid = false;
                } else if (quantity > 100000) {
                    quantityLayout.setError("That quantity is too large");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                quantityLayout.setError("Enter a valid number");
                valid = false;
            }
        }

   
        if (expiryChanged && expiryMillis != null) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            if (expiryMillis < today.getTimeInMillis()) {
                expiryLayout.setError("Expiry date cannot be in the past");
                valid = false;
            }
        }
        return valid;
    }

    private void save() {
        if (!validate()) {
            return;
        }

        final PantryItem item = new PantryItem();
        item.name = textOf(nameInput);
        item.quantity = Double.parseDouble(textOf(quantityInput).replace(',', '.'));
        item.unit = unitSpinner.getSelectedItem().toString();
        item.expiryDate = expiryMillis;
        final boolean isNew = itemId == -1;
        if (!isNew) {
            item.id = itemId;
        }

        AppDatabase.databaseExecutor.execute(() -> {
            if (isNew) {
                dao.insert(item);
            } else {
                dao.update(item);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, isNew ? "Ingredient added" : "Ingredient updated",
                        Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}

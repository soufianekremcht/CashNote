package com.soufianekre.cashnotes.ui.transactions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.helper.DialogsUtil;
import com.soufianekre.cashnotes.ui.base.BaseActivity;
import com.soufianekre.cashnotes.ui.transaction_edit.TransactionEditorActivity;
import com.soufianekre.cashnotes.ui.transactions.search.SearchActivity;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.cashnotes.helper.AppConst.TRANSACTION_ACCOUNT;
import static com.soufianekre.cashnotes.helper.AppConst.TRANSACTION_TO_EDIT;
import static com.soufianekre.cashnotes.ui.main.MainActivity.RESULT_T;
import static com.soufianekre.cashnotes.ui.main.MainActivity.SELECTED_ACCOUNT;

@SuppressLint("NonConstantResourceId")
public class TransactionsActivity extends BaseActivity implements TransactionsContract.View {

    // CONSTANTS
    @BindView(R.id.transactions_toolbar)
    Toolbar transactionsToolbar;
    @BindView(R.id.transactions_recycler_view)
    RecyclerView transactionsRecyclerView;
    @BindView(R.id.transaction_empty_view_layout)
    RelativeLayout emptyView;
    @BindView(R.id.account_balance_value_text)
    TextView accountBalanceValueText;
    @BindView(R.id.account_incomes_value_text)
    TextView accountIncomeValueText;
    @BindView(R.id.account_expenses_value_text)
    TextView accountExpenseValueText;
    @BindView(R.id.add_transaction_fab)
    FloatingActionButton addTransactionFab;


    @Inject
    TransactionsAdapter transactionsAdapter;

    @Inject
    TransactionsContract.Presenter<TransactionsContract.View> presenter;
    ShowTransactionFragment dialog;
    private ActionModeTransactions actionModeCallback;
    private ActionMode actionMode;
    private CashAccount currentAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        checkIntents();
        setupUi();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.main_menu_search:
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_T) {
            if (getIntent() != null) {
                currentAccount = getIntent().getParcelableExtra(TRANSACTION_ACCOUNT);
                if (currentAccount != null)
                    presenter.getTransactions(currentAccount.getId());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentAccount != null)
            presenter.getTransactions(currentAccount.getId());
        else
            showError("We could not fetch transactions");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.setDialogListener(null);
            dialog.onDestroy();
            dialog = null;
        }

    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onTransactionClick(CashTransaction transaction, int position) {
        // show Dialog with customLayout instead of activity
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            dialog = ShowTransactionFragment.newInstance(transaction, position,true);
            dialog.setDialogListener(this);
            dialog.show(getSupportFragmentManager(), "show_transaction_dialog");
        }
    }

    @Override
    public boolean onTransactionLongClick(int position) {
        enableActionMode(position);
        return true;
    }

    @Override
    public void onTransactionDeleteClicked(CashTransaction transaction, int position) {
        presenter.deleteTransaction(transaction,position);
    }


    @Override
    public void notifyAdapter(List<CashTransaction> transactionList, int income, int expense) {
        accountBalanceValueText.setText(String.format("%d", income + expense));
        accountExpenseValueText.setText(String.format("%d", expense));
        accountIncomeValueText.setText(String.format("%d", income));
        transactionsAdapter.addItems(transactionList);
        checkEmptyView(transactionsAdapter);
    }


    private void checkIntents() {
        if (getIntent() != null) {
            currentAccount = getIntent().getParcelableExtra(SELECTED_ACCOUNT);
        }
    }

    private void setupUi() {

        transactionsToolbar.setTitle("Transactions :" + currentAccount.getName());
        setSupportActionBar(transactionsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        changeStatusBarColor(currentAccount.getColor());

        transactionsToolbar.setNavigationOnClickListener(v -> onBackPressed());

        addTransactionFab.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionsActivity.this, TransactionEditorActivity.class);
            intent.putExtra(TRANSACTION_ACCOUNT, currentAccount);
            startActivity(intent);
        });
        actionModeCallback = new ActionModeTransactions();
        // recycler_view

        transactionsAdapter.setAdapterListener(this);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        transactionsRecyclerView.setAdapter(transactionsAdapter);

    }


    // Action Mode

    private void enableActionMode(int position) {
        if (actionMode == null) {
            transactionsAdapter.selectionMode = true;
            actionMode = startActionMode(actionModeCallback);
        }
        toggleSelection(position);

    }

    private void toggleSelection(int position) {
        transactionsAdapter.toggleSelection(position);
        int count = transactionsAdapter.getSelectedItemsCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void deleteSelectedTransactions() {
        transactionsAdapter.resetAnimationIndex();
        List<Integer> selectedItemsPositions = transactionsAdapter.getSelectedItems();
        for (int i = selectedItemsPositions.size() - 1; i >= 0; i--) {
            transactionsAdapter.deleteItem(selectedItemsPositions.get(i));
        }
        transactionsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onTransactionEditFabClicked(CashTransaction transaction, int position) {

        Intent intent = new Intent(TransactionsActivity.this, TransactionEditorActivity.class);
        intent.putExtra(TRANSACTION_TO_EDIT, transaction);
        intent.putExtra(TRANSACTION_ACCOUNT, currentAccount);
        startActivity(intent);

    }

    private void checkEmptyView(TransactionsAdapter transactionsAdapter) {
        if (transactionsRecyclerView != null) {
            if (transactionsAdapter != null && transactionsAdapter.getItemCount() > 0) {
                transactionsRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                transactionsRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    class ActionModeTransactions implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menu_action_mode_delete) {// show Delete Dialog
                DialogsUtil.showBasicDialogWithListener(TransactionsActivity.this,
                        R.string.delete_message,
                        R.string.dialog_delete_transaction_title,
                        R.string.confirm,
                        R.string.cancel,
                        (dialog, which) -> {
                            deleteSelectedTransactions();
                            mode.finish();
                        }).show();

                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode.finish();
            actionMode = null;
            transactionsAdapter.clearSelections();
            transactionsAdapter.selectionMode = false;
            transactionsAdapter.notifyDataSetChanged();
        }
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setBackgroundDrawable(new ColorDrawable(color));

    }

}

package com.soufianekre.cashnote.ui.transactions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.DialogsUtil;
import com.soufianekre.cashnote.ui.app_base.BaseActivity;
import com.soufianekre.cashnote.ui.transaction_edit.TransactionEditorActivity;
import com.soufianekre.cashnote.ui.transactions.search.SearchActivity;
import com.soufianekre.cashnote.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.cashnote.helper.AppConst.ACCOUNT_PARENT_ID;
import static com.soufianekre.cashnote.helper.AppConst.TRANSACTION_ACCOUNT_PARENT;
import static com.soufianekre.cashnote.helper.AppConst.TRANSACTION_IS_EDITING;
import static com.soufianekre.cashnote.helper.AppConst.TRANSACTION_TO_EDIT_POS;
import static com.soufianekre.cashnote.ui.main.MainActivity.ACCOUNT_PARENT;
import static com.soufianekre.cashnote.ui.main.MainActivity.REFRESH_TRANSACTION_LIST_CODE;
import static com.soufianekre.cashnote.ui.main.MainActivity.RESULT_T;

@SuppressLint("NonConstantResourceId")
public class TransactionsActivity extends BaseActivity implements TransactionsContract.View {


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
    private List<CashTransaction> transactions;
    private CashAccount accountParent;
    private ActionModeTransactions actionModeCallback;
    private ActionMode actionMode;
    private int parentAccountId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        accountParent = getIntent().getParcelableExtra(ACCOUNT_PARENT);
        parentAccountId = getIntent().getIntExtra(ACCOUNT_PARENT_ID, -1);
        setupUi();

    }

    private void setupUi() {

        transactionsToolbar.setTitle("Transactions");
        setSupportActionBar(transactionsToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transactionsToolbar.setNavigationOnClickListener(v -> onBackPressed());

        addTransactionFab.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionsActivity.this, TransactionEditorActivity.class);
            intent.putExtra(TRANSACTION_ACCOUNT_PARENT, accountParent);
            startActivity(intent);
        });
        actionModeCallback = new ActionModeTransactions();
        // recycler_view
        transactions = new ArrayList<>();
        transactionsAdapter.setAdapterListener(this);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        transactionsRecyclerView.setAdapter(transactionsAdapter);

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
            accountParent = data.getParcelableExtra(ACCOUNT_PARENT);
            onFetchTransactions(accountParent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        onFetchTransactions(accountParent);
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
        // detach The View From the presenter
        presenter.onDetach();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onTransactionClick(int position) {
        // show Dialog with customLayout instead of activity
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            CashTransaction transactionToShow = transactions.get(position);
            dialog = ShowTransactionFragment.newInstance(transactionToShow, accountParent, position);
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
    public void notifyAdapter(List<CashTransaction> transactionList, int income, int expense) {
        accountBalanceValueText.setText(String.format("%d", income + expense));
        accountExpenseValueText.setText(String.format("%d", expense));
        accountIncomeValueText.setText(String.format("%d", income));
        this.transactions = transactionList;
        transactionsAdapter.addItems(transactionList);
        checkEmptyView(transactionsAdapter);
    }

    @Override
    public void onTransactionEdit(int position) {
        Intent intent = new Intent(this, TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TRANSACTION_ACCOUNT_PARENT, accountParent);
        bundle.putBoolean(TRANSACTION_IS_EDITING, true);
        bundle.putInt(TRANSACTION_TO_EDIT_POS, position);
        intent.putExtras(bundle);
        startActivityForResult(intent, REFRESH_TRANSACTION_LIST_CODE);
    }


    @Override
    public void onTransactionDelete(int position) {
        // i don't know what to do here tell me what to do please !!
        presenter.onDeleteOptionClick(accountParent, transactions, position);

    }

    @Override
    public void onTransactionEditClicked(View v, int position) {

        onTransactionEdit(position);
    }


    @Override
    public void setAccountParent(CashAccount account) {
        accountParent = account;
    }


    private void onFetchTransactions(CashAccount accountParent) {
        if (accountParent != null)
            presenter.getTransactions(accountParent.getAccountId());
        else
            showMessage("There is an error while fetching transactions ");

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
}

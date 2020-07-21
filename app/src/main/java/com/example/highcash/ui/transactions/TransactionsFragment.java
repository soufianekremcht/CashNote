package com.example.highcash.ui.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.di.component.ActivityComponent;
import com.example.highcash.ui.account_editor.adapter.AccountCategory;
import com.example.highcash.ui.transaction_editor.TransactionEditorActivity;
import com.example.highcash.ui.base.BaseFragment;
import com.example.highcash.ui.main.MainActivity;
import com.example.highcash.ui.transactions.show_transaction.ShowTransactionDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.highcash.ui.main.MainActivity.REFRESH_TRANSACTION_LIST_CODE;
import static com.example.highcash.helper.AppConst.ACCOUNT_PARENT_OF_TRANSACTION_SHOW;
import static com.example.highcash.helper.AppConst.TRANSACTION_ACCOUNT;
import static com.example.highcash.helper.AppConst.TRANSACTION_IS_EDITING;
import static com.example.highcash.helper.AppConst.TRANSACTION_SHOW;
import static com.example.highcash.helper.AppConst.TRANSACTION_TO_EDIT_POS;


public class TransactionsFragment extends BaseFragment implements TransactionContract.View {

    @BindView(R.id.transactions_recycler_view)
    RecyclerView transactionsRecyclerView;

    @BindView(R.id.transaction_empty_view_layout)
    RelativeLayout emptyView;

    @BindView(R.id.add_transaction_fab)
    FloatingActionButton addTransactionFab;

    @Inject
    TransactionsAdapter transactionsAdapter;

    @Inject
    TransactionContract.Presenter<TransactionContract.View> presenter;

    private List<CashTransaction> transactions;
    private CashAccount accountParent;
    private MainActivity mainActivity;
    private TransactionActionMode actionModeCallback;
    private ActionMode actionMode;


    ShowTransactionDialogFragment dialog;



    public static TransactionsFragment newInstance() {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions,container,false);
        mainActivity = (MainActivity) getActivity();
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            presenter.onAttach(this);
            transactionsAdapter.setAdapterListener(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //checkIntents();
        actionModeCallback = new TransactionActionMode();
        transactions = new ArrayList<>();
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionsRecyclerView.setAdapter(transactionsAdapter);
        setupTransactionAdapter();
        addTransactionFab.setOnClickListener(v -> {
            showTransactionEditorActivity();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setupTransactionAdapter();
    }

    @Override
    public void onDestroy() {
        // detach The View From the presenter
        presenter.onDetach();
        super.onDestroy();

    }
    @Override
    public void onStop() {
        if (dialog != null) {
            dialog.onDestroy();
        }
        super.onStop();

    }


    @Override
    public void onTransactionClick(int position) {
        // show Dialog with customLayout instead of activity
        if(actionMode != null){
            toggleSelection(position);
        }else{
            CashTransaction transactionToShow = transactions.get(position);
            dialog = new ShowTransactionDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(TRANSACTION_SHOW,transactionToShow);
            bundle.putParcelable(ACCOUNT_PARENT_OF_TRANSACTION_SHOW,accountParent);
            bundle.putInt("transaction_to_show_position",position);
            dialog.setArguments(bundle);
            dialog.setDialogListener(this);
            dialog.show(mainActivity.getSupportFragmentManager(),"show_transaction_dialog");
        }



    }
    @Override
    public boolean onTransactionLongClick(int position){
        enableActionMode(position);
        return true;
    }

    @Override
    public String getAccountSource() {
        return accountParent.getName();
    }

    @Override
    public AccountCategory getAccountCategory() {
        return accountParent.getCategory();
    }



    @Override
    public void showTransactionEditorActivity(){
        Intent intent = new Intent(getActivity(), TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        if (accountParent != null){
            bundle.putParcelable(TRANSACTION_ACCOUNT, accountParent);
            intent.putExtras(bundle);
            startActivityForResult(intent,REFRESH_TRANSACTION_LIST_CODE);
        }else{
            showMessage("Account Parent is null");
        }

    }


    @Override
    public void setTransactions(List<CashTransaction> transactionList) {
        this.transactions = transactionList;
        transactionsAdapter.addItems(transactionList);
        checkEmptyView(transactionsAdapter);
    }

    @Override
    public void onTransactionEdit(int position){

        Intent intent = new Intent(getActivity(), TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TRANSACTION_ACCOUNT, accountParent);
        bundle.putBoolean(TRANSACTION_IS_EDITING,true);
        bundle.putInt(TRANSACTION_TO_EDIT_POS,position);
        intent.putExtras(bundle);
        startActivityForResult(intent,REFRESH_TRANSACTION_LIST_CODE);
    }


    @Override
    public void setAccountParent(CashAccount account) {
        accountParent = account;
    }



    @Override
    public void onTransactionEditClicked(View v, int position) {
        onTransactionEdit(position);
    }

    public void onTransactionDelete(int position){
        // i don't know what to do here tell me what to do please !!
        accountParent.setTransactionsList(transactions);
        presenter.onDeleteOptionClick(accountParent,transactions,position);
        transactionsAdapter.deleteItem(position);

    }
    private void setupTransactionAdapter(){
        mainActivity.model.getAccountId().observe(getViewLifecycleOwner(),
                accountId
                        -> presenter.getTransactions(accountId)
        );

    }


    private void checkEmptyView(TransactionsAdapter transactionsAdapter){
        if (transactionsRecyclerView!= null){
            if (transactionsAdapter != null && transactionsAdapter.getItemCount()>0 ){
                transactionsRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }else{
                transactionsRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }


    // Action Mode

    private void enableActionMode(int position){
        if (actionMode == null){
            transactionsAdapter.selectionMode = true;
            actionMode = getActivity().startActionMode(actionModeCallback);
        }
        toggleSelection(position);

    }
    private void toggleSelection(int position){
        transactionsAdapter.toggleSelection(position);
        int count = transactionsAdapter.getSelectedItemsCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }




    class TransactionActionMode implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_action_mode_delete:
                    // show Delete Dialog
                    return true;
                case R.id.menu_action_mode_select_all:

                    // select All
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

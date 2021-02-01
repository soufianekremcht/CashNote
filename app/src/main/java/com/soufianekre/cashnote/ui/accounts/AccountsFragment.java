package com.soufianekre.cashnote.ui.accounts;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.ui.account_edit.AccountEditorActivity;
import com.soufianekre.cashnote.ui.app_base.BaseFragment;
import com.soufianekre.cashnote.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.soufianekre.cashnote.helper.AppConst.ACCOUNT_IS_EDITING;
import static com.soufianekre.cashnote.helper.AppConst.ACCOUNT_TO_EDIT_ID;

public class AccountsFragment extends BaseFragment implements AccountsContract.View {


    @BindView(R.id.accounts_recycler_view)
    RecyclerView accountsRecyclerView;
    @BindView(R.id.accounts_empty_view_layout)
    RelativeLayout emptyView;

    // Accounts General Info
    @BindView(R.id.total_balance_value_text)
    TextView totalBalanceValueText;
    @BindView(R.id.accounts_count_value_text)
    TextView accountsCountValueText;
    @BindView(R.id.transactions_count_value_text)
    TextView transactionsCountValueText;


    @Inject
    AccountsContract.Presenter<AccountsContract.View> presenter;
    @Inject
    AccountsAdapter accountsAdapter;
    @Inject
    LinearLayoutManager linearLayoutManager;


    private List<CashAccount> accountList;
    private MainActivity mainActivity;

    public static AccountsFragment newInstance() {

        Bundle args = new Bundle();
        AccountsFragment fragment = new AccountsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountList = new ArrayList<>();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_accounts, container, false);
        if (getActivityComponent() != null) {
            getActivityComponent().inject(this);
            setUnBinder(ButterKnife.bind(this, root));
            presenter.onAttach(this);
        }
        mainActivity = (MainActivity) getActivity();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountsRecyclerView.setHasFixedSize(true);
        accountsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        accountsRecyclerView.setLayoutManager(linearLayoutManager);
        accountsAdapter.setAdapterListener(this);
        accountsRecyclerView.setAdapter(accountsAdapter);
        presenter.getAccounts();


    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getAccounts();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setupAccountsAdapter(List<CashAccount> accounts) {
        int total_balance = 0;
        int accounts_count = accounts.size();
        int transactions_count = 0;
        for (CashAccount account : accounts) {
            for (CashTransaction cashTransaction : account.getTransactionsList()) {
                total_balance += cashTransaction.getBalance();
                transactions_count += 1;
            }
        }
        // Set Info Card
        totalBalanceValueText.setText(String.format("%d", total_balance));
        transactionsCountValueText.setText(String.format("%d", transactions_count));
        accountsCountValueText.setText(String.format("%d", accounts_count));

        accountsAdapter.addItems(accounts);
        accountList = accountsAdapter.getAccountList();
        Timber.e("The size of accounts %s ", accountsAdapter.getAccountList().size());
        checkEmptyView(accountsAdapter);
    }

    @Override
    public void onAccountClick(int position) {
        mainActivity.showTransactionsActivity(accountList.get(position));
    }

    @Override
    public void onAccountEdit(int position) {
        Intent intent = new Intent(getActivity(), AccountEditorActivity.class);
        intent.putExtra(ACCOUNT_TO_EDIT_ID, accountList.get(position).getAccountId());
        intent.putExtra(ACCOUNT_IS_EDITING, true);
        startActivity(intent);
    }

    public void onAccountDelete(int position) {
        accountList = accountsAdapter.getAccountList();
        presenter.onDeleteOptionClick(accountList, position);
        accountsAdapter.deleteItem(position);
        checkEmptyView(accountsAdapter);
    }

    private void checkEmptyView(AccountsAdapter accountsAdapter) {
        if (accountsRecyclerView != null) {
            if (accountsAdapter != null && accountsAdapter.getItemCount() > 0) {
                accountsRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                accountsRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }

    }

}

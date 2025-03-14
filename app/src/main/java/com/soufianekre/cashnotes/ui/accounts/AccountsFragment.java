package com.soufianekre.cashnotes.ui.accounts;


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

import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.account_edit.AccountEditorActivity;
import com.soufianekre.cashnotes.ui.base.BaseFragment;
import com.soufianekre.cashnotes.ui.main.MainActivity;
import com.soufianekre.cashnotes.ui.transactions.TransactionsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.soufianekre.cashnotes.helper.AppConst.ACCOUNT_IS_EDITING;
import static com.soufianekre.cashnotes.helper.AppConst.ACCOUNT_TO_EDIT_ID;
import static com.soufianekre.cashnotes.ui.main.MainActivity.SELECTED_ACCOUNT;

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


    private List<CashTransaction> currentTransactions;
    private List<CashAccount> currentAccountList;
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
        currentAccountList = new ArrayList<>();
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
        mainActivity.getFab().show();
        mainActivity.getMainToolbar().setTitle("Accounts");
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
    public void notifyAccountAdapter(List<CashAccount> accounts,List<CashTransaction> transactions) {
        int total_balance = 0;
        int accounts_count = accounts.size();
        // Set Info Card
        int transactions_count = transactions.size();

        int income = 0;
        int expense = 0;

        for (CashTransaction transaction : transactions) {
            if (transaction.isExpense()) {
                expense += transaction.getBalance();
            } else {
                income += transaction.getBalance();
            }

        }

        String totalBalance =String.format("%d %s", income +expense,
                MyApp.AppPref().getString(PrefsConst.PREF_DEFAULT_CURRENCY, "$"));
        totalBalanceValueText.setText(totalBalance);
        transactionsCountValueText.setText(String.format("%d", transactions_count));

        accountsCountValueText.setText(String.format("%d", accounts_count));

        accountsAdapter.insertItems(accounts);

        currentAccountList = accountsAdapter.getAccountList();
        currentTransactions = transactions;
        Timber.e("The size of accounts %s ", accountsAdapter.getAccountList().size());
        checkEmptyView(accountsAdapter);
    }


    @Override
    public void onAccountClick(int position) {
        Intent transactionActivityIntent = new Intent(getActivity(), TransactionsActivity.class);
        transactionActivityIntent.putExtra(SELECTED_ACCOUNT, currentAccountList.get(position));
        startActivity(transactionActivityIntent);
    }

    @Override
    public int onAccountTransactionsCount(int accountId) {
        int transaction_count = 0;
        for (CashTransaction transaction: currentTransactions){
            if (transaction.getAccountId() == accountId){
                transaction_count++;
            }
        }
        return transaction_count;
    }

    @Override
    public void onAccountEditClicked(int position) {
        Intent intent = new Intent(getActivity(), AccountEditorActivity.class);
        intent.putExtra(ACCOUNT_TO_EDIT_ID, currentAccountList.get(position).getId());
        intent.putExtra(ACCOUNT_IS_EDITING, true);
        startActivity(intent);
    }

    public void onAccountDeleteClicked(int position) {
        currentAccountList = accountsAdapter.getAccountList();
        Timber.e("Current Account LIst Size is %s", currentAccountList.size());
        presenter.onDeleteAccount(currentAccountList, position);

    }

    @Override
    public void onAccountDeleted(int position) {
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

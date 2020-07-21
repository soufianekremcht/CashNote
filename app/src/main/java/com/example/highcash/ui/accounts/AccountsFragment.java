package com.example.highcash.ui.accounts;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.account_editor.AccountEditorActivity;
import com.example.highcash.ui.base.BaseFragment;
import com.example.highcash.ui.main.MainActivity;
import com.example.highcash.ui.transactions.TransactionsFragment;
import com.example.highcash.helper.DialogsUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.highcash.helper.AppConst.ACCOUNT_TO_EDIT_ID;
import static com.example.highcash.ui.main.MainActivity.REFRESH_ACCOUNT_LIST_CODE;
import static com.example.highcash.helper.AppConst.ACCOUNT_IS_EDITING;

public class AccountsFragment extends BaseFragment implements AccountsContract.View {


    @BindView(R.id.accounts_recycler_view)
    RecyclerView accountsRecyclerView;
    @BindView(R.id.accounts_empty_view_layout)
    RelativeLayout emptyView;
    @BindView(R.id.add_account_fab)
    FloatingActionButton addAccountFab;


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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_accounts, container, false);
        if (getActivityComponent() != null){
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

        //accountsRecyclerView.setHasFixedSize(true);
        accountsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        accountsAdapter.setAdapterListener(this);
        accountsRecyclerView.setLayoutManager(linearLayoutManager);
        accountsRecyclerView.setAdapter(accountsAdapter);
        presenter.getAccounts();

        addAccountFab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountEditorActivity.class);
            startActivityForResult(intent,REFRESH_ACCOUNT_LIST_CODE);
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.getAccounts();


    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void setupAccountsAdapter(List<CashAccount> accounts) {
        accountsAdapter.addItems(accounts);
        accountList = accountsAdapter.getAccountList();
        showMessage(accountsAdapter.getAccountList().size()+ " size of accounts");
        //checkEmptyView(accountsAdapter);
    }

    @Override
    public void onAccountPopUpMenuShow(View v,int position) {
        PopupMenu popupMenu = new PopupMenu(getActivity(),v);
        popupMenu.inflate(R.menu.popup_menu_edit);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.popup_menu_edit:
                    onAccountEdit(position);
                    break;
                case R.id.popup_menu_delete:
                    //show delete dialog;
                    DialogsUtil.showBasicDialogWithListener(getActivity(),
                            R.string.string_delete_message,
                            R.string.string_dialog_delete_title,
                            R.string.confirm,
                            R.string.cancel,
                            (dialog, which) -> onAccountDelete(position)).show();
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    @Override
    public void onAccountClick(int position) {
        // view model
        mainActivity.model.setAccountId(accountList.get(position).getAccountId());
        String fragment_title= "Transactions : " +accountList.get(position).getName();
        mainActivity.setToolbarTitle(fragment_title);
        mainActivity.loadFragment(new TransactionsFragment(),
                MainActivity.FRAGMENT_TRANSACTION_TAG);

    }


    private void onAccountEdit(int position) {
        Intent intent = new Intent(getActivity(), AccountEditorActivity.class);
        intent.putExtra(ACCOUNT_TO_EDIT_ID,accountList.get(position).getAccountId());
        intent.putExtra(ACCOUNT_IS_EDITING,true);
        startActivity(intent);
    }

    public void onAccountDelete(int position) {
        accountList = accountsAdapter.getAccountList();
        presenter.onDeleteOptionClick(accountList,position);
        accountsAdapter.deleteItem(position);
        checkEmptyView(accountsAdapter);
    }

    private void checkEmptyView(AccountsAdapter accountsAdapter){
        if (accountsRecyclerView != null){
            if (accountsAdapter != null && accountsAdapter.getItemCount()>0){
                accountsRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }else{
                accountsRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }

    }

}

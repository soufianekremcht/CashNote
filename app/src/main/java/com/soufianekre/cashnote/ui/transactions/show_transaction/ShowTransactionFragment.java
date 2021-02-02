package com.soufianekre.cashnote.ui.transactions.show_transaction;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.AppUtils;
import com.soufianekre.cashnote.ui.base.BaseBottomSheetFragment;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.cashnote.helper.AppConst.TRANSACTION_SHOW;

public class ShowTransactionFragment extends BaseBottomSheetFragment implements ShowTransactionContract.View {

    // widget
    @BindView(R.id.show_transaction_description_text)
    TextView descriptionText;
    @BindView(R.id.show_transaction_source_text)
    TextView transactionSourceText;
    @BindView(R.id.show_transaction_balance_text)
    TextView balanceValueText;
    @BindView(R.id.show_transaction_category)
    ImageView categoryImg;
    @BindView(R.id.show_transaction_notes_text)
    TextView notesText;

    @BindView(R.id.show_transaction_last_updated_date)
    TextView lastUpdatedDateText;
    @BindView(R.id.show_transaction_edit_btn)
    FloatingActionButton editBtn;

    @Inject
    ShowTransactionPresenter<ShowTransactionContract.View> mPresenter;


    private ShowTransactionDialogListener listener;
    private CashTransaction transactionToShow;
    private int current_position;


    public static ShowTransactionFragment newInstance(CashTransaction transaction, int position) {

        Bundle args = new Bundle();
        args.putParcelable(TRANSACTION_SHOW, transaction);
        args.putInt("transaction_to_show_position", position);
        ShowTransactionFragment fragment = new ShowTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIntents();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_show_transaction, container, false);
        ButterKnife.bind(this, view);
        if (getActivityComponent() != null) {
            getActivityComponent().inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet;
                if (dialog != null) {
                    bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (bottomSheet != null) {
                        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }

                    // check google io /filter package for more info of how to use this
                }
            }
        });
        initView(view);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    private void checkIntents() {
        if (getArguments() != null) {
            transactionToShow = getArguments().getParcelable(TRANSACTION_SHOW);
            current_position = getArguments().getInt("transaction_to_show_position");
        }

    }

    private void initView(View dialogView) {
        mPresenter.getTransactionAccount(transactionToShow.getAccountId());

        editBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionEditFabClicked(transactionToShow, current_position);
                dismiss();
            }

        });
        String description = transactionToShow.getName();
        String transactionBalance = String.valueOf(transactionToShow.getBalance());
        String lastUpdateDateText = AppUtils.formatDate(
                new Date(transactionToShow.getLastUpdatedDate()), AppUtils.MAIN_DATE_FORMAT);

        descriptionText.setText(description);
        notesText.setText(transactionToShow.getNotes());
        balanceValueText.setText(transactionBalance);

        lastUpdatedDateText.setText(lastUpdateDateText);


        if (transactionToShow.getCategory() != null) {
            Glide.with(dialogView)
                    .asDrawable()
                    .load(transactionToShow.getCategory().getImage())
                    .into(categoryImg);
            categoryImg.setBackgroundTintList(ColorStateList.valueOf(transactionToShow.getCategory().getColor()));

        }

    }


    public void setDialogListener(ShowTransactionDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void setTransactionAccount(CashAccount transactionAccount) {
        transactionSourceText.setText(transactionAccount.getName());
    }


    public interface ShowTransactionDialogListener {
        void onTransactionEditFabClicked(CashTransaction transaction, int position);
    }
}

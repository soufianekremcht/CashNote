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
import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.cashnote.helper.AppConst.ACCOUNT_PARENT_OF_TRANSACTION_SHOW;
import static com.soufianekre.cashnote.helper.AppConst.TRANSACTION_SHOW;

public class ShowTransactionFragment extends BottomSheetDialogFragment {

    // widget
    @BindView(R.id.show_transaction_description_text)
    TextView descriptionText;
    @BindView(R.id.show_transaction_source_text)
    TextView transactionSourceText;
    @BindView(R.id.show_transaction_balance_value)
    TextView balanceValueText;
    @BindView(R.id.show_transaction_category)
    ImageView categoryImg;

    @BindView(R.id.show_transaction_last_updated_date)
    TextView lastUpdatedDateText;
    @BindView(R.id.show_transaction_edit_btn)
    FloatingActionButton editBtn;


    private ShowTransactionDialogListener listener;
    private CashTransaction transactionToShow;
    private CashAccount accountParent;
    private int current_position;

    public static ShowTransactionFragment newInstance(CashTransaction transaction,CashAccount accountParent,int position) {

        Bundle args = new Bundle();
        args.putParcelable(TRANSACTION_SHOW,transaction);
        args.putParcelable(ACCOUNT_PARENT_OF_TRANSACTION_SHOW,accountParent);
        args.putInt("transaction_to_show_position",position);
        ShowTransactionFragment fragment = new ShowTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntents();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_show_transaction, container, false);
        ButterKnife.bind(this, view);
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
                if (dialog != null){
                    bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    // check google io filter package for more info of how to use this
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
        super.onDestroy();
    }


    private void initView(View dialogView) {

        editBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionEditClicked(dialogView, current_position);
                dismiss();
            }

        });
        String description = transactionToShow.getName();
        String transactionSource = accountParent.getName();
        String transactionBalance = String.valueOf(transactionToShow.getBalance());
        String lastUpdateDateText = AppUtils.formatDate(
                new Date(transactionToShow.getLastUpdatedDate()), AppUtils.MAIN_DATE_FORMAT);

        descriptionText.setText(description);
        transactionSourceText.setText(transactionSource);

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


    private void handleIntents() {
        if (getArguments() != null)
            transactionToShow = getArguments().getParcelable(TRANSACTION_SHOW);
            accountParent = getArguments().getParcelable(ACCOUNT_PARENT_OF_TRANSACTION_SHOW);
            current_position = getArguments().getInt("transaction_to_show_position");
    }

    public void setDialogListener(ShowTransactionDialogListener listener) {
        this.listener = listener;
    }


    public interface ShowTransactionDialogListener {
        void onTransactionEditClicked(View v, int position);
    }
}

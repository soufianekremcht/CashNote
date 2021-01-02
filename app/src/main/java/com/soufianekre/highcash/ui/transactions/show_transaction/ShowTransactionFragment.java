package com.soufianekre.highcash.ui.transactions.show_transaction;


import android.os.Build;
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
import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.helper.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.highcash.helper.AppConst.ACCOUNT_PARENT_OF_TRANSACTION_SHOW;
import static com.soufianekre.highcash.helper.AppConst.TRANSACTION_SHOW;

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
    @BindView(R.id.show_transaction_date)
    TextView creationDateText;
    @BindView(R.id.show_transaction_last_updated_date)
    TextView lastUpdatedDateText;
    @BindView(R.id.show_transaction_edit_btn)
    FloatingActionButton editBtn;


    private ShowTransactionDialogListener listener;
    private CashTransaction transactionToShow;
    private CashAccount accountParent;
    private int position;

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
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet;
                bottomSheet = (FrameLayout)
                        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
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
            if (listener != null)
                listener.onTransactionEditClicked(dialogView, position);
        });

        descriptionText.setText(transactionToShow.getName());
        transactionSourceText.setText(String.format("Source : %s", accountParent.getName()));

        balanceValueText.setText(String.format(Locale.US, "Balance : %d $", transactionToShow.getBalance()));

        lastUpdatedDateText.setText(
                String.format(
                        "Last Updated : %s",
                        AppUtils.formatDate(new Date(transactionToShow.getLastUpdatedDate()), AppUtils.MAIN_DATE_FORMAT)));
        if (transactionToShow.getCategory() != null)
            Glide.with(getActivity())
                    .asDrawable()
                    .load(transactionToShow.getCategory().getCategoryImage())
                    .into(categoryImg);

    }


    private void handleIntents() {
        if (getArguments() != null)
            transactionToShow = getArguments().getParcelable(TRANSACTION_SHOW);
            accountParent = getArguments().getParcelable(ACCOUNT_PARENT_OF_TRANSACTION_SHOW);
            position = getArguments().getInt("transaction_to_show_position");
    }

    public void setDialogListener(ShowTransactionDialogListener listener) {
        this.listener = listener;
    }


    public interface ShowTransactionDialogListener {
        void onTransactionEditClicked(View v, int position);
    }
}

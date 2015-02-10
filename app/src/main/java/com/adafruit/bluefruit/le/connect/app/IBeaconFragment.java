package com.adafruit.bluefruit.le.connect.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.adafruit.bluefruit.le.connect.R;
import com.adafruit.bluefruit.le.connect.ble.BleManager;
import com.adafruit.bluefruit.le.connect.ui.keyboard.CustomEditTextFormatter;
import com.adafruit.bluefruit.le.connect.ui.keyboard.CustomKeyboard;

import java.util.Random;


public class IBeaconFragment extends Fragment {


    // UI
    private EditText mVendorEditText;
    private EditText mUuidEditText;
    private EditText mMajorEditText;
    private EditText mMinorEditText;
    private EditText mRssiEditText;

    // Keyboard
    private CustomKeyboard mCustomKeyboard;

    // Data
    private int mRssi;
    private OnFragmentInteractionListener mListener;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    public static IBeaconFragment newInstance(int rssi) {
        IBeaconFragment fragment = new IBeaconFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, rssi);
        fragment.setArguments(args);
        return fragment;
    }

    public IBeaconFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRssi = getArguments().getInt(ARG_PARAM1);
        }
    }

    public boolean onBackPressed() {
        if (mCustomKeyboard.isCustomKeyboardVisible()) {
            mCustomKeyboard.hideCustomKeyboard();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ibeacon, container, false);

        // UI
        mVendorEditText = (EditText) rootView.findViewById(R.id.vendorEditText);
        mUuidEditText = (EditText) rootView.findViewById(R.id.uuidEditText);
        mMajorEditText = (EditText) rootView.findViewById(R.id.majorEditText);
        mMinorEditText = (EditText) rootView.findViewById(R.id.minorEditText);
        mRssiEditText = (EditText) rootView.findViewById(R.id.rssiEditText);

        Button chooseButton = (Button) rootView.findViewById(R.id.chooseManufacturerButton);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseVendor(v);
            }
        });

        Button randomUuidButton = (Button) rootView.findViewById(R.id.randomUuidButton);
        randomUuidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRandomUuid(v);
            }
        });


        Button rssiRefreshButton = (Button) rootView.findViewById(R.id.rssiButton);
        rssiRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRefreshRssi(v);
            }
        });

        Button enableButton = (Button) rootView.findViewById(R.id.enableButton);
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEnable(v);
            }
        });

        Button disableButton = (Button) rootView.findViewById(R.id.disableButton);
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDisable();
            }
        });


        // Custom keyboard
        if (mCustomKeyboard == null) {
            mCustomKeyboard = new CustomKeyboard(getActivity());
        }

        mCustomKeyboard.attachToEditText(mVendorEditText, R.xml.keyboard_hexadecimal);
        CustomEditTextFormatter.attachToEditText(mVendorEditText, 4, "", 4);

        mCustomKeyboard.attachToEditText(mUuidEditText, R.xml.keyboard_hexadecimal);
        CustomEditTextFormatter.attachToEditText(mUuidEditText, 32, "-", 2);

        mCustomKeyboard.attachToEditText(mMajorEditText, R.xml.keyboard_hexadecimal);
        CustomEditTextFormatter.attachToEditText(mMajorEditText, 4, "", 4);

        mCustomKeyboard.attachToEditText(mMinorEditText, R.xml.keyboard_hexadecimal);
        CustomEditTextFormatter.attachToEditText(mMinorEditText, 4, "", 4);

        mCustomKeyboard.attachToEditText(mRssiEditText, R.xml.keyboard_decimal);
        CustomEditTextFormatter.attachToEditText(mRssiEditText, 3, "", 3);

        // Generate initial state
        String manufacturers[] = getResources().getStringArray(R.array.beacon_manufacturers_ids);
        String manufacturerId = manufacturers[1];
        mVendorEditText.setText(manufacturerId);
        onClickRandomUuid(null);
        mMajorEditText.setText("0000");
        mMinorEditText.setText("0000");
        mRssiEditText.setText("" + mRssi);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void onClickRandomUuid(View view) {
        final String kAllowedChars = "0123456789ABCDEF";
        final int kNumChars = 32;

        final Random random = new Random();
        final StringBuilder randomString = new StringBuilder(kNumChars);
        for (int i = 0; i < kNumChars; i++) {
            randomString.append(kAllowedChars.charAt(random.nextInt(kAllowedChars.length())));
        }

        String result = CustomEditTextFormatter.formatText(randomString.toString(), 32, "-", 2);
        mUuidEditText.setText(result);
    }

    public void onClickRefreshRssi(View view) {
        BleManager bleManager = BleManager.getInstance(getActivity());
        boolean waiting = bleManager.readRssi(); // Wait for callback
        if (waiting) {

        }
    }

    public void onChooseVendor(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.beacon_manufacturer_choose_title)
                .setItems(R.array.beacon_manufacturers_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String manufacturers[] = view.getContext().getResources().getStringArray(R.array.beacon_manufacturers_ids);
                        String manufacturerId = manufacturers[which];
                        mVendorEditText.setText(manufacturerId);
                    }
                });
        builder.create().show();
    }


    public void onClickEnable(View view) {
        String manufacturerId = "0x" + getVendor();
        String uuid = getUuid();
        String major = "0x" + getMajor();
        String minor = "0x" + getMinor();
        String rssi = getRssi();

        mListener.onEnable(manufacturerId, uuid, major, minor, rssi);
    }

    public String getVendor() {
        return mVendorEditText.getText().toString();
    }

    public String getUuid() {
        return mUuidEditText.getText().toString();
    }

    public String getMajor() {
        return mMajorEditText.getText().toString();
    }

    public String getMinor() {
        return mMinorEditText.getText().toString();
    }

    public String getRssi() {
        return mRssiEditText.getText().toString();
    }

    public void setRssi(int rssi) {mRssiEditText.setText(""+rssi); }

    public interface OnFragmentInteractionListener {
        public void onEnable(String vendor, String uuid, String major, String minor, String rssi);
        public void onDisable();
    }

}
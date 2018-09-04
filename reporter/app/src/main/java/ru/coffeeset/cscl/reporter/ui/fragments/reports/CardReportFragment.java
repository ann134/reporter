package ru.coffeeset.cscl.reporter.ui.fragments.reports;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.BarcodeEncoder;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.controllers.utils.ReporterPermissionProvider;
import ru.coffeeset.cscl.reporter.databinding.FragmentReportCardBinding;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;


public class CardReportFragment extends BaseFragment implements View.OnClickListener, QRCodeReaderView.OnQRCodeReadListener {

    private FragmentReportCardBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private QRCodeReaderView qrCodeReaderView;


    public static CardReportFragment newInstance() {
        return new CardReportFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        try {
            mListener = (ISupportActionBarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ISupportActionBarListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_report_card,
                container,
                false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshActionBar();
    }

    @Override
    public void refreshActionBar() {
        if (isVisible()) {
            mListener.onTitleSet("QR");
            mListener.onHomeButtonSet(true);
        }
    }


//=============================================================================


    @Override
    public void onStart() {
        super.onStart();

        qrCodeReaderView = viewDataBinding.qrCodeView;
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();

        viewDataBinding.read.setOnClickListener(this);
        viewDataBinding.show.setOnClickListener(this);
        viewDataBinding.settings.setOnClickListener(this);

        viewDataBinding.text.setText("8uijuijjg--j788-87");

        viewDataBinding.qrCodeView.setVisibility(View.GONE);
        viewDataBinding.imageView.setVisibility(View.GONE);


        if (hasPermission()){
            viewDataBinding.read.setVisibility(View.VISIBLE);
            viewDataBinding.settings.setVisibility(View.GONE);
            viewDataBinding.textPermissionDenied.setVisibility(View.GONE);
        } else {
            requestLocationPermission();
            viewDataBinding.read.setVisibility(View.GONE);
            viewDataBinding.settings.setVisibility(View.VISIBLE);
            viewDataBinding.textPermissionDenied.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {

        if (v.equals(viewDataBinding.read)) {
            viewDataBinding.qrCodeView.setVisibility(View.VISIBLE);
            viewDataBinding.imageView.setVisibility(View.GONE);

            openCamera();
        }

        if (v.equals(viewDataBinding.show)) {
            viewDataBinding.qrCodeView.setVisibility(View.GONE);
            viewDataBinding.imageView.setVisibility(View.VISIBLE);
            qrCodeReaderView.stopCamera();

            generateQr((String) viewDataBinding.text.getText());
        }

        if(v.equals(viewDataBinding.settings)){
            openAppSettings();
        }
    }


//=============================================================================
    //read


    private boolean hasPermission() {
        return (ReporterPermissionProvider.hasPermission(mContext, Manifest.permission.CAMERA));
    }


    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, ReporterPermissionProvider.LOCALE_PERMISSION);
    }


    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        startActivityForResult(intent, 0);
        Logger.d("stsrt settings");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == ReporterPermissionProvider.LOCALE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logger.d("PERMISSION_GRANTED");

                viewDataBinding.read.setVisibility(View.VISIBLE);
                viewDataBinding.settings.setVisibility(View.GONE);
                viewDataBinding.textPermissionDenied.setVisibility(View.GONE);
            }

            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Logger.d("PERMISSION_DENIED");

            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void openCamera() {
        qrCodeReaderView.startCamera();
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Toast.makeText(mContext, "Got it", Toast.LENGTH_SHORT).show();
        Logger.d(text);
        viewDataBinding.text.setText(text);

        qrCodeReaderView.stopCamera();
    }


//=============================================================================
    //show


    private void generateQr(String code) {
        Bitmap qrCode = getQrCodeBitmap(code, 210, 210);
        viewDataBinding.imageView.setImageBitmap(qrCode);
    }


    public static Bitmap getQrCodeBitmap(String code, int width, int height) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE, width, height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);

        } catch (WriterException e) {
            Logger.e("Qr generator error: " + e.getLocalizedMessage());
            return null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    public void onStop() {
        super.onStop();
        qrCodeReaderView.stopCamera();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
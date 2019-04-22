package com.baycosinus.chatdroid;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import java.io.File;
import java.io.IOException;

public class QR
{
    public final static int QRcodeWidth = 500 ;

    private Context context;
    QR(Context context)
    {
        this.context = context;
    }
    private Bitmap Encode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        context.getResources().getColor(R.color.black):context.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    private String Decode(Bitmap bitmap)
    {
        int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(intArray, 0 , bitmap.getWidth(), 0,0,bitmap.getWidth(), bitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        BinaryBitmap map = new BinaryBitmap(new HybridBinarizer(source));

        try
        {
            Result result = new MultiFormatReader().decode(map);
            return result.getText();
        }
        catch (NotFoundException e)
        {
            return null;
        }
    }
}

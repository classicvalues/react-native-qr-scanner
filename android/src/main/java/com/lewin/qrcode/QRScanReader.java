package com.lewin.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

/**
 * Created by lewin on 2018/3/14.
 */

public class QRScanReader extends ReactContextBaseJavaModule {

    public QRScanReader(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "QRScanReader";
    }

    @ReactMethod
    public void readerQR(String path, Promise promise ) {
        if (path == null || path.length() == 0) {
            promise.reject("404", "NOT_FOUND");
        }

        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 1200);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        int width=scanBitmap.getWidth();
        int height=scanBitmap.getHeight();
        int[] pixels=new int[width*height];
        scanBitmap.getPixels(pixels,0,width,0,0,width,height);//获取图片像素点
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(),scanBitmap.getHeight(),pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();

        try {
            Result result = reader.decode(bitmap1, hints);
            promise.resolve(result.getText());
        } catch (NotFoundException e) {
            promise.reject("404", "NOT_FOUND");
        } catch (ChecksumException e) {
            promise.reject("404", "NOT_FOUND");
        } catch (FormatException e) {
             promise.reject("404", "NOT_FOUND");
        }
    }

    /**
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (path == null || path.length() == 0) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        int width=scanBitmap.getWidth();
        int height=scanBitmap.getHeight();
        int[] pixels=new int[width*height];
        scanBitmap.getPixels(pixels,0,width,0,0,width,height);//获取图片像素点
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(),scanBitmap.getHeight(),pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            System.out.println("not found " + e.getMessage());
        } catch (ChecksumException e) {
            System.out.println("bad checksum " + e.getMessage());
        } catch (FormatException e) {
            System.out.println("bad format " + e.getMessage());
        }
        return null;
    }

    /**
     * @param path
     * @return
     */
    public static Result decodeBarcodeRGB(String path) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap barcode = BitmapFactory.decodeFile(path, opts);
        Result result = decodeBarcodeRGB(barcode);
        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * @param barcode
     * @return
     */
    public static Result decodeBarcodeRGB(Bitmap barcode) {
        int width = barcode.getWidth();
        int height = barcode.getHeight();
        int[] data = new int[width * height];
        barcode.getPixels(data, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap1);
        } catch (NotFoundException e) {
            System.out.println("not found2 " + e.getMessage());
        } catch (ChecksumException e) {
             System.out.println("bad checksum2 " + e.getMessage());
        } catch (FormatException e) {
            System.out.println("bad format2 " + e.getMessage());
        }
        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * @param path
     * @return
     */
    public static Result decodeBarcodeYUV(String path) {
        if (path == null || path.length() == 0) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap barcode = BitmapFactory.decodeFile(path, opts);
        Result result = decodeBarcodeYUV(barcode);
        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * @param barcode
     * @return
     */
    public static Result decodeBarcodeYUV(Bitmap barcode) {
        if (null == barcode) {
            return null;
        }
        int width = barcode.getWidth();
        int height = barcode.getHeight();
        int[] argb = new int[width * height];
        barcode.getPixels(argb, 0, width, 0, 0, width, height);
        byte[] yuv = new byte[width * height * 3 / 2];
        encodeYUV420SP(yuv, argb, width, height);
        Result result = decodeBarcodeYUV(yuv, width, height);

        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * @param yuv
     * @param width
     * @param height
     * @return
     */
    private static Result decodeBarcodeYUV(byte[] yuv, int width, int height) {
        long start = System.currentTimeMillis();
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(null);

        Result rawResult = null;
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(yuv, width, height, 0, 0,
                width, height, false);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                re.printStackTrace();
            } finally {
                multiFormatReader.reset();
                multiFormatReader = null;
            }
        }
        long end = System.currentTimeMillis();
        return rawResult;
    }


    /**
     * Y=0.299R+0.587G+0.114B;
     * U=-0.147R-0.289G+0.436B;
     * V=0.615R-0.515G-0.1B;
     *
     * @param yuv
     * @param argb
     * @param width
     * @param height
     */
    private static void encodeYUV420SP(byte[] yuv, int[] argb, int width, int height) {
        final int frameSize = width * height;
        int Y, U, V;
        int yIndex = 0;
        int uvIndex = frameSize;
        int R, G, B;
        int rgbIndex = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                R = (argb[rgbIndex] & 0xff0000) >> 16;
                G = (argb[rgbIndex] & 0xff00) >> 8;
                B = (argb[rgbIndex] & 0xff);
                //
                rgbIndex++;
                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;
                Y = Math.max(0, Math.min(Y, 255));
                U = Math.max(0, Math.min(U, 255));
                V = Math.max(0, Math.min(V, 255));
                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                // meaning for every 4 Y pixels there are 1 V and 1 U. Note the sampling is every other
                // pixel AND every other scan line.
                // ---Y---
                yuv[yIndex++] = (byte) Y;
                // ---UV---
                if ((j % 2 == 0) && (i % 2 == 0)) {
                    //
                    yuv[uvIndex++] = (byte) V;
                    //
                    yuv[uvIndex++] = (byte) U;
                }
            }
        }
    }
}


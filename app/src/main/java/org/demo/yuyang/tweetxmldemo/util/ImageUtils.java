package org.demo.yuyang.tweetxmldemo.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 3/27/16.
 */
public class ImageUtils {
    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /**
     * 请求裁剪
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
    /**
     * 从图片浏览界面发送动弹
     */
    public static final int REQUEST_CODE_GETIMAGE_IMAGEPAVER = 3;

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null || fileName == null || context == null) {
            return;
        }

        FileOutputStream fos = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    public static void saveImageToSD(Context context, String filePath,
                                     Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));

            if (!file.exists()) {
                file.mkdirs();
            }

            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();

            if (context != null) {
                scanPhoto(context, filePath);
            }
        }
    }

    private static void scanPhoto(Context context, String fileName) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri uri = Uri.fromFile(new File(fileName));

        intent.setData(uri);

        context.sendBroadcast(intent);
    }

    public static Bitmap getBitmapByPath(String path) {
        return getBitmapByPath(path, null);
    }

    private static Bitmap getBitmapByPath(String path,
                                          BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(new File(path));
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     */
    public static String getAbsolutePathFromNoStandardUri(Uri uri) {
        String filePath = null;

        String uriString = Uri.decode(uri.toString());

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (uriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + uriString.substring(pre1.length());

        } else if (uriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + uriString.substring(pre2.length());
        }

        return filePath;
    }

    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, proj,
                null,
                null,
                null);

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

    public static Bitmap loadImgThumbnail(Activity activity, String name,
                                          int kind) {
        Bitmap bitmap = null;
        String[] projections = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME};

        Cursor cursor = activity.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projections,
                MediaStore.Images.Media.DISPLAY_NAME + "='" + name + "'",
                null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ContentResolver resolver = activity.getContentResolver();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 1;
            // TODO bitmap= MethodsComM
        }

        return bitmap;
    }

    public static Bitmap loadImgThumbnail(String path, int w, int h) {
        Bitmap bitmap = getBitmapByPath(path);
        return zoomBitmap(bitmap, w, h);
    }

    /**
     * 计算原始图片缩放后的宽高
     */
    public static int[] scaleImageSize(int[] imgSize, int squareSize) {
        if (imgSize[0] <= squareSize && imgSize[1] <= squareSize) {
            return imgSize;
        }

        double ratio = squareSize
                / (double) Math.max(imgSize[0], imgSize[1]);

        return new int[]{(int) (imgSize[0] * ratio),
                (int) (imgSize[1] * ratio)};
    }

    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath 原始大图路径
     * @param thumbFilePath  输出缩略图路径
     * @param squareSize     输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static void createImageThumbnail(Context context,
                                            String largeImagePath, String thumbFilePath,
                                            int squareSize, int quality)
            throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap largeBitmap = getBitmapByPath(largeImagePath, options);

        if (largeBitmap == null) {
            return;
        }

        // 原始图片的高宽
        int[] largeImgSize = new int[]{largeBitmap.getWidth(),
                largeBitmap.getHeight()};

        // 计算原始图片缩放后的宽高
        int[] newImgSize = scaleImageSize(largeImgSize, squareSize);

        // 生成缩放后的bitmap
        Bitmap thumbBitmap = zoomBitmap(largeBitmap, newImgSize[0],
                newImgSize[1]);

        saveImageToSD(null, thumbFilePath, thumbBitmap, quality);
    }

    /**
     * 放大或缩小图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Matrix matrix = new Matrix();
            float scaleWidth = ((float) w / width);
            float scaleHeight = ((float) h / height);

            matrix.postScale(scaleWidth, scaleHeight);

            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        }

        return newBitmap;

    }

    public static String getImageType(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        byte[] bytes = new byte[8];
        try {
            inputStream.read(bytes);

            // public int read (byte[] buffer, int byteOffset, int byteCount)

            // Reads up to byteCount bytes from this stream and stores them
            // in the byte array buffer starting at byteOffset.

            // Returns the number of bytes actually read or
            // -1 if the end of the stream has been reached.

            // public int read(byte[] buffer)
            // equivalent to read(buffer, 0, buffer.length)
            return getImageType(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }
    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }

    public static String getImagePath(Uri uri, Activity context) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String imagePath = cursor.getString(columnIndex);

            cursor.close();

            return imagePath;
        }

        return uri.toString();
    }

    static Bitmap bitmap=null;

    public static Bitmap loadPicasaImageFromGallery(final Uri uri,
                                                    final Activity context) {
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (columnIndex != -1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = MediaStore.Images.Media
                                    .getBitmap(context.getContentResolver(),
                                            uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            cursor.close();

            return bitmap;
        } else {
            return null;
        }
    }
}


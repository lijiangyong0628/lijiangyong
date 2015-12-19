package com.ljy.mychat.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;

@SuppressLint("NewApi")
public class ImageUtil
{
	public static byte[] getimage(String path) throws Exception
	{
		URL url = new URL(path);// ����URL
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ������
		conn.setRequestMethod("GET");// �������ӷ�ʽ
		conn.setConnectTimeout(5 * 1000);// �������ӳ�ʱ
		InputStream inStream = conn.getInputStream();// �õ����뛎

		byte[] data = readinputStream(inStream);
		return data;
	}

	public static byte[] readinputStream(InputStream inputStream)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int lns = 0;
		try {
			while ((lns = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, lns);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

	public static Bitmap getBitmapFromBase64String(String imageString)
	{
		// ���ֽ������ַ�������Base64���벢����ͼ�d
		if (imageString == null) // ͼ������Ϊ��
			return null;
		byte[] data = Base64.decode(imageString, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
	

	public static String getBitmapString(String image)
	{
		Bitmap bitmap = BitmapFactory.decodeFile(image);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] data = baos.toByteArray();

		return Base64.encodeToString(data, Base64.DEFAULT);// ����Base64��������ֽ������ַ�
	}
	

	public static String getBase64StringFromFile(String imageFile)
	{
		// ��ͼƬ�ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��
		InputStream in = null;
		byte[] data = null;
		// ��ȡͼƬ�ֽ�����
		try
		{
			in = new FileInputStream(imageFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// ���ֽ�����Base64����
		return Base64.encodeToString(data, Base64.DEFAULT);// ����Base64��������ֽ������ַ�
	}
	
	
	/**
	 * drawable -> Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {  
        
        Bitmap bitmap = Bitmap  
                        .createBitmap(  
                                        drawable.getIntrinsicWidth(),  
                                        drawable.getIntrinsicHeight(),  
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                                        : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        //canvas.setBitmap(bitmap);  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
        drawable.draw(canvas);  
        return bitmap;  
	}
	
	/**
	 * resource - > Bitmap
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap resourceToBitmap(Context context ,int resId){
		Resources res = context.getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
		return bitmap;  
	}
	
	
	/**
	 * Bitmap   - > Bytes
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm){  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);    
	    return baos.toByteArray();  
	}
	
	/**
	 * Bytes  - > Bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bitmap(byte[] b){  
        if(b.length!=0){  
            return BitmapFactory.decodeByteArray(b, 0, b.length);  
        }  
        else {  
            return null;  
        }  
	}
	
	
	public static Bitmap b2Bitmap(byte[] b) {
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (bais == null)
			return null;
		return FormatTools.getInstance().InputStream2Bitmap(bais);
	}
	
	public static Bitmap createImageThumbnail(String filePath,int maxSize){
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, maxSize);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        }catch (Exception e) {
           // TODO: handle exception
       }
       return bitmap;
   }
	
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	

   public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
       int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
       int roundedSize;
       if (initialSize <= 8) {
           roundedSize = 1;
           while (roundedSize < initialSize) {
               roundedSize <<= 1;
           }
       } else {
           roundedSize = (initialSize + 7) / 8 * 8;
       }
       return roundedSize;
   }

   private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
       double w = options.outWidth;
       double h = options.outHeight;
       int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
       int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
       if (upperBound < lowerBound) {
           // return the larger one when there is no overlapping zone.
           return lowerBound;
       }
       if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
           return 1;
       } else if (minSideLength == -1) {
           return lowerBound;
       } else {
           return upperBound;
       }
   }
	
}
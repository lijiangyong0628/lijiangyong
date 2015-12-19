package com.ljy.mychat.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.ljy.ljychat.R;



public class ExpressionUtil {
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 */
    public static void dealExpression(Context context,SpannableString spannableString, Pattern patten, int start,String islist) throws Exception {
    	Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            String number = key.substring(10, key.indexOf("]"));
            
            //用反射模式来获得图片资源
            Field field = R.drawable.class.getDeclaredField("ee_"+(Integer.valueOf(number)+1));
			int resId = Integer.parseInt(field.get(null).toString());		
			
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                Matrix matrix = new Matrix(); 
                if("1".equals(islist)){
                	matrix.postScale(1.0f,1.0f); //长和宽放大缩小的比例
                }else{
                	matrix.postScale(2.0f,2.0f); //长和宽放大缩小的比例
                }
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                ImageSpan imageSpan = new ImageSpan(resizeBmp);				            
                int end = matcher.start() + key.length();					
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	
                if (end < spannableString.length()) {						
                    dealExpression(context,spannableString,  patten, end,islist);
                }
                break;
            }
        }
    }
    
    public static SpannableString getExpressionString(Context context,String str,String zhengze,String islist){
    	SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);//通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context,spannableString, sinaPatten, 0,islist);
        } catch (Exception e) {
            LiLog.d("dealExpression:"+e.getMessage());
        }
        return spannableString;
    }
    
    //匹配表情
    public static SpannableString getText(Context context,String text,String islist){
    	String zhengze = "\\[emothion:[0-9]{1,2}\\]"; // 正则表达式，用来判断消息内是否有表情
    	SpannableString spannableString = null;
		try {
			spannableString = ExpressionUtil
					.getExpressionString(context, text, zhengze,islist);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return spannableString;
    }
}
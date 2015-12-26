package com.example.myproject.picbrowse;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * 单张图片显示时用的imageview
 * @author Administrator
 *
 */
public class ScaleScreenImageView extends ImageView {
    
    public ScaleScreenImageView(Context context) {
        super(context);
    }
     
    public ScaleScreenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public ScaleScreenImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    /*private void showImage(final File resultFileArg) {
    	if (resultFileArg != null && resultFileArg.exists()) {  
    		// 添加下载图片至 imageView         
    		ViewTreeObserver vto2 = imageView1.getViewTreeObserver();    
    		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {   
    			@Override                public void onGlobalLayout() {     
    				if (Build.VERSION.SDK_INT < 16) {                     
    					imageView1.getViewTreeObserver().removeGlobalOnLayoutListener(this);   
    					} else {      
    						imageView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);                    }                    Bitmap bm = BitmapFactory.decodeFile(resultFileArg.getPath());                    Bitmap thumbnailImg = ThumbnailUtils.extractThumbnail(bm,                            imageView1.getMeasuredWidth(),                            imageView1.getMeasuredHeight());                    bm.recycle();                                        imageView1.setImageBitmap(thumbnailImg);                    // imageView1.setImageBitmap(bm);                }            });        }    }
    			}
    		}
    	}
    }*/
     
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (getWidth() > 0) {
            float es = (float) getWidth() / (float)bm.getWidth();
            int height = (int) (bm.getHeight() * es);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = height;
            setLayoutParams(params);
        }
    }
 
}
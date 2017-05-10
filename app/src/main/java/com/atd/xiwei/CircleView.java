package com.atd.xiwei;
import android.view.View;
import com.atd.xiwei.R;
import android.content.Context;
import android.graphics.*;
import android.util.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.view.*;
import android.support.v7.widget.*;
public class CircleView extends View
{
	private Context mContext;
	private Bitmap mBitmap;
	private Paint mBitmapPaint;//BitmapPaint
	private Paint mBorderPaint;
	private boolean mIsPresses;
	private BitmapShader mBitmapShader;
	private Matrix mShaderMatrix;
	private int mShape;
	private RectF mRcBitmap;
	private RectF mRcBorder;
	private float mRoundRadius;
	private float mCircleRadius;
	private int mBorderColor;//边框颜色
	private int mBorderWidth;//边框大小
	private int mCoverColor;
	private final static int DEFAULT_BORDER_WIDTH = 0;
	private final static int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
	/**
     * 默认按下态蒙层Color
     */
    private static final int DEFAULT_COVER_COLOR  = Color.parseColor("#40333333");

    /**
     * 默认圆角半径
     */
    private static final int DEFAULT_ROUND_RADIUS = 0;

    /**
     * 默认形状
     */
    public static final  int  SHAPE_REC    = 1; // 矩形
    public static final  int  SHAPE_CIRCLE = 2; // 圆形
	public CircleView(Context context)
	{
		this(context,null);
	}
	public CircleView(Context context,AttributeSet attrs)
	{
		this(context,attrs,0);
	}
	public CircleView(Context context,AttributeSet attrs,int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CircleView,defStyleAttr,0);
		mBorderColor = typedArray.getColor(R.styleable.CircleView_border_color,DEFAULT_BORDER_COLOR);
		mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleView_border_width,DEFAULT_BORDER_WIDTH);
		mCoverColor = typedArray.getColor(R.styleable.CircleView_cover_color,DEFAULT_COVER_COLOR);
		mRoundRadius = typedArray.getDimensionPixelSize(R.styleable.CircleView_round_radius,DEFAULT_ROUND_RADIUS);
		mShape = typedArray.getInteger(R.styleable.CircleView_shape,SHAPE_REC);
		typedArray.recycle();
		init();
	}
	public  void setImageBitmap(Bitmap bitmap)
	{
		mBitmap = bitmap;
		preDraw();
	}
	public  void setImageDrawble(Drawable drawable)
	{
		getBitmapFromDrawable(drawable);
		preDraw();
	}
	public void setImageResource(int resId)
	{
		if(resId != 0)
		{
			try{
				mBitmap = getBitmapFromDrawable(mContext.getResources().getDrawable(resId));
			}catch(Exception e)
			{
				Log.w("CircleView","找不到资源id"+resId);
			}
			preDraw();
		}
		
			
		
	}
	private Bitmap getBitmapFromDrawable(Drawable drawable)
	{
		if(drawable == null){
			return null;
		}
		if(drawable instanceof BitmapDrawable){
			return ((BitmapDrawable)drawable).getBitmap();
		}
		try{Bitmap bitmap;
		if (drawable instanceof ColorDrawable)
		{
			bitmap = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);
		}else {
			
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
		}
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0,0,canvas.getWidth(),getHeight());
		drawable.draw(canvas);
		return bitmap;}
		catch (OutOfMemoryError e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/*private Bitmap getBitmapFromDrawable(Drawable drawable) {
       /* if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }finally
		{
			return ((BitmapDrawable) drawable).getBitmap();
		}
    }*/
	private void init(){
		mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		mShaderMatrix = new Matrix();
		mRcBitmap = new RectF();
		mRcBorder = new RectF();
	}
	private void preDraw(){
		if ( mBitmap == null)
		{
			return;
		}
		mBitmapShader = new BitmapShader(mBitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
		//BitmapShader.TileMode.CLAMP拉伸模式
		mRcBorder.set(0,0,getWidth(),getHeight());
		mRoundRadius = Math.min((mRcBorder.height() - mBorderWidth )/2,(mRcBorder.width()- mBorderWidth/2));
		if (mShape == SHAPE_REC)
		{
			mRcBitmap.set(mBorderWidth,mBorderWidth,mRcBorder.width()-mBorderWidth,mRcBorder.height()-mBorderWidth);
		}
		if(mShape == SHAPE_CIRCLE)
		{
			mRcBitmap.set(mBorderWidth/2,mBorderWidth/2,mRcBorder.width()-mBorderWidth/2,mRcBorder.height()-mBorderWidth/2);
		}
		mCircleRadius = Math.min(mRcBitmap.height(),mRcBitmap.width());
		
		updateMatrixShader();
		invalidate();
	}
	private void updateMatrixShader()
	{
		float scale;
		float dx =0;
		float dy =0;//必须初始化，不然后面用不了
		mShaderMatrix.set(null);
		//(deep) copy the src matrix into this matrix. If src is null, reset this matrix to the identity matrix.
		if(mRcBitmap.height()/mBitmap.getHeight()>mRcBitmap.width()/mBitmap.getWidth())
		{//Bitmap width缩放程度大，这里假设缩
			scale = mRcBitmap.height()/(float)mBitmap.getHeight();
			dx = (mRcBitmap.width()-mBitmap.getWidth()*scale)*0.5f;//*0.5?
		}else{
			scale = mRcBitmap.width()/(float)mBitmap.getWidth();
			dy = (mRcBitmap.height() - mBitmap.getHeight()*scale)*0.5f;
		}
		mShaderMatrix.setScale(scale,scale);
		mShaderMatrix.postTranslate((int)(dx + 0.5f)+mBorderWidth,(int)(dy + 0.5f)+mBorderWidth);
		mBitmapShader.setLocalMatrix(mShaderMatrix);
		
	}
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        preDraw();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        preDraw();
    }
	@Override
	protected void onDraw(Canvas canvas)
	{
		if ( mBitmap != null)
		{
			if(mShape == SHAPE_CIRCLE)
			{
				canvas.drawCircle(getWidth()/2.0f,getHeight()/2.0f,mCircleRadius,mBitmapPaint);
				canvas.drawCircle(getWidth()/2.0f,getHeight()/2.0f,mRoundRadius,mBorderPaint);
			}else
			if(mShape == SHAPE_REC)
			{
				canvas.drawRoundRect(mRcBitmap,mRoundRadius,mRoundRadius,mBitmapPaint);
				canvas.drawRoundRect(mRcBorder,mRoundRadius,mRoundRadius,mBorderPaint);
			}
		}
	}

	@Override
	public void setPressed(boolean pressed)
	{
		super.setPressed(pressed);
		if(mIsPresses == pressed)
		{
			return;
		}
		if(mIsPresses)
		{
			mBitmapPaint.setColorFilter(new PorterDuffColorFilter(mCoverColor,PorterDuff.Mode.SRC_ATOP));
		}else
		{
			mBitmapPaint.setColorFilter(null);
		}invalidate();
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getActionMasked();
		switch(action){
			case MotionEvent.ACTION_DOWN:
				setPressed(true);break;
			case MotionEvent.ACTION_MOVE:
				setPressed(true);break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			setPressed(true);	
			break;
		}
		return true;
		
	}
	
//setter and getter
	public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }
        mBorderColor = borderColor;
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }
        mBorderWidth = borderWidth;
        preDraw();
    }

    public int getCoverColor() {
        return mCoverColor;
    }

    public void setCoverColor(int coverColor) {
        if (coverColor == mCoverColor) {
            return;
        }
        mCoverColor = coverColor;
    }

    public int getShape() {
        return mShape;
    }

    public void setShape(int shape) {
        mShape = shape;
        preDraw();
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        mRoundRadius = roundRadius;
        preDraw();
    }
}

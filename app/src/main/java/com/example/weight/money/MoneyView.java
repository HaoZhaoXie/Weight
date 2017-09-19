package com.example.weight.money;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class MoneyView extends View {
    private static final int START_ANGLE = -90;
    private Paint mTotalPaint;
    private Paint mMoneyPaint;
    private Paint mTitleTextPaint;
    private Paint mMoneyTextPaint;
    private Context mContext;
    private int width;
    private int height;
    private float center;
    private float radius;
    private RectF ringRectF;
    private int mMoneyArc = 0;
    private String mTotalFee = "0元";
    private String mTitleText = "还款金额";
    private float mTitleSize;

    public MoneyView(Context context) {
        this(context, null);
    }

    public MoneyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initParams();
        initPaint();
    }

    private void initParams() {
        height = width = mContext.getResources().getDisplayMetrics().widthPixels / 3;
        center = width / 2;
        radius = width * 3 / 8;
        ringRectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        mTitleSize = 0.3f * radius;
    }

    private void initPaint() {
        //设置整个圆形画笔
        mTotalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTotalPaint.setStyle(Paint.Style.STROKE);
        mTotalPaint.setStrokeWidth(20);
        mTotalPaint.setColor(Color.BLUE);
        mTotalPaint.setStrokeCap(Paint.Cap.BUTT);
        //设置当前金额画笔
        mMoneyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMoneyPaint.setStyle(Paint.Style.STROKE);
        mMoneyPaint.setStrokeWidth(20);
        mMoneyPaint.setColor(Color.YELLOW);
        mMoneyPaint.setStrokeCap(Paint.Cap.BUTT);
        //设置标题画笔
        mTitleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitleTextPaint.setColor(Color.BLACK);
        mTitleTextPaint.setTextSize(mTitleSize);
        //设置当前内容画笔
        mMoneyTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMoneyTextPaint.setStyle(Paint.Style.STROKE);
        mMoneyTextPaint.setColor(Color.BLUE);
        initMoneyTextSize();
    }

    private void initMoneyTextSize(){
        float moneyTextSize = 1.414f * radius / mTitleTextPaint.measureText(mTotalFee) * mTitleSize;
        if(moneyTextSize > 0.6f * radius){
            moneyTextSize = 0.6f * radius;
        }
        mMoneyTextPaint.setTextSize(moneyTextSize);
    }

    /**
     * @param expenses_fee 利息和费用
     * @param total_fee    总还款
     */
    public void initData(double expenses_fee, double total_fee) {
        mMoneyArc = (int) (expenses_fee * 360 / total_fee);
        mTotalFee = String.valueOf(total_fee) + "元";
        initMoneyTextSize();
        invalidate();
    }

    public void setTotalCircleColor(int totalColor) {
        mTotalPaint.setColor(totalColor);
    }

    public void setMoneyCircleColor(int moneyColor) {
        mMoneyPaint.setColor(moneyColor);
    }

    public void setTitleTextColor(int totalColor) {
        mTitleTextPaint.setColor(totalColor);
    }

    public void setMoneyTextColor(int moneyColor) {
        mMoneyTextPaint.setColor(moneyColor);
    }

    public void setTitleText(String titleText) {
        if (!TextUtils.isEmpty(titleText)) {
            this.mTitleText = titleText.length() > 4 ? titleText.substring(0, 3) : titleText;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(ringRectF, START_ANGLE + mMoneyArc, 360, false, mTotalPaint);
        canvas.drawArc(ringRectF, START_ANGLE, mMoneyArc, false, mMoneyPaint);

        canvas.drawText(mTotalFee, (width - mMoneyTextPaint.measureText(mTotalFee)) / 2, center, mMoneyTextPaint);
        canvas.drawText(mTitleText, (width - mTitleTextPaint.measureText(mTitleText)) / 2, center + (0.7f * radius + mTitleTextPaint.getTextSize()) / 2, mTitleTextPaint);
    }
}

package coolbuy360.control;



import java.text.DecimalFormat;

import coolbuy360.searchhealth.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * �Զ������ʵ������΢����ͼƬ����Ч����<br/>
 * 
 * 
 * 
 */
public class PorterDuffView extends ImageView {
		private static final String TAG="PorterDuffView";
        /** ǰ��Bitmap�߶�Ϊ1���ء�����ѭ��������������� */
        public static final int FG_HEIGHT = 1;
        /** ���ؽ���ǰ��ɫ */
        // public static final int FOREGROUND_COLOR = 0x77123456;
        public static final int FOREGROUND_COLOR = 0x77123456;
        /** ���ؽ���������ɫ�� */
        public static final int TEXT_COLOR = 0xff7fff00;
        /** ���Ȱٷֱ������С�� */
        public static final int FONT_SIZE = 20;
        private Bitmap bitmapBg, bitmapFg;
        private Paint paint;
        /** ��ʶ��ǰ���ȡ� */
        private float progress;
        /** ��ʶ����ͼƬ�Ŀ����߶ȡ� */
        private int width, height;
        /** ��ʽ������ٷֱȡ� */
        private DecimalFormat decFormat;
        /** ���Ȱٷֱ��ı���ê��Y��������ֵ�� */
        private float txtBaseY;
        /** ��ʶ�Ƿ�ʹ��PorterDuffģʽ������档 */
        private boolean porterduffMode;
        /** ��ʶ�Ƿ���������ͼƬ�� */
        private boolean loading;

        public PorterDuffView(Context context, AttributeSet attrs) {
                super(context, attrs);
                init(context, attrs);
        }

        /** ����һ���뱳��ͼƬ��ͬ��Ϊ1���ص�Bitmap���� */
        private static Bitmap createForegroundBitmap(int w) {
                Bitmap bm = Bitmap.createBitmap(w, FG_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bm);
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(FOREGROUND_COLOR);
                c.drawRect(0, 0, w, FG_HEIGHT, p);
                return bm;
        }

        private void init(Context context, AttributeSet attrs) {
                if (attrs != null) {
                        // //////////////////////////////////////////
                        // int count = attrs.getAttributeCount();
                        // for (int i = 0; i < count; i++) {
                        // LogOut.out(this, "attrNameRes:" +
                        // Integer.toHexString(attrs.getAttributeNameResource(i))//
                        // + " attrName:" + attrs.getAttributeName(i)//
                        // + " attrResValue:" + attrs.getAttributeResourceValue(i, -1)//
                        // + " attrValue:" + attrs.getAttributeValue(i)//
                        // );
                        // }
                        // //////////////////////////////////////////

                        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.PorterDuffView);
                        porterduffMode = typedArr.getBoolean(R.styleable.PorterDuffView_porterduffMode, false);
                }
                Drawable drawable = getDrawable();
                if (porterduffMode && drawable != null && drawable instanceof BitmapDrawable) {
                        bitmapBg = ((BitmapDrawable) drawable).getBitmap();
                        width = bitmapBg.getWidth();
                        height = bitmapBg.getHeight();
                        // LogOut.out(this, "width=" + width + " height=" + height);
                        bitmapFg = createForegroundBitmap(width);
                } else {
                        // ������Ҫ���Զ�����Ϊfalse��
                        porterduffMode = false;
                }

                paint = new Paint();
                paint.setFilterBitmap(false);
                paint.setAntiAlias(true);
                paint.setTextSize(FONT_SIZE);

                // ����FontMetrics��������ܣ��ɼ���
                // <a href="\"http://xxxxxfsadf.iteye.com/blog/480454\"" target="\"_blank\"">http://xxxxxfsadf.iteye.com/blog/480454</a>
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                // ע��۲챾�����
                // ascent:�����ַ��������ϵ��Ƽ���࣬Ϊ����
                Log.i(TAG, "ascent:" + fontMetrics.ascent//
                                // descent:�����ַ��������µ��Ƽ���࣬Ϊ����
                                + " descent:" + fontMetrics.descent //
                                // �����ַ��������ϵ�����࣬Ϊ����
                                + " top:" + fontMetrics.top //
                                // �����ַ��������µ�����࣬Ϊ����
                                + " bottom:" + fontMetrics.bottom//
                                // �ı�������֮����Ƽ����
                                + " leading:" + fontMetrics.leading);
                // �ڴ˴�ֱ�Ӽ����������������onDraw()�����ظ�����
                txtBaseY = (height - fontMetrics.bottom - fontMetrics.top) / 2;

                decFormat = new DecimalFormat("0.0%");
        }

        public void onDraw(Canvas canvas) {
                if (porterduffMode) {
                        int tmpW = (getWidth() - width) / 2, tmpH = (getHeight() - height) / 2;
                        // ��������ͼ
                        canvas.drawBitmap(bitmapBg, tmpW, tmpH, paint);
                        // ����PorterDuffģʽ
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
                        // canvas.drawBitmap(bitmapFg, tmpW, tmpH - progress * height,
                        // paint);
                        int tH = height - (int) (progress * height);
                        for (int i = 0; i < tH; i++) {
                                canvas.drawBitmap(bitmapFg, tmpW, tmpH + i, paint);
                        }

                        // ����ȡ��xfermode
                        paint.setXfermode(null);
                        int oriColor = paint.getColor();
                        paint.setColor(TEXT_COLOR);
                        paint.setTextSize(FONT_SIZE);
                        String tmp = decFormat.format(progress);
                        float tmpWidth = paint.measureText(tmp);
                        canvas.drawText(decFormat.format(progress), tmpW + (width - tmpWidth) / 2, tmpH + txtBaseY, paint);
                        // �ָ�Ϊ��ʼֵʱ����ɫ
                        paint.setColor(oriColor);
                } else {
                        Log.i(TAG, "onDraw super");
                        super.onDraw(canvas);
                }
        }

        public void setProgress(float progress) {
                if (porterduffMode) {
                        this.progress = progress;
                        // ˢ������
                        invalidate();
                }
        }

        public void setBitmap(Bitmap bg) {
                if (porterduffMode) {
                        bitmapBg = bg;
                        width = bitmapBg.getWidth();
                        height = bitmapBg.getHeight();

                        bitmapFg = createForegroundBitmap(width);

                        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                        txtBaseY = (height - fontMetrics.bottom - fontMetrics.top) / 2;
                        
                        setImageBitmap(bg);
                        // �������²��֣������ٴε���onMeasure()
                        // requestLayout();
                }
        }

        public boolean isLoading() {
                return loading;
        }

        public void setLoading(boolean loading) {
                this.loading = loading;
        }

        public void setPorterDuffMode(boolean bool) {
                porterduffMode = bool;
        }
}

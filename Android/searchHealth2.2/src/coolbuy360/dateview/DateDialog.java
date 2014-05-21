/**
 * copyright habei
 *  ��������ʱ��ѡ����
 */
package coolbuy360.dateview;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import coolbuy360.searchhealth.R;

public class DateDialog extends Dialog {

	private static int START_YEAR = 1900, END_YEAR = 2100;
	WheelView wv_year;
	WheelView wv_month;
	WheelView wv_day;
	WheelView wv_hours;
	WheelView wv_mins;
	// �Ƿ���ʾ Ĭ�ϲ���ʾ
	Boolean YEAR = true;
	// �Ƿ���ʾ�� Ĭ�ϲ���ʾ
	Boolean MONTH = true;
	// �Ƿ���ʾ�� Ĭ�ϲ���ʾ
	Boolean DAY = true;
	// �Ƿ���ʾʱ Ĭ�ϲ���ʾ
	Boolean HOUR = false;
	// �Ƿ���ʾ�� Ĭ�ϲ���ʾ
	Boolean MINUTES = false;
	// Ĭ�ϳ�ʼ������ѡ����
	Calendar calendar = Calendar.getInstance();
	int year = calendar.get(Calendar.YEAR);
	int month = calendar.get(Calendar.MONTH);
	int day = calendar.get(Calendar.DATE);
	int hour = calendar.get(Calendar.HOUR_OF_DAY);
	int minute = calendar.get(Calendar.MINUTE);

	
	Boolean isCustomerDate = false;
	String parten = "00";
	final DecimalFormat decimal = new DecimalFormat(parten);
	private LayoutInflater inflater;// �������õ�item��Ҫ�õ������

	/**
	 * �������ڿؼ����� Ĭ����ʾ �� �� �� ��ݷ�Χ1900��-2100��
	 */
	public DateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub	
	
	}
	public DateDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	/*
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datepicker);
		InitDate();
		// setContentView(dateview);
	}

	/**
	 * ��ʼ������
	 */
	private void InitDate() {
		// ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж�
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		// �ҵ�dialog�Ĳ����ļ�
		// View dateview = inflater.inflate(R.layout.datepicker, null);
		// ��
		if (YEAR) {
			wv_year = (WheelView) this.findViewById(R.id.year);
			wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// ����"��"����ʾ����
			wv_year.setCyclic(true);// ��ѭ������
			wv_year.setLabel("��");// �������
			wv_year.setCurrentItem(year - START_YEAR);// ��ʼ��ʱ��ʾ������
			wv_year.setVisibility(View.VISIBLE);
		}
		// ��
		if (MONTH) {
			wv_month = (WheelView) this.findViewById(R.id.month);
			wv_month.setAdapter(new NumericWheelAdapter(1, 12));
			wv_month.setCyclic(true);
			wv_month.setLabel("��");
			wv_month.setCurrentItem(month);
			wv_month.setVisibility(View.VISIBLE);
		}
		// ��
		if (DAY) {
			wv_day = (WheelView) this.findViewById(R.id.day);
			wv_day.setCyclic(true);
			// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
			if (list_big.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// ����
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
			wv_day.setLabel("��");
			wv_day.setCurrentItem(day - 1);
			wv_day.setVisibility(View.VISIBLE);
		}
		// ʱ
		if (HOUR) {
			wv_hours = (WheelView) this.findViewById(R.id.hour);
			wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
			wv_hours.setCyclic(true);
			wv_hours.setCurrentItem(hour);
			wv_hours.setLabel("ʱ");
			wv_hours.setVisibility(View.VISIBLE);
		}
		// ��
		if (MINUTES) {
			wv_mins = (WheelView) this.findViewById(R.id.mins);
			wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
			wv_mins.setCyclic(true);
			wv_mins.setCurrentItem(minute);
			wv_mins.setLabel("��");
			wv_mins.setVisibility(View.VISIBLE);
		}
		// ���"��"����
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// ���"��"����
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// ������Ļ�ܶ���ָ��ѡ��������Ĵ�С
		int textSize = 0;
		textSize = 20;

		wv_day.TEXT_SIZE = textSize;
		// wv_hours.TEXT_SIZE = textSize;
		// wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

		Button btn_cancel = (Button) this
				.findViewById(R.id.btn_datetime_cancel);
		btn_cancel.setOnClickListener(new cancelonclick());

	}

	/**
	 * �����Ƿ���ʾ�Զ�������(�����Զ�������)
	 * 
	 * @param iscustomerDate
	 *            �Ƿ��Զ�����ʾ����
	 */
	public void setCustomerDate(Boolean iscustomerDate) {
		isCustomerDate = iscustomerDate;
	}
	/**
	* ʱ��ؼ�����Ĭ��ֵ
	 * @param dt
	 *        �����ַ���
	 * @param dateformat
	 *        ת���ĸ�ʽ
	 * @return
	 * 
	 * */
	public void setCustomerDate(String dtstr,String dateformat)
	{
		isCustomerDate = true;
		Date dt= getDateFromStr(dtstr, dateformat);	
		//datedialogtest.setYear(true, 1911);
		
		this.setYear(true,dt.getYear()+START_YEAR);
		Log.i("chenlinwei","�Զ������1��"+dt.getYear()+"" );
		this.setMonth(true,dt.getMonth());
		this.setDay(true,dt.getDate());	
		if(HOUR)
		{			
			this.setHour(true, dt.getHours());
		}
		if(MINUTES)
		{
			this.setMinute(true, dt.getMinutes());			
		}
	}

	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ��Ĺ���
	 * @param cusYear
	 *            �Զ���Ĭ����ʾ����� ��ݷ�Χ1900-2100
	 */
	public void setYear(Boolean isSetYear, int cusYear) {
		YEAR = isSetYear;
		if (isCustomerDate) {
			Log.i("chenlinwei","�Զ������2��"+cusYear+"" );
			if (cusYear > START_YEAR && cusYear < END_YEAR)// ���С�ڻ�����޶���������ʾĬ������
			{
				Log.i("chenlinwei","�Զ������2��"+cusYear+"" );
				year = cusYear;
			}
		}
	}
	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ��Ĺ���	
	 */
	public void setYear(Boolean isSetYear) {
		YEAR = isSetYear;
	}

	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ�µ�ѡ����
	 * @param cusYear
	 *            �Զ���Ĭ����ʾ���·�
	 */
	public void setMonth(Boolean isSetMonth, int cusMonth) {
		MONTH = isSetMonth;
		if (isCustomerDate) {
			month = cusMonth;
		}
	}
	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ�µ�ѡ����
	 */
	public void setMonth(Boolean isSetMonth) {
		MONTH = isSetMonth;
	}

	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ�յ�ѡ����
	 * @param cusYear
	 *            �Զ���Ĭ����ʾ����
	 */
	public void setDay(Boolean isSetDay, int cusDay) {		
		DAY = isSetDay;
		if (isCustomerDate) {
			day = cusDay;
		}
	}
	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ�յ�ѡ����
	 */
	public void setDay(Boolean isSetDay) {
		DAY = isSetDay;
	}

	/**
	 * ���� ʱ�����ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾʱ��ѡ����
	 * @param cusYear
	 *            �Զ���Ĭ����ʾ��ʱ
	 */
	public void setHour(Boolean isSetHour, int cusHour) {
		HOUR = isSetHour;
		if (isCustomerDate) {
			hour = cusHour;
		}
	}
	/**
	 * ���� ʱ�����ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾʱ��ѡ����	
	 */
	public void setHour(Boolean isSetHour) {
		HOUR = isSetHour;
	}

	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ�ֵ�ѡ����
	 * @param cusYear
	 *            �Զ���Ĭ����ʾ�ķ�
	 */
	public void setMinute(Boolean isSetMinute, int cusMin) {
		MINUTES = isSetMinute;
		if (isCustomerDate) {
			minute = cusMin;
		}
	}
	/**
	 * ���� �������ʾ��
	 * 
	 * @param isSetYear
	 *            �Ƿ���ʾ�ֵ�ѡ����	
	 */
	public void setMinute(Boolean isSetMinute) {
		MINUTES = isSetMinute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#setContentView(android.view.View)
	 */
	/*
	 * @Override public void setContentView(View view) { // TODO Auto-generated
	 * method stub Calendar calendar = Calendar.getInstance(); int year =
	 * calendar.get(Calendar.YEAR); int month = calendar.get(Calendar.MONTH);
	 * int day = calendar.get(Calendar.DATE); int hour =
	 * calendar.get(Calendar.HOUR_OF_DAY); int minute =
	 * calendar.get(Calendar.MINUTE);
	 * 
	 * // ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж� String[] months_big = { "1", "3", "5", "7",
	 * "8", "10", "12" }; String[] months_little = { "4", "6", "9", "11" };
	 * 
	 * final List<String> list_big = Arrays.asList(months_big); final
	 * List<String> list_little = Arrays.asList(months_little); // �ҵ�dialog�Ĳ����ļ�
	 * View dateview = inflater.inflate(R.layout.datepicker, null);
	 * 
	 * // �� wv_year = (WheelView) dateview.findViewById(R.id.year);
	 * wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));//
	 * ����"��"����ʾ���� wv_year.setCyclic(true);// ��ѭ������ wv_year.setLabel("��");// �������
	 * wv_year.setCurrentItem(year - START_YEAR);// ��ʼ��ʱ��ʾ������
	 * //wv_year.setVisibility(view.GONE); // �� wv_month = (WheelView)
	 * dateview.findViewById(R.id.month); wv_month.setAdapter(new
	 * NumericWheelAdapter(1, 12)); wv_month.setCyclic(true);
	 * wv_month.setLabel("��"); wv_month.setCurrentItem(month);
	 * 
	 * 
	 * // �� wv_day = (WheelView) dateview.findViewById(R.id.day);
	 * wv_day.setCyclic(true); // �жϴ�С�¼��Ƿ�����,����ȷ��"��"������ if
	 * (list_big.contains(String.valueOf(month + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(month + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 30)); } else { // ���� if ((year % 4 == 0 && year %
	 * 100 != 0) || year % 400 == 0) wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 29)); else wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 28)); } wv_day.setLabel("��");
	 * wv_day.setCurrentItem(day - 1);
	 * 
	 * // ʱ
	 * 
	 * final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
	 * wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
	 * wv_hours.setCyclic(true); wv_hours.setCurrentItem(hour);
	 * 
	 * // �� final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
	 * wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
	 * wv_mins.setCyclic(true); wv_mins.setCurrentItem(minute);
	 * 
	 * 
	 * // ���"��"���� OnWheelChangedListener wheelListener_year = new
	 * OnWheelChangedListener() { public void onChanged(WheelView wheel, int
	 * oldValue, int newValue) { int year_num = newValue + START_YEAR; //
	 * �жϴ�С�¼��Ƿ�����,����ȷ��"��"������ if (list_big
	 * .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(wv_month .getCurrentItem() + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 30)); } else { if ((year_num
	 * % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 29)); else
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 28)); } } }; // ���"��"����
	 * OnWheelChangedListener wheelListener_month = new OnWheelChangedListener()
	 * { public void onChanged(WheelView wheel, int oldValue, int newValue) {
	 * int month_num = newValue + 1; // �жϴ�С�¼��Ƿ�����,����ȷ��"��"������ if
	 * (list_big.contains(String.valueOf(month_num))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(month_num))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 30)); } else { if (((wv_year.getCurrentItem() +
	 * START_YEAR) % 4 == 0 && (wv_year .getCurrentItem() + START_YEAR) % 100 !=
	 * 0) || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 29)); else
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 28)); } } };
	 * wv_year.addChangingListener(wheelListener_year);
	 * wv_month.addChangingListener(wheelListener_month);
	 * 
	 * // ������Ļ�ܶ���ָ��ѡ��������Ĵ�С int textSize = 0;
	 * 
	 * textSize = 12;
	 * 
	 * wv_day.TEXT_SIZE = textSize; // wv_hours.TEXT_SIZE = textSize; //
	 * wv_mins.TEXT_SIZE = textSize; wv_month.TEXT_SIZE = textSize;
	 * wv_year.TEXT_SIZE = textSize;
	 * 
	 * Button btn_cancel = (Button) dateview
	 * .findViewById(R.id.btn_datetime_cancel);
	 * btn_cancel.setOnClickListener(new cancelonclick());
	 * super.setContentView(dateview);
	 * 
	 * }
	 */
	
	
	/**
	 * ���ַ���ת����Date����
	 * @param dt
	 *        �����ַ���
	 * @param dateformat
	 *        ת���ĸ�ʽ
	 * @return
	 *       ����Date����
	 */
	 private  Date getDateFromStr(String dt, String dateformat)
		{
			SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
			Date strtodate;
			try {
				strtodate = formatter.parse(dt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
			 return null;
			}
			return strtodate;
			
		}
	

	/**
	 * ȡ����ť����ز���
	 */
	private final class cancelonclick implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DateDialog.this.dismiss();		
		}
	}

	/**
	 * ȷ����ť����ز���
	 * 
	 * @param str
	 *            ��ť��ʾ������
	 * @param listener
	 *            ��ť�ļ����¼�
	 */
	public void setOkListener(final String str,
			final View.OnClickListener listener) {

		Button btn_sure = (Button) this.findViewById(R.id.btn_datetime_sure);
		btn_sure.setText(str);
		btn_sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String time = wv_year.getCurrentItem() + START_YEAR + "-"
						+ decimal.format((wv_month.getCurrentItem() + 1)) + "-"
						+ decimal.format((wv_day.getCurrentItem() + 1)) + " ";
				v.setTag(time);
				listener.onClick(v);
				dismiss();
			}
		});
	}
	/**
	 * ��ô����ڸ�ʽ������
	 * @return
	 */
   public String getDateTime(String formatStr)
   {	  
	   Date dt=getDate();   
	   //SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E ");  
	   SimpleDateFormat dateformat=new SimpleDateFormat(formatStr);  
	   dt.getDay();
       String dateforWeek=dateformat.format(dt);	   
	   return dateforWeek;
   }
   public int getWeenk()
   {	   
	   return getDate().getDay();
   }
   
	/**
	 * ��ȡʱ��
	 * @return
	 */
   public Date getDate()
   {
	   String datetimeStr= getYear()+"-"+getMonth()+"-"+getDay();
	   String formatStr="yyyy-MM-dd";
	   if(HOUR)
	   {
		   datetimeStr=" "+datetimeStr+getHour()+":";
		   formatStr="yyyy-MM-dd hh";
		   if(MINUTES)
		   {
			   datetimeStr=datetimeStr+getMin()+":"+"00";
			   formatStr="yyyy-MM-dd hh:MM";
		   }
	   }
	   DateFormat f = new SimpleDateFormat(formatStr);
	   Date dt = null;
		try {
			Log.i("chenlinwei", "format"+formatStr);
			Log.i("chenlinwei","content" +datetimeStr);
			dt = f.parse(datetimeStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
		}
		Log.i("chenlinwei", dt.toString());
	   return dt;	   
   }
	
	/**
	 * �����
	 * 
	 * @return
	 */
	public String getYear() {
		return YEAR? wv_year.getCurrentItem() + START_YEAR + "":"";
	}

	/**
	 * �����
	 * 
	 * @return
	 */
	public String getMonth() {
		return MONTH? decimal.format((wv_month.getCurrentItem() + 1)):"";
	}

	/**
	 * ���Сʱ
	 * 
	 * @return
	 */
	public String getHour() {
		return HOUR?decimal.format(wv_hours.getCurrentItem()):"";
	}

	/**
	 * �����
	 * 
	 * @return
	 */
	public String getDay() {
		return DAY?decimal.format((wv_day.getCurrentItem() + 1)):"";
	}

	/**
	 * ��÷���
	 * 
	 * @return
	 */
	public String getMin() {
		return MINUTES?decimal.format(wv_mins.getCurrentItem()):"";
	}

	/*
	 * ���ñ���
	 * 
	 * @see android.app.Dialog#setTitle(java.lang.CharSequence)
	 */
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	/*
	 * ���ñ���
	 * 
	 * @see android.app.Dialog#setTitle(int)
	 */
	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		super.setTitle(titleId);
	}

}

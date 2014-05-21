/**
 * copyright habei
 *  弹出日期时间选择器
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
	// 是否显示 默认不显示
	Boolean YEAR = true;
	// 是否显示月 默认不显示
	Boolean MONTH = true;
	// 是否显示日 默认不显示
	Boolean DAY = true;
	// 是否显示时 默认不显示
	Boolean HOUR = false;
	// 是否显示分 默认不显示
	Boolean MINUTES = false;
	// 默认初始化日期选择项
	Calendar calendar = Calendar.getInstance();
	int year = calendar.get(Calendar.YEAR);
	int month = calendar.get(Calendar.MONTH);
	int day = calendar.get(Calendar.DATE);
	int hour = calendar.get(Calendar.HOUR_OF_DAY);
	int minute = calendar.get(Calendar.MINUTE);

	
	Boolean isCustomerDate = false;
	String parten = "00";
	final DecimalFormat decimal = new DecimalFormat(parten);
	private LayoutInflater inflater;// 创建引用的item需要用到填从器

	/**
	 * 构造日期控件函数 默认显示 年 月 日 年份范围1900年-2100年
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
	 * 初始化数据
	 */
	private void InitDate() {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		// 找到dialog的布局文件
		// View dateview = inflater.inflate(R.layout.datepicker, null);
		// 年
		if (YEAR) {
			wv_year = (WheelView) this.findViewById(R.id.year);
			wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
			wv_year.setCyclic(true);// 可循环滚动
			wv_year.setLabel("年");// 添加文字
			wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
			wv_year.setVisibility(View.VISIBLE);
		}
		// 月
		if (MONTH) {
			wv_month = (WheelView) this.findViewById(R.id.month);
			wv_month.setAdapter(new NumericWheelAdapter(1, 12));
			wv_month.setCyclic(true);
			wv_month.setLabel("月");
			wv_month.setCurrentItem(month);
			wv_month.setVisibility(View.VISIBLE);
		}
		// 日
		if (DAY) {
			wv_day = (WheelView) this.findViewById(R.id.day);
			wv_day.setCyclic(true);
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// 闰年
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
			wv_day.setLabel("日");
			wv_day.setCurrentItem(day - 1);
			wv_day.setVisibility(View.VISIBLE);
		}
		// 时
		if (HOUR) {
			wv_hours = (WheelView) this.findViewById(R.id.hour);
			wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
			wv_hours.setCyclic(true);
			wv_hours.setCurrentItem(hour);
			wv_hours.setLabel("时");
			wv_hours.setVisibility(View.VISIBLE);
		}
		// 分
		if (MINUTES) {
			wv_mins = (WheelView) this.findViewById(R.id.mins);
			wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
			wv_mins.setCyclic(true);
			wv_mins.setCurrentItem(minute);
			wv_mins.setLabel("分");
			wv_mins.setVisibility(View.VISIBLE);
		}
		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
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
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
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

		// 根据屏幕密度来指定选择器字体的大小
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
	 * 定义是否显示自定义日期(复杂自定义设置)
	 * 
	 * @param iscustomerDate
	 *            是否自定义显示日期
	 */
	public void setCustomerDate(Boolean iscustomerDate) {
		isCustomerDate = iscustomerDate;
	}
	/**
	* 时间控件设置默认值
	 * @param dt
	 *        日期字符串
	 * @param dateformat
	 *        转化的格式
	 * @return
	 * 
	 * */
	public void setCustomerDate(String dtstr,String dateformat)
	{
		isCustomerDate = true;
		Date dt= getDateFromStr(dtstr, dateformat);	
		//datedialogtest.setYear(true, 1911);
		
		this.setYear(true,dt.getYear()+START_YEAR);
		Log.i("chenlinwei","自定义的年1："+dt.getYear()+"" );
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
	 * 设置 年相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示年的滚轮
	 * @param cusYear
	 *            自定义默认显示的年份 年份范围1900-2100
	 */
	public void setYear(Boolean isSetYear, int cusYear) {
		YEAR = isSetYear;
		if (isCustomerDate) {
			Log.i("chenlinwei","自定义的年2："+cusYear+"" );
			if (cusYear > START_YEAR && cusYear < END_YEAR)// 如果小于或大于限定日期则显示默认日期
			{
				Log.i("chenlinwei","自定义的年2："+cusYear+"" );
				year = cusYear;
			}
		}
	}
	/**
	 * 设置 年相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示年的滚轮	
	 */
	public void setYear(Boolean isSetYear) {
		YEAR = isSetYear;
	}

	/**
	 * 设置 月相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示月的选择项
	 * @param cusYear
	 *            自定义默认显示的月份
	 */
	public void setMonth(Boolean isSetMonth, int cusMonth) {
		MONTH = isSetMonth;
		if (isCustomerDate) {
			month = cusMonth;
		}
	}
	/**
	 * 设置 月相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示月的选择项
	 */
	public void setMonth(Boolean isSetMonth) {
		MONTH = isSetMonth;
	}

	/**
	 * 设置 日相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示日的选择项
	 * @param cusYear
	 *            自定义默认显示的日
	 */
	public void setDay(Boolean isSetDay, int cusDay) {		
		DAY = isSetDay;
		if (isCustomerDate) {
			day = cusDay;
		}
	}
	/**
	 * 设置 日相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示日的选择项
	 */
	public void setDay(Boolean isSetDay) {
		DAY = isSetDay;
	}

	/**
	 * 设置 时相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示时的选择项
	 * @param cusYear
	 *            自定义默认显示的时
	 */
	public void setHour(Boolean isSetHour, int cusHour) {
		HOUR = isSetHour;
		if (isCustomerDate) {
			hour = cusHour;
		}
	}
	/**
	 * 设置 时相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示时的选择项	
	 */
	public void setHour(Boolean isSetHour) {
		HOUR = isSetHour;
	}

	/**
	 * 设置 分相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示分的选择项
	 * @param cusYear
	 *            自定义默认显示的分
	 */
	public void setMinute(Boolean isSetMinute, int cusMin) {
		MINUTES = isSetMinute;
		if (isCustomerDate) {
			minute = cusMin;
		}
	}
	/**
	 * 设置 分相关显示项
	 * 
	 * @param isSetYear
	 *            是否显示分的选择项	
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
	 * // 添加大小月月份并将其转换为list,方便之后的判断 String[] months_big = { "1", "3", "5", "7",
	 * "8", "10", "12" }; String[] months_little = { "4", "6", "9", "11" };
	 * 
	 * final List<String> list_big = Arrays.asList(months_big); final
	 * List<String> list_little = Arrays.asList(months_little); // 找到dialog的布局文件
	 * View dateview = inflater.inflate(R.layout.datepicker, null);
	 * 
	 * // 年 wv_year = (WheelView) dateview.findViewById(R.id.year);
	 * wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));//
	 * 设置"年"的显示数据 wv_year.setCyclic(true);// 可循环滚动 wv_year.setLabel("年");// 添加文字
	 * wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
	 * //wv_year.setVisibility(view.GONE); // 月 wv_month = (WheelView)
	 * dateview.findViewById(R.id.month); wv_month.setAdapter(new
	 * NumericWheelAdapter(1, 12)); wv_month.setCyclic(true);
	 * wv_month.setLabel("月"); wv_month.setCurrentItem(month);
	 * 
	 * 
	 * // 日 wv_day = (WheelView) dateview.findViewById(R.id.day);
	 * wv_day.setCyclic(true); // 判断大小月及是否闰年,用来确定"日"的数据 if
	 * (list_big.contains(String.valueOf(month + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(month + 1))) { wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 30)); } else { // 闰年 if ((year % 4 == 0 && year %
	 * 100 != 0) || year % 400 == 0) wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 29)); else wv_day.setAdapter(new
	 * NumericWheelAdapter(1, 28)); } wv_day.setLabel("日");
	 * wv_day.setCurrentItem(day - 1);
	 * 
	 * // 时
	 * 
	 * final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
	 * wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
	 * wv_hours.setCyclic(true); wv_hours.setCurrentItem(hour);
	 * 
	 * // 分 final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
	 * wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
	 * wv_mins.setCyclic(true); wv_mins.setCurrentItem(minute);
	 * 
	 * 
	 * // 添加"年"监听 OnWheelChangedListener wheelListener_year = new
	 * OnWheelChangedListener() { public void onChanged(WheelView wheel, int
	 * oldValue, int newValue) { int year_num = newValue + START_YEAR; //
	 * 判断大小月及是否闰年,用来确定"日"的数据 if (list_big
	 * .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 31)); } else if
	 * (list_little.contains(String.valueOf(wv_month .getCurrentItem() + 1))) {
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 30)); } else { if ((year_num
	 * % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 29)); else
	 * wv_day.setAdapter(new NumericWheelAdapter(1, 28)); } } }; // 添加"月"监听
	 * OnWheelChangedListener wheelListener_month = new OnWheelChangedListener()
	 * { public void onChanged(WheelView wheel, int oldValue, int newValue) {
	 * int month_num = newValue + 1; // 判断大小月及是否闰年,用来确定"日"的数据 if
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
	 * // 根据屏幕密度来指定选择器字体的大小 int textSize = 0;
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
	 * 从字符串转化成Date类型
	 * @param dt
	 *        日期字符串
	 * @param dateformat
	 *        转化的格式
	 * @return
	 *       返回Date类型
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
	 * 取消按钮的相关操作
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
	 * 确定按钮的相关操作
	 * 
	 * @param str
	 *            按钮显示的文字
	 * @param listener
	 *            按钮的监听事件
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
	 * 获得带星期格式的日期
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
	 * 获取时间
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
	 * 获得年
	 * 
	 * @return
	 */
	public String getYear() {
		return YEAR? wv_year.getCurrentItem() + START_YEAR + "":"";
	}

	/**
	 * 获得月
	 * 
	 * @return
	 */
	public String getMonth() {
		return MONTH? decimal.format((wv_month.getCurrentItem() + 1)):"";
	}

	/**
	 * 获得小时
	 * 
	 * @return
	 */
	public String getHour() {
		return HOUR?decimal.format(wv_hours.getCurrentItem()):"";
	}

	/**
	 * 获得天
	 * 
	 * @return
	 */
	public String getDay() {
		return DAY?decimal.format((wv_day.getCurrentItem() + 1)):"";
	}

	/**
	 * 获得分钟
	 * 
	 * @return
	 */
	public String getMin() {
		return MINUTES?decimal.format(wv_mins.getCurrentItem()):"";
	}

	/*
	 * 设置标题
	 * 
	 * @see android.app.Dialog#setTitle(java.lang.CharSequence)
	 */
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	/*
	 * 设置标题
	 * 
	 * @see android.app.Dialog#setTitle(int)
	 */
	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		super.setTitle(titleId);
	}

}

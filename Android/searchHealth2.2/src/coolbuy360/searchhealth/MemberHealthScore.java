package coolbuy360.searchhealth;

import java.util.HashMap;
import java.util.Map;

import coolbuy360.control.CustomerScrollView;
import coolbuy360.control.CustomerScrollView.onTurnListener;
import coolbuy360.control.RotateAnimation;
import coolbuy360.control.RotateAnimation.InterpolatedTimeListener;
import coolbuy360.control.ScoreChangePopup;
import coolbuy360.logic.User;
import coolbuy360.searchhealth.MemberLogin.loginbtnOnClick;
import coolbuy360.searchhealth.R.id;
import coolbuy360.searchhealth.R.layout;
import coolbuy360.searchhealth.R.string;
import coolbuy360.service.BuilderGestureExt;
import coolbuy360.service.CommandResult;
import coolbuy360.service.CommonMethod;
import coolbuy360.service.DisplayParams;
import coolbuy360.service.DisplayUtil;
import android.R.anim;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MemberHealthScore extends Activity implements
		InterpolatedTimeListener, onTurnListener {
	public static String tagString = "MemberHealthScore";
	private ImageView customer_scrollview_bg;
	private Button member_score_btn_check;
	private TextView member_score_txt_checkstate;
	private TextView member_score_text_total;
	private TextView member_score_text_detail;
	private CustomerScrollView customerScrollView;
	private GestureDetector gestureDetector;
	private Button member_score_btn_More;
	private View line_up;
	// private TableLayout tl_main;
	private int current_id = 0;
	PopupWindow popupWindow;
	private int todayScore = 0;
	public ProgressBar pBarLoading;
	/*
	 * private int drawable_id[] = { R.drawable.member_health_score_hole,
	 * R.drawable.member_health_score_hole_artist,
	 * R.drawable.member_health_hole_information };
	 */
	private int drawable_id[] = { R.drawable.score_uncheck,
			R.drawable.score_had_check };

	private int drawablebg_id[] = { R.drawable.score_header_bg,
			R.drawable.score_header_bg, R.drawable.score_header_bg,
			R.drawable.score_header_bg, R.drawable.score_header_bg,
			R.drawable.score_header_bg, R.drawable.score_header_bg,
			R.drawable.score_header_bg, R.drawable.score_header_bg,
			R.drawable.score_header_bg };

	protected void initView() {
		setContentView(R.layout.memberhealthscore);
		customerScrollView = (CustomerScrollView) findViewById(R.id.customerScrollView);
		customer_scrollview_bg = (ImageView) findViewById(R.id.customer_scrollview_bg);
		customer_scrollview_bg.setImageResource(drawablebg_id[GetRandom(8)]);
		line_up = (View) findViewById(R.id.line_up);
		member_score_btn_check = (Button) findViewById(R.id.member_score_btn_check);		
		//Log.i(tagString, tagString + "member_score_btn_check clickable= "+  member_score_btn_check.isClickable());
		member_score_btn_check.isClickable();
		member_score_txt_checkstate = (TextView) findViewById(R.id.member_score_txt_checkstate);
		member_score_text_total = (TextView) findViewById(R.id.member_score_text_val);
		String totalScore = User.getScore(this);
		if (totalScore == null && totalScore.equals("")) {
			member_score_text_total.setText("0");
		} else {
			member_score_text_total.setText(totalScore);
		}
		member_score_text_detail = (TextView) findViewById(R.id.member_score_text_detail);
		pBarLoading = (ProgressBar) findViewById(R.id.member_score_check_loading_progressbar);

		customerScrollView.setTurnListener(this);
		member_score_btn_More = (Button) this
				.findViewById(R.id.member_score_btn_More);
		member_score_btn_More.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MemberHealthScore.this,
						MemberEarnMoreScore.class);
				MemberHealthScore.this.startActivity(intent);
			}
		});

		
		TextView titleView = (TextView) this
				.findViewById(R.id.actionbar_page_title);
		titleView.setText(R.string.program_healthscore);
		ImageButton prebtnButton = (ImageButton) this
				.findViewById(R.id.actionbar_pre_btn);
		prebtnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				MemberHealthScore.this.finish();
			}
		});

		member_score_btn_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				new AsyncCheckHandle().execute();
			}
		});
		//Log.i(tagString, tagString + "member_score_btn_check clickable= "+  member_score_btn_check.isClickable());
		member_score_btn_check.setClickable(false);
		//Log.i(tagString, tagString + "member_score_btn_check clickable= "+  member_score_btn_check.isClickable());	
		member_score_text_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MemberHealthScore.this,
						MemberScoreLog.class);
				MemberHealthScore.this.startActivity(intent);
			}
		});
		/* 兑换礼品 */
		FrameLayout member_exchange_gif_layout = (FrameLayout) findViewById(R.id.member_exchange_gif_layout);
		member_exchange_gif_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(MemberHealthScore.this,
						MemberScoreExchange.class);
				MemberHealthScore.this.startActivity(intent);*/
			}
		});
	/*	member_exchange_gif_layout.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action =event.getAction();
				switch (action) {
				case MotionEvent.ACTION_MOVE:
					Log.i(tagString, tagString + "member_exchange_gif_layout: ACTION_MOVE:");
					break;
				case MotionEvent.ACTION_DOWN:
					Log.i(tagString, tagString + "member_exchange_gif_layout: ACTION_DOWN:");
					break;
				case MotionEvent.ACTION_UP:
					Log.i(tagString, tagString + "member_exchange_gif_layout: ACTION_UP:");
					break;
				default:
					break;
				}
				return false;
			}
		});*/
		
	/*	member_exchange_gif_layout.setOnTouchListener(new CommonMethod.setOnPressed());
		member_exchange_gif_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MemberHealthScore.this,
						MemberScoreExchange.class);
				MemberHealthScore.this.startActivity(intent);
			}
		});*/
		/* 兑换钱 */
		FrameLayout member_exchange_money_layout = (FrameLayout) findViewById(R.id.member_exchange_money_layout);
	/*	member_exchange_money_layout
				.setOnTouchListener(new CommonMethod.setOnPressed());*/
		member_exchange_money_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(MemberHealthScore.this,
						MemberScoreExchange.class);
				MemberHealthScore.this.startActivity(intent);*/
			}
		});
		/* 兑换活动 */
		FrameLayout member_exchange_activity_layout = (FrameLayout) findViewById(R.id.member_exchange_activity_layout);
/*		member_exchange_activity_layout
				.setOnTouchListener(new CommonMethod.setOnPressed());*/
		member_exchange_activity_layout
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						/*Intent intent = new Intent(MemberHealthScore.this,
								MemberScoreExchange.class);
						MemberHealthScore.this.startActivity(intent);*/

					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		customerScrollView.setImageView(customer_scrollview_bg);// 背景
		customerScrollView.setLine_up(line_up);
		new AsyncLoader_Refresh().execute();
		//GestureEvent();
	}

	@Override
	protected void onRestart() {
		// TODO 自动生成的方法存根
		Log.i(tagString, tagString + " onRestart");
		super.onRestart();
	}

	/**
	 * 改变签到过后的文字
	 * 
	 * @param textView
	 * @param dayString
	 * @param scoreString
	 */
	private void CheckValueState(TextView textView, String strsString) {

		int dayStartIndex = strsString.indexOf("到");
		int dayEndIndex = strsString.indexOf("天");
		int scoreStartIndex = strsString.indexOf("日");
		SpannableStringBuilder style = new SpannableStringBuilder(strsString);
		// style.setSpan(new
		// BackgroundColorSpan(Color.RED),5,7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.RED), dayStartIndex + 1,
				dayEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.RED), scoreStartIndex + 1,
				strsString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		textView.setText(style);
	}

	/**
	 * 显示积分变化弹窗
	 * 
	 * @param parentViewID
	 * @param value
	 */
	private void showScoreChange(int parentViewID, int value) {
		ScoreChangePopup popupWindow = new ScoreChangePopup(this, value);
		todayScore = value;
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO 自动生成的方法存根

				changeTotalScore(todayScore);
			}
		});
		popupWindow.showAtLocation(findViewById(parentViewID), Gravity.CENTER,
				0, 0);
		popupWindow.delayedDismiss();
	}

	public void changeTotalScore(int score) {
		int total = Integer.valueOf(
				member_score_text_total.getText().toString()).intValue();
		total += score;
		member_score_text_total.setText(total + "");

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.i(tagString, tagString + " onResume");
		// TODO 自动生成的方法存根
		customer_scrollview_bg.setImageResource(drawablebg_id[GetRandom(10)]);
		super.onResume();
	}

	@Override
	public void onTurn() {
		RotateAnimation animation = new RotateAnimation();
		animation.setFillAfter(true);
		animation.setInterpolatedTimeListener(this);
		animation.setRepeatCount(-1);
		member_score_btn_check.startAnimation(animation);
		// current_id = current_id < drawable_id.length - 1 ? ++current_id : 0;
		// current_id=0;
		new AsyncLoader_Refresh().execute();
	}

	@Override
	public void interpolatedTime(float interpolatedTime) {
		// 监听到翻转进度过半时，更新图片内容，
		if (interpolatedTime > 0.5f) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					drawable_id[current_id]);
			// member_score_btn_check.setImageBitmap(bitmap);
			member_score_btn_check
					.setBackgroundResource(drawable_id[current_id]);
		}
	}

	/**
	 * 获取从0开始到maxint-1的随机数
	 * 
	 * @param maxint
	 * @return
	 */
	private int GetRandom(int maxint) {
		int n = 0;
		for (int i = 0; i < 30; i++) {
			n = 1 + (int) (Math.random() * maxint);
		}
		return n - 1;
	}

	/**
	 * 签到操作 请求签到过程<br/>
	 * 若签到成功<br/>
	 * 返回[result],[message],[addscore],[score],[checkintimes],[nextscore],[today
	 * ];<br/>
	 * today表示今天是否签到的状态，1为已签到，0为未签到<br/>
	 * 若签到失败<br/>
	 * 返回[result],[message]键值。
	 */
	private class AsyncCheckHandle extends AsyncTask<String, Void, Boolean> {
		Map<String, String> innerMap = new HashMap<String, String>();
		CommandResult commandResult = new CommandResult(false, "签到失败，未知错误");

		@Override
		protected void onPreExecute() {
			// TODO 自动生成的方法存根
			showLoading();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO 自动生成的方法存根
			try {
				User user = new User(MemberHealthScore.this);
				commandResult = user.checkIn();
				if (commandResult.getResult()) {
					innerMap = commandResult.getOriginalResult();
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				commandResult.setResult(false);
				commandResult.setMessage("系统服务器错误");
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			cancelLoading();
			member_score_btn_check.clearAnimation();
			if (result) {
				int addscore = Integer.valueOf(innerMap.get("addscore"))
						.intValue();
				int checkintimes = Integer
						.valueOf(innerMap.get("checkintimes")).intValue();
				int nextscore = Integer.valueOf(innerMap.get("nextscore"))
						.intValue();
				// showPopupWindow(addscore+"");
				showScoreChange(R.id.member_score_btn_check, addscore);
				String strsString = "已连续签到" + checkintimes + "天 明日+"
						+ nextscore;
				CheckValueState(member_score_txt_checkstate, strsString);
				member_score_btn_check.setClickable(false);
				current_id = 1;
				member_score_btn_check
						.setBackgroundResource(drawable_id[current_id]);
			} else {
				Toast.makeText(MemberHealthScore.this,
						commandResult.getMessage(), 1).show();
			}
		}

	}

	/**
	 * 获取签到状态
	 * 
	 * @author Administrator //请求签到过程
	 *         //若获取成功，返回[score],[checkintimes],[nextscore],[today];
	 *         //若获取失败，返回[message]键值。
	 */
	private class AsyncLoader_Refresh extends AsyncTask<String, Void, Boolean> {
		Map<String, String> innerMap = new HashMap<String, String>();
		CommandResult commandResult = new CommandResult(false, "签到失败，未知错误");

		@Override
		protected void onPreExecute() {
			// TODO 自动生成的方法存根
			showLoading();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO 自动生成的方法存根
			try {
				User user = new User(MemberHealthScore.this);
				commandResult = user.getCheckInState();
				if (commandResult.getResult()) {
					innerMap = commandResult.getOriginalResult();
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				commandResult.setResult(false);
				commandResult.setMessage("系统服务器错误");
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			cancelLoading();
			member_score_btn_check.clearAnimation();
			if (result) {
				int totalscore = Integer.valueOf(innerMap.get("score"))
						.intValue();
				int checkintimes = Integer
						.valueOf(innerMap.get("checkintimes")).intValue();
				int nextscore = Integer.valueOf(innerMap.get("nextscore"))
						.intValue();
				int today = Integer.valueOf(innerMap.get("today")).intValue();

				if (today == 0) {
					// showPopupWindow(addscore+"");
					String strsString = "已连续签到" + checkintimes + "天 今日+"
							+ nextscore;
					CheckValueState(member_score_txt_checkstate, strsString);
					current_id = 0;
					member_score_btn_check
							.setBackgroundResource(drawable_id[current_id]);
					member_score_btn_check.setClickable(true);
					member_score_text_total.setText(totalscore + "");
				} else {
					String strsString = "已连续签到" + checkintimes + "天 明日+"
							+ nextscore;
					CheckValueState(member_score_txt_checkstate, strsString);
					current_id = 1;
					member_score_btn_check
							.setBackgroundResource(drawable_id[current_id]);
					member_score_btn_check.setClickable(false);
					member_score_text_total.setText(totalscore + "");
				}
			} else {
				Toast.makeText(MemberHealthScore.this,
						commandResult.getMessage(), 1).show();
			}
		}

	}

	public void showLoading() {
		if (pBarLoading != null) {
			pBarLoading.setVisibility(View.VISIBLE);
		}
	}

	public void cancelLoading() {
		pBarLoading.setVisibility(View.GONE);
	}	

/*	private void GestureEvent() {
		gestureDetector = new BuilderGestureExt(this,
				new BuilderGestureExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {
						if (direction == 2 || direction == 3) {
							// show(Integer.toString(direction));
							MemberHealthScore.this.finish();
							overridePendingTransition(R.anim.push_no,
									R.anim.push_right_out);
						}

					}
				}).Buile();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);

	}*/

}

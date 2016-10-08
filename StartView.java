package com.changel.test_calculator;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.changel.test_calculator.dao.ICalculator;
import com.changel.test_calculator.db.Db;
import com.changel.test_calculator.factory.CalculatorFactory;
import com.changel.test_calculator.type.Item;
import com.changel.test_calculator.type.Types;
import com.changel.test_calculator.R;

@SuppressLint("NewApi")
public class StartView extends BaseActivity implements OnClickListener {

	private TextView tvmsg;
	private List<Item> items = new ArrayList<Item>();
	private long lastClickTime = 0;

	private Db db;
	private SQLiteDatabase dbRead, dbWrite, delete;
	private SimpleCursorAdapter adapter;
	private Vibrator vibrator;
	private View mMoreView;
	private ImageView mMoreImage;
	private View mMoreMenuView;
	private View MsgView;
	private View keybroast;
	private Button changeTheme;
	private Button dataDelete;
	private boolean mShowMenu = false;
	private static boolean flag = false;
	public double pi=4*Math.atan(1);  

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_view);
		// 获得震动服务
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// 取得历史记录按钮
		mMoreView = findViewById(R.id.more);
		MsgView = findViewById(R.id.MsgView);
		// 取得键界面
		keybroast = findViewById(R.id.keybroast);
		mMoreMenuView = findViewById(R.id.listview);
		mMoreImage = (ImageView) findViewById(R.id.more_image);
		tvmsg = (TextView) findViewById(R.id.msg);
		changeTheme = (Button) findViewById(R.id.changeTheme);
		dataDelete = (Button) findViewById(R.id.dataDelete);

		mMoreView.setOnClickListener(this);
		findViewById(R.id.changeTheme).setOnClickListener(this);
		findViewById(R.id.zero).setOnClickListener(this);
		findViewById(R.id.one).setOnClickListener(this);
		findViewById(R.id.two).setOnClickListener(this);
		findViewById(R.id.three).setOnClickListener(this);
		findViewById(R.id.four).setOnClickListener(this);
		findViewById(R.id.five).setOnClickListener(this);
		findViewById(R.id.six).setOnClickListener(this);
		findViewById(R.id.seven).setOnClickListener(this);
		findViewById(R.id.eight).setOnClickListener(this);
		findViewById(R.id.nine).setOnClickListener(this);
		findViewById(R.id.C).setOnClickListener(this);
		findViewById(R.id.jian).setOnClickListener(this);
		findViewById(R.id.cheng).setOnClickListener(this);
		findViewById(R.id.chu).setOnClickListener(this);
		findViewById(R.id.jia).setOnClickListener(this);
		findViewById(R.id.point).setOnClickListener(this);
		findViewById(R.id.equal).setOnClickListener(this);
		findViewById(R.id.zhengOrfu).setOnClickListener(this);
		findViewById(R.id.delete).setOnClickListener(this);
		findViewById(R.id.dataDelete).setOnClickListener(this);
		findViewById(R.id.sin).setOnClickListener(this);
		findViewById(R.id.cos).setOnClickListener(this);
		findViewById(R.id.tan).setOnClickListener(this);
		findViewById(R.id.factorial).setOnClickListener(this);
		findViewById(R.id.ln).setOnClickListener(this);
		findViewById(R.id.log).setOnClickListener(this);
		findViewById(R.id.power).setOnClickListener(this);
		findViewById(R.id.sqrt).setOnClickListener(this);
		// 列表项长按监听器
		getListView().setOnItemLongClickListener(listViewItemLongClickListener);
		// 数据库操作
		db = new Db(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();
		delete = db.getWritableDatabase();
		// 实例化历史记录列表
		adapter = new SimpleCursorAdapter(this, R.layout.user_list_cell, null,
				new String[] { "content" }, new int[] { R.id.tvContent });
		setListAdapter(adapter);
		// 刷新列表
		refreshListView();
		showMoreView(!mShowMenu);

		if (flag) {
			changeTheme.setText("夜间");
			flag = false;
		} else {
			changeTheme.setText("日光");
			flag = true;
		}

	}

	private void refreshListView() {
		Cursor c = dbRead.query("user", null, null, null, null, null, null);
		adapter.changeCursor(c);
	}

	// 设置历史记录
	public void showMoreView(boolean bShow) {
		if (bShow) {
			mMoreMenuView.setVisibility(View.GONE);
			dataDelete.setVisibility(View.GONE);
			MsgView.setVisibility(View.VISIBLE);
			keybroast.setVisibility(View.VISIBLE);

			mMoreImage.setImageResource(R.drawable.login_more_up);
			mShowMenu = true;
		} else {
			mMoreMenuView.setVisibility(View.VISIBLE);
			dataDelete.setVisibility(View.VISIBLE);
			MsgView.setVisibility(View.GONE);
			keybroast.setVisibility(View.GONE);
			mMoreImage.setImageResource(R.drawable.login_more);
			mShowMenu = false;
		}
	}

	int key = 0;
	double sum = 0;
	String str = null;
	StringBuffer sf = new StringBuffer();
	Types t;
	String[] s;
	double result,x,y;
	int j = 1,k=0;
	int i,fac=0,Fac=1,z=0;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dataDelete:
			new AlertDialog.Builder(StartView.this)
					.setTitle("提醒")
					.setMessage("您确定要清除所有记录吗?")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									delete.delete("user", null, null);
									refreshListView();
								}
							}).show();
			// refreshListView();
			// delete.close();
			break;
		case R.id.more:
			showMoreView(!mShowMenu);
			break;
		case R.id.zero:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.0 + "");
				key = 0;
			} else{
				if(k==1) 
				tvmsg.setText("0");
				else
				tvmsg.append("0");
			}
			j = 1;
			break;

		case R.id.one:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.1 + "");
			} else{
				if(k==1&&z==0) 
				tvmsg.setText("1");
				else
				tvmsg.append("1");
			}
			j = 1;
			break;

		case R.id.two:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.2 + "");
				key = 0;
			} else{
				if(k==1&&z==0) 
				tvmsg.setText("2");
				else
				tvmsg.append("2");
			}
			j = 1;
			break;

		case R.id.three:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.3 + "");
				key = 0;
			} else{
				if(k==1&&z==0) 
				tvmsg.setText("3");
				else
				tvmsg.append("3");
			}
			j = 1;
			break;

		case R.id.four:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.4 + "");
				key = 0;
			} else{
				if(k==1&&z==0) 
				tvmsg.setText("4");
				else
				tvmsg.append("4");
			}
			j = 1;
			break;
		case R.id.five:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.5 + "");
				key = 0;
			} else{
				if(k==1&&z==0) 
				tvmsg.setText("5");
				else
				tvmsg.append("5");
			}
			j = 1;
			break;

		case R.id.six:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.6 + "");
				key = 0;
			} else{
				if(k==1&&z==0) 
				tvmsg.setText("6");
				else
				tvmsg.append("6");
			}
			j = 1;
			break;

		case R.id.seven:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.7 + "");
				key = 0;
			}else{
				if(k==1&&z==0) 
				tvmsg.setText("7");
				else
				tvmsg.append("7");
			}
			j = 1;
			break;
		case R.id.eight:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.8 + "");
				key = 0;
			}else{
				if(k==1&&z==0) 
				tvmsg.setText("8");
				else
				tvmsg.append("8");
			}
			j = 1;
			break;
		case R.id.nine:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			if (key == 1) {
				tvmsg.setText(sum + 0.9 + "");
				key = 0;
			}else{
				if(k==1&&z==0) 
				tvmsg.setText("9");
				else
				tvmsg.append("9");
			}
			j = 1;
			break;
		// 清除功能
		case R.id.C:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			tvmsg.setText("");
			str = "";
			j = 1;
			items.clear();
			break;
		case R.id.cheng:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			// j 的意义是判断是不是重复输入同一个运算符
			if (j == 1) {
				if (s != null) {
					// 将str的数字分离出来，存储在s数组里
					s = str.split("\\+|\\-|\\*|\\÷");
					// 如果数组长度大于1，则说明是两个数字以上的运算，下面将其前面两个数执行一次运算。
					if (s.length > 1) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
						ICalculator calculator = CalculatorFactory
								.getCalculator(t);
						result = calculator.calculate(items, sf);
						// 将之前的容器清空，再将结果放如容器
						items.clear();
						items.add(new Item(result));
					}
					// 或者数组长度小于等于1，说明是第一个数将继续运算
				} else if (!str.equals("")) {
					System.out.println(str);
					s = str.split("\\×");
					if (s.length != 0) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
					}
				}
				// 设置运算类型，通过CalculatorFactory来识别是哪种运算
				t = Types.CHENG;
				tvmsg.setText(str + "×");
			}
			j++;
			break;
		// 以下运算类似于上面
		case R.id.jia:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			if (j == 1) {
				if (s != null) {
					s = str.split("\\+|\\-|\\×|\\÷");
					if (s.length > 1) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
						ICalculator calculator = CalculatorFactory
								.getCalculator(t);
						result = calculator.calculate(items, sf);
						System.out.println(result);
						items.clear();
						items.add(new Item(result));

					}

				} else if (!str.equals("")) {
					System.out.println(str);
					s = str.split("\\+");
					if (s.length != 0) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
					}
				}
				t = Types.JIA;
				tvmsg.setText(str + "+");
			}
			j++;
			break;
		case R.id.chu:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			if (j == 1) {
				if (s != null) {
					s = str.split("\\+|\\-|\\×|\\÷");
					if (s.length > 1) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
						ICalculator calculator = CalculatorFactory
								.getCalculator(t);
						result = calculator.calculate(items, sf);
						System.out.println(result);
						items.clear();
						items.add(new Item(result));

					}

				} else if (!str.equals("")) {
					System.out.println(str);
					s = str.split("\\÷");
					if (s.length != 0) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
					}
				}
				t = Types.CHU;
				tvmsg.setText(str + "÷");
			}
			j++;
			break;

		case R.id.jian:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			if (j == 1) {
				if (s != null) {
					s = str.split("\\+|\\-|\\×|\\÷");
					if (s.length > 1) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
						ICalculator calculator = CalculatorFactory
								.getCalculator(t);
						result = calculator.calculate(items, sf);
						System.out.println(result);
						items.clear();
						items.add(new Item(result));

					}

				} else if (!str.equals("")) {
					System.out.println(str);
					s = str.split("\\-");
					if (s.length != 0) {
						items.add(new Item(Double.parseDouble(s[s.length - 1])));
					}
				}
				t = Types.JIAN;
				tvmsg.setText(str + "-");
			}
			j++;
			break;
	
		case R.id.equal:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			s = str.split("\\+|\\-|\\×|\\÷");
			// 如果没有输入数字就直接按等于，则全部清空，便于下次运算。
			if (str.equals("")) {
				tvmsg.setText("");
				items.clear();
				str = null;
				s = null;
				break;
			}
			// 如果输入最后一个数字是浮点型或者整型都能运算
			if (s.length != 0
					&& (s[s.length - 1].matches("^\\d{1,10}+\\.\\d{1,8}$") || s[s.length - 1]
							.matches("\\d{1,10}"))) {
				// 为防止连续按下运算符号键导致显示重复和运算错误。
				if(k==1){
					y=Double.parseDouble(tvmsg.getText().toString());
					tvmsg.setText(Math.pow(x, y)+"");
					k=0;
					z=0;
				}
				else{
				if (!tvmsg.getText().toString().endsWith("+")
						&& !tvmsg.getText().toString().endsWith("-")
						&& !tvmsg.getText().toString().endsWith("×")
						&& !tvmsg.getText().toString().endsWith("÷")) {
					items.add(new Item(Double.parseDouble(s[s.length - 1])));
					ICalculator calculator = CalculatorFactory.getCalculator(t);
					result = calculator.calculate(items, sf);
					tvmsg.setText(result + "");
					
				} else
					tvmsg.setText(result + "");
				// 将运算过程写入数据库
				if (sf.length() != 0) {
					ContentValues cv = new ContentValues();
					cv.put("content", str + " = " + result);
					dbWrite.insert("user", null, cv);
					refreshListView();
					sf.delete(0, sf.length());
				}
				items.clear();
				str = null;
				s = null;
				}
			}
			break;
		// 小数点
		case R.id.point:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			String[] q = tvmsg.getText().toString().split("\\+|\\-|\\×|\\÷");
			// 判断最后一个数组元素是否为空，不为空再判断，这个数里面有没有小数点（同一个数不可能有两个小数点）
			if (q[q.length - 1].length() != 0
					&& q[q.length - 1].indexOf(".") == -1) {
				tvmsg.append(".");
			}
            z=1;
			break;
		case R.id.sin:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				tvmsg.setText((Math.sin(Double.parseDouble(str)
						)) + "");
			}
		
			j++;
			break;
		case R.id.cos:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				tvmsg.setText((Math.cos(Double.parseDouble(str)
						)) + "");
			}
		
			j++;
			break;
		case R.id.tan:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				tvmsg.setText((Math.tan(Double.parseDouble(str)
						)) + "");
			}
		
			j++;
			break;
		case R.id.ln:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				tvmsg.setText((Math.log(Double.parseDouble(str)
						)) + "");
			}
		
			j++;
			break;
		case R.id.log:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				tvmsg.setText((Math.log10(Double.parseDouble(str)
						)) + "");
			}
		
			j++;
			break;
		case R.id.sqrt:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				if(Math.sqrt(Double.parseDouble(str))>0||Math.sqrt(Double.parseDouble(str))==0){
				tvmsg.setText((Math.sqrt(Double.parseDouble(str)
						)) + "");
				}
				else{
			    tvmsg.setText("负数没有平方根");;		
				}
			}
		
			j++;
			break;
		case R.id.factorial:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				fac=Integer.parseInt(str);
				if(fac==0)  
					tvmsg.setText(1+"");
				else{
					for(i=1;i<=fac;i++){
					Fac*=i;
			
					tvmsg.setText(Fac+"");
					}
				}
			}
		    fac=0;
		    Fac=1;
			j++;
			break;
		case R.id.power:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
	
			if (str.length() != 0) {
				x=Double.parseDouble(str);
				tvmsg.setText("^");
				k++;
			}
	        j++; 
			break;
		case R.id.zhengOrfu:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			if (str.length() != 0) {
				tvmsg.setText((0 - Double.parseDouble(tvmsg.getText()
						.toString())) + "");
			}
			break;
		// 从后面逐个删除
		case R.id.delete:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			str = tvmsg.getText().toString();
			if (str.length() != 0) {
				tvmsg.setText(str.substring(0, str.length() - 1));
				j = 1;
			}
			break;
		// 主题设置
		case R.id.changeTheme:
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(60);
			}
			PreferenceHelper.setTheme(this,
					mTheme == R.style.AppTheme ? R.style.AppTheme_Another
							: R.style.AppTheme);
			reload();
			// flag = false;

			break;

		}
	}

	// 长按列表项的设置
	private OnItemLongClickListener listViewItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {

			new AlertDialog.Builder(StartView.this)
					.setTitle("提醒")
					.setMessage("您确定要删除该项吗")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Cursor c = adapter.getCursor();
									c.moveToPosition(position);

									int itemId = c.getInt(c
											.getColumnIndex("_id"));
									dbWrite.delete("user", "_id=?",
											new String[] { itemId + "" });
									refreshListView();
								}
							}).show();

			return true;
		}
	};

	// 实现双击退出程序
	public void onBackPressed() {

		if (lastClickTime <= 0) {
			Toast.makeText(this, "再按一次后退键退出应用", Toast.LENGTH_SHORT).show();
			lastClickTime = System.currentTimeMillis();
		} else {
			long currentClickTime = System.currentTimeMillis();
			if (currentClickTime - lastClickTime < 1500) {
				flag = false;
				finish();
			} else {
				Toast.makeText(this, "再按一次后退键退出应用", Toast.LENGTH_SHORT).show();
				lastClickTime = currentClickTime;
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "你已经进入设置选项", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_close:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

}

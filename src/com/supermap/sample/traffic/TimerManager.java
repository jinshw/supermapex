package com.supermap.sample.traffic;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 任务管理
 * 
 * @author fengdl
 *
 */
public class TimerManager {
	//是否执行定时器
	public static boolean IS_EXE = true;

	//timer不管上次是否执行完，隔几秒执行一次

	/**
	 * <h4>任务管理构造函数初始化时间线程<p>at 2016-6-16</p></h4>
	 * 
	 * @param h 小时
	 * @param m 分钟
	 * @param s 秒钟
	 * @param period 时间间隔，毫秒  [24 * 60 * 60 * 1000 =1天]
	 * @param isNowRun 是否立刻执行
	 * @param isAddPeriod 
	  					<p>当isNowRun为false时，设置开始执行的时间是否是间隔时间后开始执行，否则则是隔天定时执行</P>
	  					<p>当isNowRun为true时，参数失效</P>
	 * @param task 执行的任务TimerTask
	 */
	public TimerManager(int h, int m, int s, long period, Boolean isNowRun, Boolean isAddPeriod, TimerTask task) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, h); //凌晨1点
		calendar.set(Calendar.MINUTE, m);
		calendar.set(Calendar.SECOND, s);
		Date date = calendar.getTime(); //第一次执行定时任务的时间

		//如果第一次执行定时任务的时间 小于当前的时间
		//此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if (!isNowRun) {
			if (date.before(new Date())) {
				if (isAddPeriod) {
					//间隔时间后开始执行
					date = new Date(System.currentTimeMillis() + period);
				} else {
					//定时在几日的几点开始执行
					date = this.addDay(date, 1);
				}
			}
		}

		//启动任务
		Timer timer = new Timer();
		timer.schedule(task, date, period);
	}

	// 增加或减少天数
	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

}

/**
 * Copyright 2017 kubotaku1119 <kubotaku1119@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kubotaku.android.code4kyoto5374.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kubotaku.android.code4kyoto5374.data.GarbageCollectDay;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DAY_OF_WEEK_IN_MONTH;

/**
 * Utilities.
 */

public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    /**
     * Assets下の指定されたテキストファイルを読み込む
     *
     * @param context  コンテキスト.
     * @param fileName 対象ファイル名
     * @return テキストの内容
     */
    @NonNull
    public static String readFileFromAssets(Context context, String fileName) {

        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            try {
                is = context.getAssets().open(fileName);
                br = new BufferedReader(new InputStreamReader(is));

                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 曜日文字列から{@link Calendar#DAY_OF_WEEK}を返す
     *
     * @param src 曜日文字列
     * @return
     */
    public static int parseDayOfWeek(@NonNull String src) {
        switch (src) {
            case "日":
                return Calendar.SUNDAY;
            case "月":
                return Calendar.MONDAY;
            case "火":
                return Calendar.TUESDAY;
            case "水":
                return Calendar.WEDNESDAY;
            case "木":
                return Calendar.THURSDAY;
            case "金":
                return Calendar.FRIDAY;
            case "土":
                return Calendar.SATURDAY;
        }
        return 0; // error
    }

    /**
     * {@link Calendar#DAY_OF_WEEK}から文字列を返す
     *
     * @param dayOfWeek
     * @return
     */
    public static String convertDayOfWeekText(final int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "日";
            case 2:
                return "月";
            case 3:
                return "火";
            case 4:
                return "水";
            case 5:
                return "木";
            case 6:
                return "金";
            case 7:
                return "土";
        }
        return ""; // error
    }

    /**
     * N日後をUI表示用に変換する
     *
     * @param daysAfter N日後
     * @return 変換後の文字列
     */
    public static String convertDaysAfterText(int daysAfter) {
        switch (daysAfter) {
            case 0:
                return "今日";

            case 1:
                return "明日";

            case 2:
                return "明後日";
        }
        return "" + daysAfter + "日後";
    }

    /**
     * 指定されたごみ収集日のもっとも近い日(N日後)を取得する
     *
     * @param daysList 収集日リスト
     * @return N日後
     */
    public static int calcNearestDaysAfter(List<GarbageCollectDay.GarbageDaysForViews> daysList, final int targetHour, final int targetMinute, final boolean ignoreToday) {

        boolean beforeTargetTime = isBeforeTargetTime(targetHour, targetMinute);
        if (ignoreToday) {
            beforeTargetTime = false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        final Date today = calendar.getTime();
        final long todayMillis = today.getTime();

        int nearestDaysAfter = 60;
        for (GarbageCollectDay.GarbageDaysForViews days : daysList) {

            // clear calendar instance.
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            int day = days.day;
            int week = days.week;

            int diff;

            // set target date
            calendar.set(Calendar.DAY_OF_WEEK, day);
            if (week != -1) {
                calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, week);
            }
            Date targetDate = calendar.getTime();
            long targetDateMillis = targetDate.getTime();

            long diffMillis = targetDateMillis - todayMillis;
            if ((diffMillis > 0) || ((diffMillis == 0) && beforeTargetTime)) {
                // today(current time) is before target date(and target time).
                diff = (int) (diffMillis / (24 * 60 * 60 * 1000));
            } else {
                // today(current time) is after target date(and target time)
                // set next garbage date.
                if (week != -1) {
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_WEEK, day);
                    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, week);
                } else {
                    calendar.add(Calendar.WEEK_OF_MONTH, 1);
                    calendar.set(Calendar.DAY_OF_WEEK, day);
                }
                targetDate = calendar.getTime();
                targetDateMillis = targetDate.getTime();
                diffMillis = targetDateMillis - todayMillis;
                diff = (int) (diffMillis / (24 * 60 * 60 * 1000));
            }

            if ((diff >= 0) && (nearestDaysAfter > diff)) {
                nearestDaysAfter = diff;
            }
        }

        return nearestDaysAfter;
    }

    private static boolean isBeforeTargetTime(final int targetHour, final int targetMinute) {
        final Calendar instance = Calendar.getInstance();
        final int currentH = instance.get(Calendar.HOUR_OF_DAY);
        final int currentM = instance.get(Calendar.MINUTE);

        if (currentH < targetHour) {
            return true;
        } else if ((currentH == targetHour) && (currentM <= targetMinute)) {
            return true;
        }

        return false;
    }

    public static String createGarbageCollectDaysText(List<GarbageCollectDay.GarbageDaysForViews> daysList, int nearestDaysAfter) {
        String text = "";
        for (GarbageCollectDay.GarbageDaysForViews days : daysList) {
            text += days.toViewString();
            text += " ";
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, nearestDaysAfter);
        text += calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        return text;
    }

}

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
import android.util.Log;

import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.AreaMaster;
import com.kubotaku.android.code4kyoto5374.util.github.CommitHistory;
import com.kubotaku.android.code4kyoto5374.util.github.DataSourceAccessor;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * 地域別ごみ収集日情報のリーダークラス
 * <p>
 * GitHub上のCSVファイルからデータ取得する
 * </p>
 */
public class AreaDataReader {

    private static final String GITHUB_OWNER = "ofuku3f";

    private static final String GITHUB_REPO = "5374osaka.github.com";

    private static final String GITHUB_BRANCH = "gh-pages";

    private static final String GITHUB_AREA_DAYS = "data/area_days.csv";

    private static final String GITHUB_AREA_MASTER = "data/area_master.csv";

    private List<AreaMaster> areaMasterList;

    private List<AreaDays> areaDaysList;

    /**
     * GitHubのリポジトリが更新されているかチェックする
     *
     * @return 更新有無
     */
    public static boolean checkUpdate(Context context) {
        boolean updated = false;

        try {
            final String latestUpdate = Prefs.loadGitHubLatestUpdate(context);

            final String latestSHA = Prefs.loadGitHubLatestSHA(context);

            final List<CommitHistory> commitHistory
                    = DataSourceAccessor.getCommitHistory(GITHUB_OWNER, GITHUB_REPO, latestUpdate);

            if ((commitHistory != null) && (commitHistory.size() != 0)) {
                // 1件以上取得できる
                // 1件目のSHAが保存済みのものと異なっていれば更新ありとみなす
                final CommitHistory latestCommit = commitHistory.get(0);
                if (!latestCommit.sha.equals(latestSHA)) {
                    updated = true;

                    // 情報更新
                    Prefs.saveGitHubLatestUpdate(context, latestCommit.commit.author.date);
                    Prefs.saveGitHubLatestSHA(context, latestCommit.sha);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            updated = false;
        }

        return updated;
    }

    /**
     * GitHub上のCSVファイルから地域データを取得
     *
     * @return 成否
     */
    public boolean importAreaData() {

        boolean ret = true;

        try {
            // 地域マスター情報
            String areaMasterContent = DataSourceAccessor.readFileFromGitHub(GITHUB_OWNER, GITHUB_REPO, GITHUB_BRANCH, GITHUB_AREA_MASTER);
            String[] sepAreaMaster = areaMasterContent.split("\n");

            areaMasterList = new ArrayList<>();
            for (int index = 1; index < sepAreaMaster.length; index++) { // 1行目はヘッダー
                final AreaMaster areaMaster = AreaMaster.create(sepAreaMaster[index]);
                areaMasterList.add(areaMaster);
            }

            // 区域別ごみ収集日情報
            String areaDaysContent = DataSourceAccessor.readFileFromGitHub(GITHUB_OWNER, GITHUB_REPO, GITHUB_BRANCH, GITHUB_AREA_DAYS);
            String[] sepAreaDays = areaDaysContent.split("\n");

            areaDaysList = new ArrayList<>();
            for (int index = 1; index < sepAreaDays.length; index++) { // 1行目はヘッダー
                final AreaDays areaDays = AreaDays.create(sepAreaDays[index]);
                if (areaDays != null) {
                    areaDaysList.add(areaDays);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    /**
     * 取得した地域データをローカルのDBに保存する
     *
     * @return
     */
    public boolean saveAreaData() {
        boolean ret = true;

        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            realm.copyToRealm(areaMasterList);
            realm.copyToRealm(areaDaysList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            realm.close();
        }

        return ret;
    }

}

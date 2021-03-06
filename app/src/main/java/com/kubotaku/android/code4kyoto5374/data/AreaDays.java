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
package com.kubotaku.android.code4kyoto5374.data;

import com.kubotaku.android.code4kyoto5374.util.AppUtil;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * ごみの収集日の地区別情報クラス
 */
public class AreaDays extends RealmObject {

    /**
     * エリアマスタのID
     */
    public int masterAreaID;

    /**
     * 地区名
     */
    public String areaName;

    /**
     * 収集センター名称
     */
    public String centerName;

    /**
     * 燃やすごみの日
     */
    public RealmList<GarbageDays> burnableDays;

    /**
     * 缶、ビン、ペットボトルの日
     */
    public RealmList<GarbageDays> binDays;

    /**
     * プラごみの日
     */
    public RealmList<GarbageDays> plasticDays;

    /**
     * 小型金属の日
     */
    public RealmList<GarbageDays> smallDays;

    public int getMasterAreaID() {
        return masterAreaID;
    }

    public void setMasterAreaID(int masterAreaID) {
        this.masterAreaID = masterAreaID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public RealmList<GarbageDays> getBurnableDays() {
        return burnableDays;
    }

    public void setBurnableDays(RealmList<GarbageDays> burnableDays) {
        this.burnableDays = burnableDays;
    }

    public RealmList<GarbageDays> getBinDays() {
        return binDays;
    }

    public void setBinDays(RealmList<GarbageDays> binDays) {
        this.binDays = binDays;
    }

    public RealmList<GarbageDays> getPlasticDays() {
        return plasticDays;
    }

    public void setPlasticDays(RealmList<GarbageDays> plasticDays) {
        this.plasticDays = plasticDays;
    }

    public RealmList<GarbageDays> getSmallDays() {
        return smallDays;
    }

    public void setSmallDays(RealmList<GarbageDays> smallDays) {
        this.smallDays = smallDays;
    }

    /**
     * 指定したごみ種別の収集日リストを取得する
     *
     * @param garbageType ごみ種別
     * @return 収集日リスト
     */
    public List<GarbageDays> getTargetGarbageDays(final int garbageType) {
        switch (garbageType) {
            default:
                return null;

            case GarbageType.TYPE_BURNABLE:
                return burnableDays;

            case GarbageType.TYPE_PLASTIC:
                return plasticDays;

            case GarbageType.TYPE_SMALL:
                return smallDays;

            case GarbageType.TYPE_BIN:
                return binDays;
        }
    }

    public String toInfoString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("燃やすごみ : ");
        for (GarbageDays d : burnableDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n\n");

        sb.append("缶・ビン・ペットボトル : ");
        for (GarbageDays d : binDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n\n");

        sb.append("プラスチック製容器包装 : ");
        for (GarbageDays d : plasticDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n\n");

        sb.append("小型金属類 : ");
        for (GarbageDays d : smallDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(areaName);
        sb.append("\n");
        sb.append("  マスターID : " + masterAreaID);
        sb.append("\n");
        sb.append("  収集センター : " + centerName);
        sb.append("\n");
        sb.append("  燃やすごみ : ");
        for (GarbageDays d : burnableDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        sb.append("  缶、ビン、ペットボトル : ");
        for (GarbageDays d : binDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        sb.append("  プラごみ : ");
        for (GarbageDays d : plasticDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        sb.append("  資源ごみ : ");
        for (GarbageDays d : smallDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * ごみ収集の地区情報クラスを生成する
     *
     * @param src 元データ
     * @return
     */
    public static AreaDays create(String src) {
        try {
            final AreaDays areaDays = new AreaDays();

            String[] sepEntity = src.split(",");

            areaDays.masterAreaID = Integer.parseInt(sepEntity[0]);
            areaDays.areaName = sepEntity[1];
            areaDays.centerName = sepEntity[2];

            areaDays.burnableDays = parseGarbageDays(sepEntity[3]);
            areaDays.binDays = parseGarbageDays(sepEntity[4]);
            areaDays.plasticDays = parseGarbageDays(sepEntity[5]);
            areaDays.smallDays = parseGarbageDays(sepEntity[6]);

            return areaDays;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 各ごみの収集日をパースする
     * <p>
     * 元データは「月1 木3」のような形式
     * </p>
     *
     * @param src 元データ
     * @return
     */
    private static RealmList<GarbageDays> parseGarbageDays(String src) {
        RealmList<GarbageDays> garbageDays = new RealmList<>();

        String[] sepEntity = src.split(" ");
        for (String entity : sepEntity) {
            entity = entity.replace("\r", "");
            final int length = entity.length();
            switch (length) {
                case 1: {
                    final GarbageDays days = new GarbageDays();
                    days.week = -1;
                    days.day = AppUtil.parseDayOfWeek(entity.substring(0, 1));
                    garbageDays.add(days);
                }
                break;

                case 2: {
                    final GarbageDays days = new GarbageDays();
                    days.week = Integer.parseInt(entity.substring(1));
                    days.day = AppUtil.parseDayOfWeek(entity.substring(0, 1));
                    garbageDays.add(days);
                }
                break;

                default:
                    break;
            }
        }

        return garbageDays;
    }
}

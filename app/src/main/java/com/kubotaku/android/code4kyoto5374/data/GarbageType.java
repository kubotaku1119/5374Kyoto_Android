package com.kubotaku.android.code4kyoto5374.data;

import io.realm.RealmObject;

/**
 * ごみ区分クラス.
 */

public class GarbageType extends RealmObject {

    /**
     * 禁止：京都市では回収していないもの
     */
    public static final int TYPE_NO = 0;

    /**
     * 禁止：京都市では回収していないもの
     */
    public static final String TEXT_TYPE_NO = "禁止";

    /**
     * 燃やす：燃やすゴミ収集
     */
    public static final int TYPE_BURNABLE = 1;

    /**
     * 燃やす：燃やすゴミ収集
     */
    public static final String TEXT_TYPE_BURNABLE = "燃やす";

    /**
     * 缶等：缶・ビン・ペットボトル分別収集
     */
    public static final int TYPE_BIN = 2;

    /**
     * 缶等：缶・ビン・ペットボトル分別収集
     */
    public static final String TEXT_TYPE_BIN = "缶等";

    /**
     * プラ：プラスチック製の容器と包装分別収集
     */
    public static final int TYPE_PLASTIC = 3;

    /**
     * プラ：プラスチック製の容器と包装分別収集
     */
    public static final String TEXT_TYPE_PLASTIC = "プラ";

    /**
     * 小金：小型金属類分別収集
     */
    public static final int TYPE_SMALL = 4;

    /**
     * 小金：小型金属類分別収集
     */
    public static final String TEXT_TYPE_SMALL = "小金";

    /**
     * 大型：大型ごみ
     */
    public static final int TYPE_BIG = 5;

    /**
     * 大型：大型ごみ
     */
    public static final String TEXT_TYPE_BIG = "大型";

    /**
     * 拠回：拠点回収
     */
    public static final int TYPE_PLACE = 6;

    /**
     * 拠回：拠点回収
     */
    public static final String TEXT_TYPE_PLACE = "拠回";

    /**
     * 雑がみ：新聞・段ボール・紙パック以外のリサイクル可能な紙類
     */
    public static final int TYPE_PAPER = 7;

    /**
     * 雑がみ：新聞・段ボール・紙パック以外のリサイクル可能な紙類
     */
    public static final String TEXT_TYPE_PAPER = "雑がみ";

    /**
     * リ法：法律でリサイクルが定められているもの
     */
    public static final int TYPE_RECYCLE = 8;

    /**
     * リ法：法律でリサイクルが定められているもの
     */
    public static final String TEXT_TYPE_RECYCLE = "リ法";

    /**
     * 店頭：販売店等自主回収ルートあり
     */
    public static final int TYPE_SHOP = 9;

    /**
     * 店頭：販売店等自主回収ルートあり
     */
    public static final String TEXT_TYPE_SHOP = "店頭";

    /**
     * 新聞紙
     */
    public static final int TYPE_PAPER_NEWS_PAPER = 10;

    /**
     * 新聞紙
     */
    public static final String TEXT_TYPE_PAPER_NEWS_PAPER = "新聞";

    /**
     * 段ボール
     */
    public static final int TYPE_PAPER_CARDBOARD = 11;

    /**
     * 段ボール
     */
    public static final String TEXT_TYPE_PAPER_CARDBOARD = "ダンボール";

    /**
     * ごみ区別
     */
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static GarbageType parse(String src) {

        final GarbageType garbageType = new GarbageType();

        switch (src) {
            case TEXT_TYPE_NO:
                garbageType.type = TYPE_NO;
                break;
            case TEXT_TYPE_BURNABLE:
                garbageType.type = TYPE_BURNABLE;
                break;
            case TEXT_TYPE_BIN:
                garbageType.type = TYPE_BIN;
                break;
            case TEXT_TYPE_PLASTIC:
                garbageType.type = TYPE_PLASTIC;
                break;
            case TEXT_TYPE_SMALL:
                garbageType.type = TYPE_SMALL;
                break;
            case TEXT_TYPE_BIG:
                garbageType.type = TYPE_BIG;
                break;
            case TEXT_TYPE_PLACE:
                garbageType.type = TYPE_PLACE;
                break;
            case TEXT_TYPE_PAPER:
                garbageType.type = TYPE_PAPER;
                break;
            case TEXT_TYPE_RECYCLE:
                garbageType.type = TYPE_RECYCLE;
                break;
            case TEXT_TYPE_SHOP:
                garbageType.type = TYPE_SHOP;
                break;
            case TEXT_TYPE_PAPER_NEWS_PAPER:
                garbageType.type = TYPE_PAPER_NEWS_PAPER;
                break;
            case TEXT_TYPE_PAPER_CARDBOARD:
                garbageType.type = TYPE_PAPER_CARDBOARD;
                break;
        }

        return garbageType;
    }
}
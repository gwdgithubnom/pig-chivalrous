/**
 * 文 件 名 :
 * CopyRright (c) 1949-xxxx:
 * 文件编号：
 * 创 建 人：龚文东
 * 日    期：Nov 22, 2015
 * 修 改 人：root
 * 日   期：
 * 修改备注：
 * 描   述：
 * 版 本 号：
 */

package org.gjgr.pig.chivalrous.core.date;

/**
 * This class is used for ... ClassName: DateStyle
 *
 * @author 龚文东 root
 * @version Nov 22, 2015 11:58:33 AM
 * @Description: TODO
 */
public enum DateStyle {

    YYYYMM("yyyyMM", false),
    YYYYMMDD("yyyMMdd", false),
    YYYY_MM("yyyy-MM", false),
    /**
     * 标准日期格式：yyyy-MM-dd
     */
    YYYY_MM_DD("yyyy-MM-dd", false),
    /**
     * 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm
     */
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),
    HHMM("hhmm", true),
    HHMMSS("hhmmss", true),
    HH_MM("HH:mm", true),
    /**
     * 标准时间格式：HH:mm:ss
     */
    HH_MM_SS("HH:mm:ss", true),
    MM_DD("MM-dd", true),
    MM_DD_HH_MM("MM-dd HH:mm", true),
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

    EN_YYYY_MM("yyyy/MM", false),
    EN_YYYY_MM_DD("yyyy/MM/dd", false),
    EN_YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm", false),
    EN_YYYY_MM_DD_HH_MM_SS("yyyy/MM/dd HH:mm:ss", false),
    /**
     * 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS
     */
    EN_YYYY_MM_DD_HH_MM_SS_SSS("yyyy/MM/dd HH:mm:ss.SSS", false),
    EN_MM_DD("MM/dd", true),
    EN_MM_DD_HH_MM("MM/dd HH:mm", true),
    EN_MM_DD_HH_MM_SS("MM/dd HH:mm:ss", true),

    CN_YYYY_MM("yyyy年MM月", false),
    CN_YYYY_MM_DD("yyyy年MM月dd日", false),
    CN_YYYY_MM_DD_HH_MM("yyyy年MM月dd日 HH:mm", false),
    CN_YYYY_MM_DD_HH_MM_SS("yyyy年MM月dd日 HH:mm:ss", false),
    CN_MM_DD("MM月dd日", true),
    CN_MM_DD_HH_MM("MM月dd日 HH:mm", true),
    CN_MM_DD_HH_MM_SS("MM月dd日 HH:mm:ss", true),
    /**
     * HTTP头中日期时间格式：EEE, dd MMM yyyy HH:mm:ss z
     */
    HTTP_DATESTYLE_DEFAULT("EEE, dd MMM yyyy HH:mm:ss z", true);

    private String value;

    private boolean isShowOnly;

    DateStyle(String value, boolean isShowOnly) {
        this.value = value;
        this.isShowOnly = isShowOnly;
    }

    public String getValue() {
        return value;
    }

    public boolean isShowOnly() {
        return isShowOnly;
    }

}

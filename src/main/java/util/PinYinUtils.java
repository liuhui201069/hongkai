package util;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 拼音转换工具类
 *
 * @author huiliu
 * @date 17/3/6
 */
public class PinYinUtils {
    /**
     * 生成拼音
     * 
     * @param text
     * @return
     */
    public static String getPinYin(String text) {
        return PinyinHelper.convertToPinyinString(text, "", PinyinFormat.WITHOUT_TONE);
    }
}

package lsl.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 词频统计并排序
 * @author LSL
 *
 */
public class WordCountAndSort {

	public static List<Entry<String, Integer>> getWordCountAndSort(int maxLen,
			HashSet<String> DICT, String text) {
		return sortMap(getCount(matchWords(DICT, maxLen, text)));
	}

	/**
	 * map 排序
	 * 
	 * @param map
	 * @return
	 */
	private static List<Map.Entry<String, Integer>> sortMap(
			Map<String, Integer> map) {

		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			// 升序排序
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}

		});

		return list;
	}

	/**
	 * 正向最大匹配算法（MM算法），最长词优先匹配
	 * 
	 * @return
	 */
	private static List<String> matchWords(final HashSet<String> DICT,final int MAX_LENGTH, String text) {
		// result存储分词后的结果
		List<String> result = new ArrayList<String>();
		while (0 < text.length()) {
			int len = MAX_LENGTH;
			// 如果待切分字符串的长度小于词典中的最大词长，则令最大词长等于待切分字符串的长度
			if (text.length() < len) {
				len = text.length();
			}
			// 取指定的最大长度的文本去词典里面匹配
			String tryWord = text.substring(0, len);
			// 匹配不成功，则缩小截取范围
			while (!DICT.contains(tryWord)) {
				if (tryWord.length() == 1) {
					break;
				}
				tryWord = tryWord.substring(0, tryWord.length() - 1);
			}
			// 匹配成功
			result.add(tryWord);
			// 从待切分字符串中去除已经分词完的部分
			text = text.substring(tryWord.length());
		}
		return result;
	}

	// 将list中的元素保存到map中，之后，统计个数
	private static Map<String, Integer> getCount(List<String> str) {
		int size = str.size();// 元素个数
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < size; i++) {
			// 只匹配文字，标点符号不计算入内
			boolean word = Pattern.matches("^[\u4E00-\u9FA5]{0,}$", ""
					+ str.get(i));
			if (word && str.get(i).length() > 1) {
				if (map.get(str.get(i)) == null) {
					map.put(str.get(i), 1);
				} else {
					map.put(str.get(i), map.get(str.get(i)) + 1);
				}
			}
		}
		return map;
	}

}

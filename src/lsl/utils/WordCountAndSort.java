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
 * ��Ƶͳ�Ʋ�����
 * @author LSL
 *
 */
public class WordCountAndSort {

	public static List<Entry<String, Integer>> getWordCountAndSort(int maxLen,
			HashSet<String> DICT, String text) {
		return sortMap(getCount(matchWords(DICT, maxLen, text)));
	}

	/**
	 * map ����
	 * 
	 * @param map
	 * @return
	 */
	private static List<Map.Entry<String, Integer>> sortMap(
			Map<String, Integer> map) {

		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			// ��������
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}

		});

		return list;
	}

	/**
	 * �������ƥ���㷨��MM�㷨�����������ƥ��
	 * 
	 * @return
	 */
	private static List<String> matchWords(final HashSet<String> DICT,final int MAX_LENGTH, String text) {
		// result�洢�ִʺ�Ľ��
		List<String> result = new ArrayList<String>();
		while (0 < text.length()) {
			int len = MAX_LENGTH;
			// ������з��ַ����ĳ���С�ڴʵ��е����ʳ����������ʳ����ڴ��з��ַ����ĳ���
			if (text.length() < len) {
				len = text.length();
			}
			// ȡָ������󳤶ȵ��ı�ȥ�ʵ�����ƥ��
			String tryWord = text.substring(0, len);
			// ƥ�䲻�ɹ�������С��ȡ��Χ
			while (!DICT.contains(tryWord)) {
				if (tryWord.length() == 1) {
					break;
				}
				tryWord = tryWord.substring(0, tryWord.length() - 1);
			}
			// ƥ��ɹ�
			result.add(tryWord);
			// �Ӵ��з��ַ�����ȥ���Ѿ��ִ���Ĳ���
			text = text.substring(tryWord.length());
		}
		return result;
	}

	// ��list�е�Ԫ�ر��浽map�У�֮��ͳ�Ƹ���
	private static Map<String, Integer> getCount(List<String> str) {
		int size = str.size();// Ԫ�ظ���
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < size; i++) {
			// ֻƥ�����֣������Ų���������
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

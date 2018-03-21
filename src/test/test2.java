package test;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import lsl.utils.WordCountAndSort;

/**
 * User: chanson-pro
 * Date-Time: 2018-2-10 20:43
 * Description:
 * �ó���ͳ���º���ľ���һ���ж��ٸ��ʣ��Լ�ÿ���ʳ��ֵĴ�����+
 * ������Ͱͼ��ž�Ӫ����ҵ������Ҳ�ӹ�����˾��ҵ��ͷ�����ȡ�þ�Ӫ��ҵ��̬ϵͳ�ϵ�֧Ԯ��
 * ҵ��͹�����˾��ҵ��������Ա�������è���ۻ��㡢ȫ������ͨ������Ͱ͹��ʽ����г���1688���������衢�����ơ����Ͻ������������ȡ���
 */
public class test2 {
    //DICTΪ�ʵ䣬MAX_LENGTHΪ�ʵ��е����ʳ�
    private static final HashSet<String> DICT = new HashSet<String>();
    private static final int MAX_LENGTH;

    static {
        int max = 1;
        DICT.add("����Ͱ�");
        DICT.add("����");
        DICT.add("��Ӫ");
        DICT.add("����");
        DICT.add("ҵ��");
        DICT.add("����");
        DICT.add("������˾");
        DICT.add("����");
        DICT.add("ȡ��");
        DICT.add("��ҵ");
        DICT.add("��̬ϵͳ");
        DICT.add("֧Ԯ");
        DICT.add("��ҽԺ");
        DICT.add("ҽԺ");
        DICT.add("����ҽԺ");
//        DICT.add("����");
//        DICT.add("֧Ԯ");
//        DICT.add("�Ա���");
//        DICT.add("��è");
//        DICT.add("Ҳ��");
//        DICT.add("�ۻ���");
//        DICT.add("ȫ��");
//        DICT.add("����ͨ");
//        DICT.add("����");
//        DICT.add("�����г�");
//        DICT.add("1688");
//        DICT.add("��������");
//        DICT.add("������");
//        DICT.add("���Ͻ��");
//        DICT.add("����");
//        DICT.add("����");
        for (String word:DICT) {
            if (max<word.length()){
                max = word.length();
            }
        }
        MAX_LENGTH = max;
    }

    public static void main(String[] args) {
        String text =
                "����Ͱͼ��ž�Ӫ����ҵ������Ҳ�ӹ�����˾��ҵ��ͷ�����ȡ�þ�Ӫ��ҵ��̬ϵͳ�ϵ�֧Ԯ��ҽԺ," +
                        "ҵ��͹�����˾��ҵ��������Ա�����ҽԺ,��è���ۻ��㡢ȫ������ͨ������Ͱ͹��ʽ����г���" +
                        "1688���������衢�����ơ�����ҽԺ���Ͻ������������ȡ���ҽԺ,,";
        System.out.println("�ʵ��ܸ���Ϊ��"+getCount(matchWords(text)).size());
        System.out.println("ÿ���ʵĸ���Ϊ��"+getCount(matchWords(text)));
        
        
        int max = 0;
        String c = "";
        Map<String,Integer> map = new HashMap<String,Integer>();
        map = getCount(matchWords(text));
        for (Object o : map.keySet()) {
//        	System.out.println("key=" + o + " value=" + map.get(o));  
//        	max = map.get(o);
//        	c = (String) o;
        	if (max < map.get(o)) {
				max = map.get(o);
				c = (String) o;
			}
        }
//        System.out.println(c);

//        DICT.remove("ҵ��");
//        System.out.println(DICT);
        
        List<Map.Entry<String,Integer>> list = WordCountAndSort.getWordCountAndSort(MAX_LENGTH, DICT, text);
        
//       for(Map.Entry<String,Integer> mapping:list){ 
//            System.out.println(mapping.getKey()+":"+mapping.getValue()); 
//       } 
       System.out.println(MAX_LENGTH);
        System.out.println(list.size());
        System.out.println(list);
    }
    
    private static List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
//    	//���ｫmap.entrySet()ת����list
//        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
//        //Ȼ��ͨ���Ƚ�����ʵ������
//        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
//            //��������
//            public int compare(Entry<String, Integer> o1,
//                    Entry<String, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
    	
    	List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            //��������
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });
    	
    	return list;  	
    }

    /**
     * �������ƥ���㷨��MM�㷨�����������ƥ��
     * @return
     */
    private static List<String> matchWords(String text){
        //result�洢�ִʺ�Ľ��
        List<String> result = new ArrayList<String>();
        while (0 < text.length()){
            int len = MAX_LENGTH;
            //������з��ַ����ĳ���С�ڴʵ��е����ʳ����������ʳ����ڴ��з��ַ����ĳ���
            if (text.length()<len){
                len = text.length();
            }
            //ȡָ������󳤶ȵ��ı�ȥ�ʵ�����ƥ��
            String tryWord = text.substring(0,len);
            //ƥ�䲻�ɹ�������С��ȡ��Χ
            while (!DICT.contains(tryWord)){
                if (tryWord.length()==1){
                    break;
                }
                tryWord = tryWord.substring(0,tryWord.length()-1);
            }
            // ƥ��ɹ�
            result.add(tryWord);
            //�Ӵ��з��ַ�����ȥ���Ѿ��ִ���Ĳ���
            text = text.substring(tryWord.length());
        }
        return result;
    }
    //��list�е�Ԫ�ر��浽map�У�֮��ͳ�Ƹ���
    private static Map<String, Integer> getCount(List<String> str){
        int size = str.size();//Ԫ�ظ���
        Map<String,Integer> map = new HashMap<String,Integer>();
        for (int i=0;i<size;i++){
            //ֻƥ�����֣������Ų���������
            boolean word = Pattern.matches("^[\u4E00-\u9FA5]{0,}$",""+str.get(i));
            if (word && str.get(i).length()>1){
                if (map.get(str.get(i)) == null){
                    map.put(str.get(i),1);
                }else{
                    map.put(str.get(i),map.get(str.get(i))+1);
                }
            }
        }
        return map;
    }



}
package test;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import lsl.utils.WordCountAndSort;

/**
 * User: chanson-pro
 * Date-Time: 2018-2-10 20:43
 * Description:
 * 用程序统计下后面的句子一共有多少个词，以及每个词出现的次数？+
 * “阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。
 * 业务和关联公司的业务包括：淘宝网、天猫、聚划算、全球速卖通、阿里巴巴国际交易市场、1688、阿里妈妈、阿里云、蚂蚁金服、菜鸟网络等。”
 */
public class test2 {
    //DICT为词典，MAX_LENGTH为词典中的最大词长
    private static final HashSet<String> DICT = new HashSet<String>();
    private static final int MAX_LENGTH;

    static {
        int max = 1;
        DICT.add("阿里巴巴");
        DICT.add("集团");
        DICT.add("经营");
        DICT.add("多项");
        DICT.add("业务");
        DICT.add("另外");
        DICT.add("关联公司");
        DICT.add("服务");
        DICT.add("取得");
        DICT.add("商业");
        DICT.add("生态系统");
        DICT.add("支援");
        DICT.add("大医院");
        DICT.add("医院");
        DICT.add("人民医院");
//        DICT.add("包括");
//        DICT.add("支援");
//        DICT.add("淘宝网");
//        DICT.add("天猫");
//        DICT.add("也从");
//        DICT.add("聚划算");
//        DICT.add("全球");
//        DICT.add("速卖通");
//        DICT.add("国际");
//        DICT.add("交易市场");
//        DICT.add("1688");
//        DICT.add("阿里妈妈");
//        DICT.add("阿里云");
//        DICT.add("蚂蚁金服");
//        DICT.add("菜鸟");
//        DICT.add("网络");
        for (String word:DICT) {
            if (max<word.length()){
                max = word.length();
            }
        }
        MAX_LENGTH = max;
    }

    public static void main(String[] args) {
        String text =
                "阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。医院," +
                        "业务和关联公司的业务包括：淘宝网、医院,天猫、聚划算、全球速卖通、阿里巴巴国际交易市场、" +
                        "1688、阿里妈妈、阿里云、人民医院蚂蚁金服、菜鸟网络等。大医院,,";
        System.out.println("词的总个数为："+getCount(matchWords(text)).size());
        System.out.println("每个词的个数为："+getCount(matchWords(text)));
        
        
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

//        DICT.remove("业务");
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
//    	//这里将map.entrySet()转换成list
//        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
//        //然后通过比较器来实现排序
//        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
//            //升序排序
//            public int compare(Entry<String, Integer> o1,
//                    Entry<String, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
    	
    	List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            //升序排序
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });
    	
    	return list;  	
    }

    /**
     * 正向最大匹配算法（MM算法），最长词优先匹配
     * @return
     */
    private static List<String> matchWords(String text){
        //result存储分词后的结果
        List<String> result = new ArrayList<String>();
        while (0 < text.length()){
            int len = MAX_LENGTH;
            //如果待切分字符串的长度小于词典中的最大词长，则令最大词长等于待切分字符串的长度
            if (text.length()<len){
                len = text.length();
            }
            //取指定的最大长度的文本去词典里面匹配
            String tryWord = text.substring(0,len);
            //匹配不成功，则缩小截取范围
            while (!DICT.contains(tryWord)){
                if (tryWord.length()==1){
                    break;
                }
                tryWord = tryWord.substring(0,tryWord.length()-1);
            }
            // 匹配成功
            result.add(tryWord);
            //从待切分字符串中去除已经分词完的部分
            text = text.substring(tryWord.length());
        }
        return result;
    }
    //将list中的元素保存到map中，之后，统计个数
    private static Map<String, Integer> getCount(List<String> str){
        int size = str.size();//元素个数
        Map<String,Integer> map = new HashMap<String,Integer>();
        for (int i=0;i<size;i++){
            //只匹配文字，标点符号不计算入内
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
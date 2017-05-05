package com.oupeng.joke.back.service;

import com.oupeng.joke.back.util.BCConvert;
import com.oupeng.joke.back.util.FilterSet;
import com.oupeng.joke.back.util.WordNode;
import com.oupeng.joke.dao.mapper.SensitiveMapper;
import com.oupeng.joke.domain.Sensitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 过滤敏感词
 * Created by java_zong on 2017/4/26.
 */
@Service
public class SensitiveFilterService {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilterService.class);

//    private Map sensitiveWordMap  = Maps.newConcurrentMap();
//
//    private Set<String> sensitiveSet = Sets.newHashSet();
//
//    public static int minMatchTYpe = 1;      //最小匹配规则
//
//    public static int maxMatchType = 2;      //最大匹配规则
    private static FilterSet set = new FilterSet(); // 存储首字

//    public void init() {
//        sensitiveSet = sensitiveMapper.getSensitiveWord();
//        addSensitiveWordToHashMap(sensitiveSet);
//    }
    private static Map<Integer, WordNode> nodes = new HashMap<>(1024, 1); // 存储节点
    private static Set<Integer> stopwdSet = new HashSet<>(); // 停顿词
    private static char SIGN = ' '; // 敏感词过滤替换
    @Autowired
    private SensitiveMapper sensitiveMapper;

    /**
     * 增加停顿词
     *
     * @param words
     */
    private static void addStopWord(final List<String> words) {
        if (words != null && words.size() > 0) {
            char[] chs;
            for (String curr : words) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    stopwdSet.add(charConvert(c));
                }
            }
        }
    }

//    /**
//     * 增加敏感词
//     * @param path
//     * @return
//     */
//    private static List<String> readWordFromFile(String path) {
//        List<String> words;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(WordFilter.class.getClassLoader().getResourceAsStream(path)));
//            words = new ArrayList<String>(1200);
//            for (String buf = ""; (buf = br.readLine()) != null;) {
//                if (buf == null || buf.trim().equals(""))
//                    continue;
//                words.add(buf);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                if (br != null)
//                    br.close();
//            } catch (IOException e) {
//            }
//        }
//        return words;
//    }

    /**
     * 大写转化为小写 全角转化为半角
     *
     * @param src
     * @return
     */
    private static int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

//    static {
//        try {
//            long a = System.nanoTime();
//            init();
//            a = System.nanoTime() - a;
//            System.out.println("加载时间 : " + a + "ns");
//            System.out.println("加载时间 : " + a / 1000000 + "ms");
//        } catch (Exception e) {
//            throw new RuntimeException("初始化过滤器失败");
//        }
//    }
    @PostConstruct
    public void init() {
        // 获取敏感词
        addSensitiveWord(sensitiveMapper.getSensitiveWord());
//        addStopWord(readWordFromFile("stopwd.txt"));
    }

    /**
     * 添加DFA节点
     * @param word
     */
    public boolean addSensitiveWord(final String word) {
        if (word != null && word.length() > 0) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode; // 首字母节点
            chs = word.toCharArray();
            fchar = charConvert(chs[0]);
            if (!set.contains(fchar)) {// 没有首字定义
                set.add(fchar);// 首字标志位 可重复add,反正判断了，不重复了
                fnode = new WordNode(fchar, chs.length == 1);
                nodes.put(fchar, fnode);
            } else {
                fnode = nodes.get(fchar);
                if (!fnode.isLast() && chs.length == 1)
                    fnode.setLast(true);
            }
            lastIndex = chs.length - 1;
            for (int i = 1; i < chs.length; i++) {
                fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
            }
            sensitiveMapper.insertWord(word);
            return true;
        }
        return false;
    }

    /**
     * 添加DFA节点
     * @param words
     */
    public void addSensitiveWord(final List<String> words) {
        if (words != null && words.size() > 0) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode; // 首字母节点
            for (String curr : words) {
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                if (!set.contains(fchar)) {// 没有首字定义
                    set.add(fchar);// 首字标志位 可重复add,反正判断了，不重复了
                    fnode = new WordNode(fchar, chs.length == 1);
                    nodes.put(fchar, fnode);
                } else {
                    fnode = nodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1)
                        fnode.setLast(true);
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     * @param src
     * @return
     */
    public String doFilter(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!set.contains(currc)) {
                continue;
            }
            node = nodes.get(currc);// 日 2
            if (node == null)// 其实不会发生，习惯性写上了
                continue;
            boolean couldMark = false;
            int markNum = -1;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
                markNum = 0;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length;) {
                int temp = charConvert(chs[k]);
                if (stopwdSet.contains(temp))
                    continue;
                node = node.querySub(temp);
                if (node == null)// 没有了
                    break;
                if (node.isLast()) {
                    couldMark = true;
                    markNum = k - i;// 3-2
                }
            }
            if (couldMark) {
                for (k = 0; k <= markNum; k++) {
                    chs[k + i] = SIGN;
                }
                i = i + markNum;
            }
        }

        return new String(chs);
    }

    /**
     * 是否包含敏感词
     * @param src
     * @return
     */
    public boolean isContains(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!set.contains(currc)) {
                continue;
            }
            node = nodes.get(currc);// 日 2
            if (node == null)// 其实不会发生，习惯性写上了
                continue;
            boolean couldMark = false;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length;) {
                int temp = charConvert(chs[k]);
                if (stopwdSet.contains(temp))
                    continue;
                node = node.querySub(temp);
                if (node == null)// 没有了
                    break;
                if (node.isLast()) {
                    couldMark = true;
                }
            }
            if (couldMark) {
                return true;
            }
        }

        return false;
    }

//    /**
//     * 获取文字中的敏感词
//     *
//     * @param txt       文字
//     * @param matchType 1：最小匹配规则，2：最大匹配规则
//     * @return
//     */
//    public Set<String> getSensitiveWord(String txt, int matchType) {
//        Set<String> sensitiveWordList = new HashSet<>();
//
//        for (int i = 0; i < txt.length(); i++) {
//            int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
//            if (length > 0) {    //存在,加入list中
//                sensitiveWordList.add(txt.substring(i, i + length));
//                i = i + length - 1;    //减1的原因，是因为for会自增
//            }
//        }
//
//        return sensitiveWordList;
//    }
//
//    /**
//     * 替换敏感字字符
//     *
//     * @param txt
//     * @param matchType
//     * @param replaceChar 替换字符，默认*
//     */
//    public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
//        String resultTxt = txt;
//        Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
//        Iterator<String> iterator = set.iterator();
//        String word = null;
//        String replaceString = null;
//        while (iterator.hasNext()) {
//            word = iterator.next();
//            replaceString = getReplaceChars(replaceChar, word.length());
//            resultTxt = resultTxt.replaceAll(word, replaceString);
//        }
//
//        return resultTxt;
//    }
//
//    /**
//     * 获取替换字符串
//     */
//    private String getReplaceChars(String replaceChar, int length) {
//        String resultReplace = replaceChar;
//        for (int i = 1; i < length; i++) {
//            resultReplace += replaceChar;
//        }
//
//        return resultReplace;
//    }
//
//    /**
//     * 检查文字中是否包含敏感字符
//     */
//    public int CheckSensitiveWord(String txt, int beginIndex, int matchType) {
//        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
//        int matchFlag = 0;     //匹配标识数默认为0
//        char word = 0;
//        Map nowMap = sensitiveWordMap;
//        for (int i = beginIndex; i < txt.length(); i++) {
//            word = txt.charAt(i);
//            nowMap = (Map) nowMap.get(word);     //获取指定key
//            if (nowMap != null) {     //存在，则判断是否为最后一个
//                matchFlag++;     //找到相应key，匹配标识+1
//                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
//                    flag = true;       //结束标志位为true
//                    if (minMatchTYpe == matchType) {    //最小规则，直接返回,最大规则还需继续查找
//                        break;
//                    }
//                }
//            } else {     //不存在，直接返回
//                break;
//            }
//        }
//        if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
//            matchFlag = 0;
//        }
//        return matchFlag;
//    }
//
//    /**
//     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型
//     */
//    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
//        //初始化敏感词容器，减少扩容操作
//        String key = null;
//        Map nowMap = null;
//        Map<String, String> newWorMap = null;
//        //迭代keyWordSet
//        Iterator<String> iterator = keyWordSet.iterator();
//        while (iterator.hasNext()) {
//            key = iterator.next();    //关键字
//            nowMap = sensitiveWordMap;
//            for (int i = 0; i < key.length(); i++) {
//                char keyChar = key.charAt(i);       //转换成char型
//                Object wordMap = nowMap.get(keyChar);       //获取
//
//                if (wordMap != null) {        //如果存在该key，直接赋值
//                    nowMap = (Map) wordMap;
//                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
//                    newWorMap = new HashMap();
//                    newWorMap.put("isEnd", "0");     //不是最后一个
//                    nowMap.put(keyChar, newWorMap);
//                    nowMap = newWorMap;
//                }
//
//                if (i == key.length() - 1) {
//                    nowMap.put("isEnd", "1");    //最后一个
//                }
//            }
//        }
//    }
//

    /**
     * 敏感词数量
     *
     * @param keyWord
     * @return
     */
    public int getListForCount(String keyWord) {
        return sensitiveMapper.getListForCount(keyWord);
    }

    /**
     * 敏感词列表
     *
     * @param keyWord
     * @param offset
     * @param pageSize
     * @return
     */
    public List<Sensitive> getList(String keyWord, int offset, Integer pageSize) {
        return sensitiveMapper.getList(keyWord, offset, pageSize);
    }
//
//    /**
//     * 添加敏感词
//     *
//     * @param word
//     * @return
//     */
//    public boolean add(String word) {
//        if (sensitiveSet.add(word)) {
//            sensitiveMapper.insertWord(word);
//            addSensitiveWordToHashMap(sensitiveSet);//重新初始化敏感词库
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 删除敏感词
//     *
//     * @param id
//     */

    /**
     * 重置
     * @return
     */
    public boolean reset() {
        try {
//            String word = sensitiveMapper.getWordById(id);
            set = new FilterSet(); // 存储首字
            nodes = new HashMap<>(1024, 1); // 存储节点
//            sensitiveSet.remove(word);
//            sensitiveWordMap = Maps.newConcurrentMap();
//            addSensitiveWordToHashMap(sensitiveSet);
            init();
            return true;
        } catch (Exception e) {
            logger.error("敏感词库删除失败:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 数据库删除
     * @param id
     */
    public void del(Integer id) {
        sensitiveMapper.deleteWord(id);
    }
}

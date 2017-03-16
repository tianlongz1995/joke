package test;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.spider.domain.CommentT;
import com.oupeng.joke.spider.domain.Joke;


/**
 * Created by Administrator on 2017/3/13.
 */
public class JsonTest {
    public static void main(String[] args){
        Joke joke=new Joke();
        joke.setTitle("title");
        CommentT com=new CommentT();
        com.setContent("comment");
        joke.setComment(com);
        System.out.println(JSON.toJSON(joke).toString());
    }
}

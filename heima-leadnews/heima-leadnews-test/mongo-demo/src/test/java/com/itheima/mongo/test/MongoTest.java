package com.itheima.mongo.test;


import com.itheima.mongo.MongoApplication;
import com.itheima.mongo.pojo.ApAssociateWords;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringRunner.class)
public class MongoTest {


    @Autowired
    private MongoTemplate mongoTemplate;

    //保存
    @Test
    public void saveTest(){
        ApAssociateWords apAssociateWords = new ApAssociateWords();

    }

    //查询一个
    @Test
    public void saveFindOne(){
//        ApAssociateWords byId = mongoTemplate.findById("60bdc02a4582bc47e1137a39", ApAssociateWords.class);
//        System.out.println(byId);
        List list=new ArrayList();
        Query query = Query.query(Criteria.where("associateWords").is("黑马java")).limit(0).skip(1);

        List<ApAssociateWords> apAssociateWords = mongoTemplate.find(query, ApAssociateWords.class);
        for (ApAssociateWords apAssociateWord : apAssociateWords) {
            System.out.println(apAssociateWord);
        }
    }

    //条件查询
    @Test
    public void testQuery(){

    }

    @Test
    public void testDel(){

    }
}

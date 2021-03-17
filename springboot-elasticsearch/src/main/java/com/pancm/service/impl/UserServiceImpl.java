package com.pancm.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.functionscore.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.pancm.dao.UserDao;
import com.pancm.pojo.User;
import com.pancm.service.UserService;

/**
 * 
* Title: UserServiceImpl
* Description:
* 用户操作实现类 
* Version:1.0.0  
* @author pancm
* @date 2018年1月9日
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserDao userDao;
	@Override
	public boolean insert(User user) {
		boolean falg=false;
		try{
			userDao.save(user);
			falg=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return falg;
	}

	@Override
	public List<User> search(String searchContent) {
		  QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
		  System.out.println("查询的语句:"+builder);
          Iterable<User> searchResult = userDao.search(builder);
          Iterator<User> iterator = searchResult.iterator();
          List<User> list=new ArrayList<User>();
          while (iterator.hasNext()) {
       	   	list.add(iterator.next());
          }
       return list;
	}
	
	
	
	@Override
	public List<User> searchUser(Integer pageNumber, Integer pageSize,String searchContent) {
		 // 分页参数
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(builder).build();
        System.out.println("查询的语句:" + searchQuery.getQuery().toString());
        Page<User> searchPageResults = userDao.search(searchQuery);
        return searchPageResults.getContent();
	}
	

	@Override
	public List<User> searchUserByWeight(String searchContent) {
		FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
		ScoreFunctionBuilder<WeightBuilder> scoreFunctionBuilder = new WeightBuilder();
		scoreFunctionBuilder.setWeight(10);
		QueryBuilder termQuery = QueryBuilders.termQuery("name", searchContent);
		FunctionScoreQueryBuilder.FilterFunctionBuilder category = new FunctionScoreQueryBuilder.FilterFunctionBuilder(termQuery, scoreFunctionBuilder);
		filterFunctionBuilders[0] = category;

		scoreFunctionBuilder = new WeightBuilder();
		scoreFunctionBuilder.setWeight(100);
		termQuery = QueryBuilders.termQuery("description", searchContent);
		category = new FunctionScoreQueryBuilder.FilterFunctionBuilder(termQuery, scoreFunctionBuilder);
		filterFunctionBuilders[1] = category;

		FunctionScoreQueryBuilder functionScoreQueryBuilder1 = QueryBuilders.functionScoreQuery(filterFunctionBuilders);

        System.out.println("查询的语句:" + functionScoreQueryBuilder1.toString());
        Iterable<User> searchResult = userDao.search(functionScoreQueryBuilder1);
        Iterator<User> iterator = searchResult.iterator();
        List<User> list=new ArrayList<User>();
        while (iterator.hasNext()) {
     	   	list.add(iterator.next());
        }
        return list;
	}

	void search1() {
		String searchContent = "IPhone";
		TransportClient client = null;
		String index = "10000";
		SearchRequestBuilder searchBuilder = client.prepareSearch(index);
		//分页
		searchBuilder.setFrom(0).setSize(10);
		//explain为true表示根据数据相关度排序，和关键字匹配最高的排在前面
		searchBuilder.setExplain(true);

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		// 搜索 title字段包含IPhone的数据
		queryBuilder.must(QueryBuilders.matchQuery("title", searchContent));

		FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[3];

		//过滤条件1：分类为：品牌手机最重要 -- 权重查询Weight
		ScoreFunctionBuilder<WeightBuilder> scoreFunctionBuilder = new WeightBuilder();
		scoreFunctionBuilder.setWeight(2);
		QueryBuilder termQuery = QueryBuilders.termQuery("categoryName", "品牌手机");
		FunctionScoreQueryBuilder.FilterFunctionBuilder category = new FunctionScoreQueryBuilder.FilterFunctionBuilder(termQuery, scoreFunctionBuilder);
		filterFunctionBuilders[0] = category;

		// 过滤条件2：销量越高越排前 --计分查询 FieldValueFactor
		ScoreFunctionBuilder<FieldValueFactorFunctionBuilder> fieldValueScoreFunction = new FieldValueFactorFunctionBuilder("salesVolume");
		((FieldValueFactorFunctionBuilder) fieldValueScoreFunction).factor(1.2f);
		FunctionScoreQueryBuilder.FilterFunctionBuilder sales = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fieldValueScoreFunction);
		filterFunctionBuilders[1] = sales;

		// 给定每个用户随机展示：  --random_score
		ScoreFunctionBuilder<RandomScoreFunctionBuilder> randomScoreFilter = new RandomScoreFunctionBuilder();
		((RandomScoreFunctionBuilder) randomScoreFilter).seed(2);
		FunctionScoreQueryBuilder.FilterFunctionBuilder random = new FunctionScoreQueryBuilder.FilterFunctionBuilder(randomScoreFilter);
		filterFunctionBuilders[2] = random;

		// 多条件查询 FunctionScore
		FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(queryBuilder, filterFunctionBuilders)
				.scoreMode(FunctionScoreQuery.ScoreMode.SUM).boostMode(CombineFunction.SUM);
		searchBuilder.setQuery(query);

		SearchResponse response = searchBuilder.execute().actionGet();
		SearchHits hits = response.getHits();
		String searchSource;
		for (SearchHit hit : hits)
		{
			searchSource = hit.getSourceAsString();
			System.out.println(searchSource);
		}
		//        long took = response.getTook().getMillis();
		long total = hits.getTotalHits();
		System.out.println(total);
	}
}

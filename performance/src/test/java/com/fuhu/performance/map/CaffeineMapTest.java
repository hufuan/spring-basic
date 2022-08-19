package com.fuhu.performance.map;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.fuhu.performance.utils.PerformanceUtil.getLookupRecordIds;
import static com.fuhu.performance.utils.PerformanceUtil.getRandomRecordIds;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaffeineMapTest {
    private static final long ISNG_NEA_FLOW_RECORDS_EXPIRE_TIME_IN_SECOND = 3000;
    static final int NUM_KEY_1M = 1 * 1000 * 1000;
    static final int NUM_KEY_10M = 10 * 1000 * 1000;
    static final int NUM_KEY_50M = 50 * 1000 * 1000;
    CorrelationRecordIdMeta recordIds = null;

    @Before
    public void init() {
        //recordIds = getRandomrecordIds(NUM_KEY_50M);
    }

    @Test
    @DisplayName("run test1")
    public void test0() {
        for (int i = 0; i < 10; i++) {
            System.out.println("dummy!");
        }
    }

    private Cache<Long, Long> createCaffeineCache(int size) {
        Cache<Long, Long> cache = Caffeine.newBuilder()
                .expireAfterWrite(ISNG_NEA_FLOW_RECORDS_EXPIRE_TIME_IN_SECOND, TimeUnit.SECONDS)
                .maximumSize(size)
                //.recordStats()
                .build();
        return cache;
    }


    private Cache<Long, Long> buildCaffeineCache(int size) {
        Cache<Long, Long> cache = createCaffeineCache(size);
        for (int i = 0; i < size; i++) {
            Long recordId = recordIds.getRecordIds().get(i);
            cache.put(recordId, recordId);
        }
        return cache;
    }

    private List<Cache<Long, Long>> buidSplitCacheMap(int size, int groupNum, boolean isInitSize) {
        int sizeForEach = size / groupNum;
        List<Cache<Long, Long>> mapList = new ArrayList<>();
        int totalIndex = 0;
        for (int groupIndx = 0; groupIndx < groupNum; groupIndx++) {
            Cache<Long, Long> cache;
            cache = createCaffeineCache(sizeForEach);
            for (int i = 0; i < sizeForEach; i++) {
                Long recordId = recordIds.getRecordIds().get(totalIndex);
                cache.put(recordId, recordId);
                totalIndex++;
            }
            mapList.add(cache);
        }
        return mapList;
    }

    private List<Cache<Long, Long>> buidSplitHashMapRandomSplit(int size, int groupNum, boolean isInitSize) {
        int sizeForEach = size / groupNum;
        List<Cache<Long, Long>> mapList = new ArrayList<>();
        int totalIndex = 0;
        for (int groupIndx = 0; groupIndx < groupNum; groupIndx++) {
            Cache<Long, Long> cache;
            cache = createCaffeineCache(sizeForEach);
            mapList.add(cache);
        }
        for (int groupIndx = 0; groupIndx < groupNum; groupIndx++) {
            for (int i = 0; i < sizeForEach; i++) {
                Long recordId = recordIds.getRecordIds().get(totalIndex);
                int mod = (int) (recordId & (groupNum - 1));
                mapList.get(mod).put(recordId, recordId);
                totalIndex++;
            }
        }
        return mapList;
    }

    @Test
    @DisplayName("run test1")
    public void test1() {
        int keySize = NUM_KEY_10M;
        //int keySize = NUM_KEY_10M;
        //int keySize = 100;
        int insertTestLoop = 3;
        int splitFactor = 64;
        boolean preConfigSize = true;
        recordIds = getRandomRecordIds(keySize);
        System.out.println("record id table setup finished!");
        //warm up, drop the test
        Cache<Long, Long> map = buildCaffeineCache(keySize);
        System.out.println("warm up finished!");

        List[] testLoopMapArr = new List[insertTestLoop];
        long perfStartTime = java.lang.System.nanoTime();
        for (int i = 0; i < insertTestLoop; i++) {
            testLoopMapArr[i] = buidSplitCacheMap(keySize, splitFactor, preConfigSize);
            //System.out.println("map["+ i+ "] size = " + mapArr[i].size());
        }
        long perfEndTime = java.lang.System.nanoTime();
        long deltTime1 = (perfEndTime - perfStartTime) / 1000000 / insertTestLoop;
        System.out.println("DEBUG: Insert test loop: " + insertTestLoop);
        System.out.println("One Cache is split into " + testLoopMapArr[0].size() + ", One big map totally costs " + deltTime1 + " ms.");
    }

    @Test
    @DisplayName("run test2")
    public void test2() {
        int keySize = NUM_KEY_10M;
        //int keySize = NUM_KEY_50M;
        //int keySize = 100;
        int insertTestLoop = 3;
        int splitFactor = 1;
        boolean preConfigSize = true;
        int foundNum = 10000;
        int lookupTestLoop = 1000; //total lookup 1000*10000 times
        recordIds = getRandomRecordIds(keySize);
        long[] lookupIds = getLookupRecordIds(recordIds, foundNum);
        System.out.println("record id table setup finished!");
        //System.out.println("lookup id num: " + lookupIds.length);
        //warm up, drop the test
        Cache<Long, Long> map = buildCaffeineCache(keySize);
        System.out.println("warm up finished!");

        List[] testLoopMapArr = new List[insertTestLoop];
        long perfStartTime = java.lang.System.nanoTime();
        for (int i = 0; i < insertTestLoop; i++) {
            testLoopMapArr[i] = buidSplitHashMapRandomSplit(keySize, splitFactor, preConfigSize);
            //System.out.println("map["+ i+ "] size = " + testLoopMapArr[i].size());
        }
        long perfEndTime = java.lang.System.nanoTime();
        long deltTime1 = (perfEndTime - perfStartTime) / 1000000 / insertTestLoop;


        //check lookup time
        long[] foundRes = new long[lookupIds.length];
        perfStartTime = java.lang.System.nanoTime();
        List<Cache<Long, Long>> mapPickedToCheck = testLoopMapArr[0];
        for (int k = 0; k < lookupTestLoop; k++) {
            for (int i = 0; i < lookupIds.length; i++) {
                int key = (int) (lookupIds[i] & (splitFactor - 1));
                foundRes[i] = mapPickedToCheck.get(key).getIfPresent(lookupIds[i]);
            }
        }
        perfEndTime = java.lang.System.nanoTime();
        long deltTime2 = (perfEndTime - perfStartTime) / 1000000;


        for (int i = 0; i < lookupIds.length; i++) {
            assertEquals(lookupIds[i], foundRes[i]);
        }
        for (int i = 0; i < splitFactor; i = i + 10) {
            System.out.println("map[ " + i + " ] size: " + mapPickedToCheck.get(i).estimatedSize());
        }
        System.out.println("DEBUG: Insert test loop: " + insertTestLoop);
        System.out.println("DEBUG: Lookup test loop: " + lookupTestLoop);
        System.out.println("One Cache is split into " + testLoopMapArr[0].size() + ",One big Cache  totally costs " + deltTime1 + " ms.");
        System.out.println("finding  " + (lookupIds.length * lookupTestLoop) + " keys  costs " + deltTime2 + " ms.");
    }
}

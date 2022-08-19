package com.fuhu.performance.map;

import java.util.ArrayList;
import java.util.List;

public class CorrelationRecordIdMeta {
    public CorrelationRecordIdMeta() {
        this(0);
    }
    public CorrelationRecordIdMeta(int initSize) {
        recordIds = initSize == 0 ? new ArrayList<>() : new ArrayList<>(initSize);
    }

    public int recordIdNum;
    public List<Long> recordIds;
    public void pushback(long recordId){
        recordIdNum++;
        recordIds.add(recordId);
    }

    public int getRecordIdNum() {
        return recordIdNum;
    }

    public List<Long> getRecordIds() {
        return recordIds;
    }

    public void clear(){
        recordIdNum = 0;
        recordIds.clear();
    }
}

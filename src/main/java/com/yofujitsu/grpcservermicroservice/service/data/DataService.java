package com.yofujitsu.grpcservermicroservice.service.data;

import com.yofujitsu.grpcservermicroservice.entity.Data;

import java.util.List;

public interface DataService {
    void handle(Data data);

    List<Data> getWithBatch(long batchSize);
}

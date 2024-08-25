package com.yofujitsu.grpcservermicroservice.service.grpc;

import com.google.protobuf.Timestamp;
import com.yofujitsu.grpccommon.AnalyticsServerGrpc;
import com.yofujitsu.grpccommon.GRPCAnalyticsRequest;
import com.yofujitsu.grpccommon.GRPCData;
import com.yofujitsu.grpccommon.ValueType;
import com.yofujitsu.grpcservermicroservice.entity.Data;
import com.yofujitsu.grpcservermicroservice.service.data.DataService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.ZoneOffset;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GRPCAnalyticsService extends AnalyticsServerGrpc.AnalyticsServerImplBase {

    private final DataService dataService;

    @Override
    public void askForData(GRPCAnalyticsRequest request, StreamObserver<GRPCData> responseObserver) {
        List<Data> data = dataService.getWithBatch(request.getBatchSize());
        for (Data d : data) {
            GRPCData dataRequest = GRPCData.newBuilder()
                    .setHeroId(d.getHeroId())
                    .setTimestamp(
                            Timestamp.newBuilder()
                                    .setSeconds(d.getTimestamp()
                                            .toEpochSecond(ZoneOffset.UTC))
                                    .build()
                    )
                    .setValueType(ValueType.valueOf(
                            d.getValueType().name())
                    )
                    .setValue(d.getValue())
                    .build();
            responseObserver.onNext(dataRequest);
        }
        log.info("Batch was sent");
        responseObserver.onCompleted();
    }
}

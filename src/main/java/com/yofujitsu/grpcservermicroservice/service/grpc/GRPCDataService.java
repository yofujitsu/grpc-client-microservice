package com.yofujitsu.grpcservermicroservice.service.grpc;

import com.google.protobuf.Empty;
import com.yofujitsu.grpccommon.DataServerGrpc;
import com.yofujitsu.grpccommon.GRPCData;
import com.yofujitsu.grpcservermicroservice.entity.Data;
import com.yofujitsu.grpcservermicroservice.service.data.DataService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GRPCDataService extends DataServerGrpc.DataServerImplBase {

    private final DataService dataService;

    /**
     * This method is called when a new GRPCData message is received. It creates a new Data object from the received message and
     * passes it to the handle method of the dataService. It then sends an empty response message back to the client.
     *
     * @param request           the GRPCData message received from the client
     * @param responseObserver  the observer object used to send responses back to the client
     */
    @Override
    public void addData(GRPCData request, StreamObserver<Empty> responseObserver) {
        // Create a new Data object from the received GRPCData message
        Data data = new Data(request);

        // Pass the newly created Data object to the handle method of the dataService
        dataService.handle(data);

        // Send an empty response message back to the client
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    /**
     * This method is called when a stream of GRPCData messages is received. It handles each message by creating a new Data object from it
     * and passing it to the handle method of the dataService. It then sends an empty response message back to the client when the stream
     * is completed.
     *
     * @param responseObserver  the observer object used to send responses back to the client
     * @return the StreamObserver used to handle the incoming stream of GRPCData messages
     */
    @Override
    public StreamObserver<GRPCData> addStreamOfData(StreamObserver<Empty> responseObserver) {
        return new StreamObserver<GRPCData>() {
            @Override
            public void onNext(GRPCData grpcData) {
                // Create a new Data object from the received GRPCData message
                Data data = new Data(grpcData);

                // Pass the newly created Data object to the handle method of the dataService
                dataService.handle(data);
            }

            @Override
            public void onError(Throwable throwable) {
                // If an error occurs during the stream, throw a RuntimeException with the error as the cause
                throw new RuntimeException(throwable);
            }

            @Override
            public void onCompleted() {
                // Send an empty response message back to the client
                responseObserver.onNext(Empty.newBuilder().build());
                responseObserver.onCompleted();
            }
        };
    }
}

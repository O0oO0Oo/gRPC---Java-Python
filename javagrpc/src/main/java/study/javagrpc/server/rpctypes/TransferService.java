package study.javagrpc.server.rpctypes;

import io.grpc.stub.StreamObserver;
import study.javagrpc.TransferRequest;
import study.javagrpc.TransferResponse;
import study.javagrpc.TransferServiceGrpc;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {
    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}

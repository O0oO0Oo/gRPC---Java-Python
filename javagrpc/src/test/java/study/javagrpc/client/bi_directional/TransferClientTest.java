package study.javagrpc.client.bi_directional;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.BankServiceGrpc;
import study.javagrpc.DepositRequest;
import study.javagrpc.TransferRequest;
import study.javagrpc.TransferServiceGrpc;
import study.javagrpc.client.client_streaming.BalanceStreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TransferClientTest {

    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",11111)
                .usePlaintext()
                .build();
        this.transferServiceStub = TransferServiceGrpc.newStub(managedChannel);
    }


    @Test
    @DisplayName("gRPC - Bi-Directional Streaming")
    public void transfer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // 클라이언트의 스트림 메시지 전송 동작을 정의한 옵져버
        StreamObserver<TransferRequest> observer = this.transferServiceStub.transfer(
                new TransferStreamingResponse(latch)
        );

        for (int i = 0; i < 10; i++) {
            TransferRequest request = TransferRequest.newBuilder()
                    .setFromAccount(
                            ThreadLocalRandom.current().nextInt(0, 99)
                    )
                    .setToAccount(
                            ThreadLocalRandom.current().nextInt(0, 99)
                    )
                    .setAmount(ThreadLocalRandom.current().nextInt(1, 10000))
                    .build();
            observer.onNext(request);
        }
        observer.onCompleted();
        latch.await();
    }
}

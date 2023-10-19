package study.javagrpc.client.client_streaming;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.BankServiceGrpc;
import study.javagrpc.DepositRequest;
import study.javagrpc.WithdrawRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",11111)
                .usePlaintext()
                .build();

        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }


    @Test
    @DisplayName("gRPC - Client Streaming Test")
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> observer = this.bankServiceStub.cashDeposit(
                new BalanceStreamObserver(latch)
        );
        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest =
                    DepositRequest.newBuilder()
                            .setAccountNumber(1)
                            .setAmount(1)
                            .build();
            observer.onNext(depositRequest);
        }
        observer.onCompleted();
        latch.await();
    }
}

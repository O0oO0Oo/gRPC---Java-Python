package study.javagrpc.client.server_streaming;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.BankServiceGrpc;
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
    @DisplayName("gRPC - Blocking Client Server-streaming Test")
    public void withdrawBlockingTest(){
        WithdrawRequest request = WithdrawRequest.newBuilder()
                .setAccountNumber(10)
                .setAmount(20)
                .build();
        bankServiceBlockingStub.withdraw(request)
                .forEachRemaining(
                        money -> {
                            System.out.println("money = " + money);
                        }
                );
    }

    @Test
    @DisplayName("gRPC - Async Client Server-streaming Test")
    public void withdrawAsyncTest(){
        WithdrawRequest request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(30)
                .build();

        this.bankServiceStub.withdraw(request, new MoneyStreamingResponse());

        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("gRPC - CountDownLatch Client Server-streaming Test")
    public void withdrawCountDownLatchTest() throws InterruptedException {
        WithdrawRequest request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(30)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.bankServiceStub.withdraw(request,
                new MoneyStreamingCountDownLatchResponse(
                        countDownLatch
                ));
        countDownLatch.await();
    }
}

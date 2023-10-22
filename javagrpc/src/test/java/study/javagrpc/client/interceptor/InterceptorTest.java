package study.javagrpc.client.interceptor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.BankServiceGrpc;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InterceptorTest {
    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6001)
                .intercept(new DeadlineInterceptor())
                .usePlaintext()
                .build();

        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }
}

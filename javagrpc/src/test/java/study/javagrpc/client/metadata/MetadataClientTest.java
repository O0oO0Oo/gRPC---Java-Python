package study.javagrpc.client.metadata;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.Balance;
import study.javagrpc.BalanceCheckRequest;
import study.javagrpc.BankServiceGrpc;
import study.javagrpc.client.interceptor.DeadlineInterceptor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataClientTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6004)
                .intercept(MetadataUtils.
                        newAttachHeadersInterceptor(
                                ClientConstants.getClientToken()))
                .usePlaintext()
                .build();

        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void metadata() {
        BalanceCheckRequest build = BalanceCheckRequest.newBuilder()
                .setAccountNumber(8).build();

        try {
            Balance balance = this.bankServiceBlockingStub
                    .withCallCredentials(
                            new UserSessionToken("user-token")
                    ).getBalance(build);

            System.out.println("balance = " + balance);
        } catch (StatusRuntimeException e) {
            //
            e.printStackTrace();
        }
    }
}

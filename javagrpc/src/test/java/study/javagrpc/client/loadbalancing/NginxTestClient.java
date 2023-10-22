package study.javagrpc.client.loadbalancing;

import io.grpc.EquivalentAddressGroup;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.Balance;
import study.javagrpc.BalanceCheckRequest;
import study.javagrpc.BankServiceGrpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxTestClient {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("192.168.35.221", 80)
                .usePlaintext().build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
    }

    @Test
    public void balanceTest() {
        for (int i = 0; i < 11; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(10)
                    .build();

            Balance balance = this.blockingStub.
                    getBalance(balanceCheckRequest);
            System.out.println("balance = " + balance);
        }
    }
}

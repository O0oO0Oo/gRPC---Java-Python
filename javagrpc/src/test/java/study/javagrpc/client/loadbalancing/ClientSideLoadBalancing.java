package study.javagrpc.client.loadbalancing;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import study.javagrpc.Balance;
import study.javagrpc.BalanceCheckRequest;
import study.javagrpc.BankServiceGrpc;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientSideLoadBalancing {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup() {
        ServiceRegistry.register("bank-service", List.of(
                "localhost:6001","localhost:6002","localhost:6003"
        ));
        NameResolverRegistry.getDefaultRegistry().register(
                new TempNamedResolverProvider()
        );

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forTarget("http://bank-service")
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext().build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
    }

    @Test
    public void balanceTest() {
        for (int i = 0; i < 11; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(10)
                    .build();

            Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
            System.out.println("Received = " + balance.getAmount());
        }

    }
}

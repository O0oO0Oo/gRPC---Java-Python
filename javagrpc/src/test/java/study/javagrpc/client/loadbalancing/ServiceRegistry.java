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

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceRegistry {
    private static final Map<String, List<EquivalentAddressGroup>> MAP = new HashMap<>();

    public static void register(String service, List<String> instance) {
        List<EquivalentAddressGroup> addressGroupsList = instance.stream()
                .map(i -> i.split(":"))
                .map(a -> new InetSocketAddress(a[0], Integer.parseInt(a[1])))
                .map(EquivalentAddressGroup::new)
                .collect(Collectors.toList());
        MAP.put(service, addressGroupsList);
    }

    public static List<EquivalentAddressGroup> getInstance(String service) {
        return MAP.get(service);
    }
}

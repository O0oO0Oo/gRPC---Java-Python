package study.javagrpc.client.loadbalancing;

import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

import java.util.List;

public class TempNameResolver extends NameResolver {

    private final String service;

    public TempNameResolver(String service) {
        this.service = service;
    }

    @Override
    public String getServiceAuthority() {
        return "temp";
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void refresh() {
        super.refresh();
    }

    // NameResolver 의 시작
    @Override
    public void start(Listener2 listener) {
        List<EquivalentAddressGroup> instance = ServiceRegistry.getInstance(this.service);
        ResolutionResult build = ResolutionResult.newBuilder()
                .setAddresses(instance)
                .build();
        listener.onResult(build);
    }
}

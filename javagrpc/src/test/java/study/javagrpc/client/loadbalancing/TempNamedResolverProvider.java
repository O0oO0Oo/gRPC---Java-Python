package study.javagrpc.client.loadbalancing;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;

public class TempNamedResolverProvider extends NameResolverProvider {

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }

    // grpc now includes a separate name resolved for dns. Lets keep ours as http.
    // Also look for getAuthroity() to find a service instead of toString.
    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        System.out.println(
                "Looking for service : " + targetUri.toString()
        );

        return new TempNameResolver(targetUri.getAuthority());
    }

    @Override
    public String getDefaultScheme() {
        return "http";
    }
}

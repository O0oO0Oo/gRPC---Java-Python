package study.javagrpc.client.metadata;

import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.concurrent.Executor;

public class UserSessionToken extends CallCredentials {

    private String jwt;

    public UserSessionToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier metadataApplier) {
        appExecutor.execute(() ->{
            Metadata metadata = new Metadata();
            metadata.put(ClientConstants.USER_TOKEN, this.jwt);
            metadataApplier.apply(metadata);
            // metadataApplier.fail();
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }
}

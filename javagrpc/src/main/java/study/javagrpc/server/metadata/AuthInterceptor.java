package study.javagrpc.server.metadata;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String clientToken = metadata.get(ServerConstants.USER_TOKEN);
        if (this.validate(clientToken)) {
            //
            return serverCallHandler.startCall(serverCall, metadata);
        } else {
            Status status = Status.UNAUTHENTICATED.withDescription("invalid token/expired token.");
            serverCall.close(status, metadata);
        }

        // null 을 보내면 에러가 발생함 io.grpc.internal.SerializingExecutor run
        return new ServerCall.Listener<ReqT>() {};
    }

    private boolean validate(String token) {
        return Objects.nonNull(token) &&
                (token.equals("bank-client-secret") || token.equals("user-token"));
    }
}

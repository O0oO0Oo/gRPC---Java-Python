package study.javagrpc.server.metadata;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import study.javagrpc.server.rpctypes.BankService;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6004)
                .addService(new BankService())
                .intercept(new AuthInterceptor())
                .build();

        server.start();
        server.awaitTermination();
    }
}
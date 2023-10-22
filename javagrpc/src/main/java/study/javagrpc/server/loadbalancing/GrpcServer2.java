package study.javagrpc.server.loadbalancing;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import study.javagrpc.server.rpctypes.BankService;

import java.io.IOException;

public class GrpcServer2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6002)
                .addService(new BankService())
                .build();

        server.start();
        server.awaitTermination();
    }
}

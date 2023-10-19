package study.javagrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import study.javagrpc.server.BankService;

import java.io.IOException;

@SpringBootApplication
public class JavagrpcApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		Server server = ServerBuilder.forPort(11111)
				.addService(new BankService())
				.build();

		server.start();
		server.awaitTermination();
	}
}

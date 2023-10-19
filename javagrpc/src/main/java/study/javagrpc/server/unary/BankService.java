package study.javagrpc.server.unary;

import io.grpc.stub.StreamObserver;
import study.javagrpc.Balance;
import study.javagrpc.BalanceCheckRequest;
import study.javagrpc.BankServiceGrpc;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(accountNumber * 10)
                .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }
}

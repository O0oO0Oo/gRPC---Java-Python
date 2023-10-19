package study.javagrpc.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import study.javagrpc.*;

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

    // Server streaming

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();

        // validation / error handling
        if (amount > 100) {
            Status status = Status.FAILED_PRECONDITION
                    .withDescription("amount should be less than 100");
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        for (int i = 0; i < amount / 10; i++) {
            Money money = Money.newBuilder().setValue(10).build();

            responseObserver.onNext(money);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
}

package study.javagrpc.server.rpctypes;

import io.grpc.stub.StreamObserver;
import study.javagrpc.Balance;
import study.javagrpc.DepositRequest;

public class CashStreamingRequest implements StreamObserver<DepositRequest> {

    private StreamObserver<Balance> balanceStreamObserver;
    private static int accountBalance = 100;

    public CashStreamingRequest(StreamObserver<Balance> balanceStreamObserver) {
        this.balanceStreamObserver = balanceStreamObserver;
    }

    @Override
    public void onNext(DepositRequest value) {
        int accountNumber = value.getAccountNumber();
        int amount = value.getAmount();

        this.accountBalance += amount;
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {
        Balance balance = Balance.newBuilder()
                .setAmount(accountBalance)
                .build();

        this.balanceStreamObserver.onNext(balance);
        this.balanceStreamObserver.onCompleted();
    }
}

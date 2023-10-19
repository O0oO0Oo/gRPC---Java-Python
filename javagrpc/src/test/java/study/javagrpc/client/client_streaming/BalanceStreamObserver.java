package study.javagrpc.client.client_streaming;

import io.grpc.stub.StreamObserver;
import study.javagrpc.Balance;

import java.util.concurrent.CountDownLatch;

public class BalanceStreamObserver implements StreamObserver<Balance> {

    private CountDownLatch countDownLatch;

    public BalanceStreamObserver(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(Balance value) {
        System.out.println(
                "Final Balance : " + value.getAmount()
        );
    }

    @Override
    public void onError(Throwable t) {
        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Server is Done!"
        );
        countDownLatch.countDown();
    }
}

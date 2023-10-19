package study.javagrpc.client.server_streaming;

import io.grpc.stub.StreamObserver;
import study.javagrpc.Money;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingCountDownLatchResponse implements StreamObserver<Money> {

    private CountDownLatch latch;

    public MoneyStreamingCountDownLatchResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money value) {
        System.out.println("Received async : " + value.getValue());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(
                t.getMessage()
        );
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Server is Done."
        );
        latch.countDown();
    }
}

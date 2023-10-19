package study.javagrpc.client.server_streaming;

import io.grpc.stub.StreamObserver;
import study.javagrpc.Money;

public class MoneyStreamingResponse implements StreamObserver<Money> {
    @Override
    public void onNext(Money value) {
        System.out.println("Received async : " + value.getValue());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(
                t.getMessage()
        );
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Server is Done."
        );
    }
}

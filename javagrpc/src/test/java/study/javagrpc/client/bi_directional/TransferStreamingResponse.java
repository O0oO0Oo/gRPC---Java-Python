package study.javagrpc.client.bi_directional;

import io.grpc.stub.StreamObserver;
import study.javagrpc.TransferResponse;

import java.util.concurrent.CountDownLatch;

// 클라이언트의 스트림 메시지 전송 로직 정의
public class TransferStreamingResponse implements StreamObserver<TransferResponse> {

    private CountDownLatch latch;

    public TransferStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(TransferResponse value) {
        System.out.println("Status : " + value.getStatus());

        value.getAccountList()
                .stream()
                .map(
                        account ->
                            account.getAccountNumber() + " : " + account.getAmount()
                ).forEach(System.out::println);
        System.out.println("-------------------------------");
    }

    @Override
    public void onError(Throwable t) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "All transfers done."
        );
        this.latch.countDown();
    }
}

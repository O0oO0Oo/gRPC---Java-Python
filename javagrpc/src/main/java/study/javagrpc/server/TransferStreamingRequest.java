package study.javagrpc.server;

import io.grpc.stub.StreamObserver;
import study.javagrpc.Account;
import study.javagrpc.TransferRequest;
import study.javagrpc.TransferResponse;
import study.javagrpc.TransferStatus;

import java.util.HashMap;
import java.util.Map;

// 서버의 스트림 메시지 동작 정의
public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferRequestStreamObserver;

    private static Map<Integer, Integer> accountMap = new HashMap<>();

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferRequestStreamObserver) {
        this.transferRequestStreamObserver = transferRequestStreamObserver;

        for (int i = 0; i < 100; i++) {
            this.accountMap.put(Integer.valueOf(i), Integer.valueOf(10000));
        }
    }

    @Override
    public void onNext(TransferRequest value) {
        int fromAccount = value.getFromAccount();
        int toAccount = value.getToAccount();
        int amount = value.getAmount();

        int balance = accountMap.get(fromAccount);
        
        // 잔고 부족, 번호 같으면 에러
        TransferStatus status = TransferStatus.FAILED;
        if (balance >= amount && fromAccount != toAccount) {
            accountMap.put(Integer.valueOf(fromAccount),Integer.valueOf(accountMap.get(fromAccount) - amount));
            accountMap.put(Integer.valueOf(toAccount),Integer.valueOf(accountMap.get(toAccount) + amount));
            status = TransferStatus.SUCCESS;

            System.out.println(
                    "fromAccount 의 잔고 : " + accountMap.get(Integer.valueOf(fromAccount))
            );
        }

        Account from = Account.newBuilder()
                .setAccountNumber(fromAccount)
                .setAmount(accountMap.get(Integer.valueOf(fromAccount)))
                .build();
        Account to = Account.newBuilder()
                .setAccountNumber(toAccount)
                .setAmount(accountMap.get(Integer.valueOf(toAccount)))
                .build();
        TransferResponse response = TransferResponse.newBuilder()
                .setStatus(status)
                .addAccount(from)
                .addAccount(to)
                .build();
        this.transferRequestStreamObserver.onNext(response);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {
        this.transferRequestStreamObserver.onCompleted();
    }
}

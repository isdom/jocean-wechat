package org.jocean.wechat.sign;

import org.jocean.http.Interact;
import org.jocean.http.InteractPipe;
import org.springframework.beans.factory.annotation.Value;

import rx.Observable;

public class PKSigner implements InteractPipe {

    @Override
    public Observable<Interact> call(final Observable<Interact> interacts) {
        return interacts.doOnNext(interact ->
        interact.reqtransformer(SignerV3.signRequest(_merchantId, _privateKeySerialNumber, _privateKeyAsPem)));
    }

    @Value("${merchant_id}")
    public String _merchantId;

    @Value("${private.key.serial_no}")
    public String _privateKeySerialNumber;

    @Value("${private.key.pem}")
    public String _privateKeyAsPem;
}

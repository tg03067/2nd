package org.example.second.signature;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.second.signature.model.SignReq;
import org.example.second.signature.model.VerifyReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.*;

@RestController
@RequestMapping("/api/signature")
@Tag(name = "서명 아님" , description = "서명아님")
public class DigitalSignatureController {
    private DigitalSignatureService digitalSignatureService;

    @Autowired
    public void SignatureController(DigitalSignatureService digitalSignatureService) {
        this.digitalSignatureService = digitalSignatureService;
    }

    public DigitalSignatureController(DigitalSignatureService digitalSignatureService) {
        this.digitalSignatureService = digitalSignatureService;
    }

    @PostMapping("/sign")
    @Operation(summary = "데이터 서명", description = "데이터와 개인 키를 받아 서명합니다.")
    public ResponseEntity<String> signData(@RequestBody SignReq request) throws Exception {
        String data = request.getData();
        String privateKey = request.getPrivateKey();

        if (data == null || privateKey == null) {
            throw new IllegalArgumentException("Data or PrivateKey cannot be null");
        }

        String signature = digitalSignatureService.sign(data, privateKey);
        return ResponseEntity.ok(signature);
    }

    @PostMapping("/verify")
    @Operation(summary = "서명 검증", description = "데이터와 서명을 받아 검증합니다.")
    public ResponseEntity<Boolean> verifySignature(@RequestBody VerifyReq request) throws Exception {
        String data = request.getData();
        String signatureString = request.getSignature();

        if (data == null || signatureString == null) {
            throw new IllegalArgumentException("Data or Signature cannot be null");
        }

        PublicKey publicKey = digitalSignatureService.getPublicKey();
        boolean isValid = digitalSignatureService.verify(data, signatureString, publicKey);
        return ResponseEntity.ok(isValid);
    }

}

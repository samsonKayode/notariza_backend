package com.backend.notariza.util;

public class DocumentResourcesListPojo {

    private byte[] signature;
    private byte[] stamp1;
    private byte[] redSeal;
    private byte[] plainSeal;
    private byte[] ctc;

    public DocumentResourcesListPojo(byte[] signature, byte[] stamp1, byte[] redSeal, byte[] plainSeal, byte[] ctc) {
        this.signature = signature;
        this.stamp1 = stamp1;
        this.redSeal = redSeal;
        this.plainSeal = plainSeal;
        this.ctc = ctc;
    }

    public DocumentResourcesListPojo() {

    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getStamp1() {
        return stamp1;
    }

    public void setStamp1(byte[] stamp1) {
        this.stamp1 = stamp1;
    }

    public byte[] getRedSeal() {
        return redSeal;
    }

    public void setRedSeal(byte[] redSeal) {
        this.redSeal = redSeal;
    }

    public byte[] getCtc() {
        return ctc;
    }

    public void setCtc(byte[] ctc) {
        this.ctc = ctc;
    }

    public byte[] getPlainSeal() {
        return plainSeal;
    }

    public void setPlainSeal(byte[] plainSeal) {
        this.plainSeal = plainSeal;
    }
}

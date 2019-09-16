package com.heylichen.java.io.socket.framing;

import java.io.IOException;

public interface VoteRequestCodec {

    byte[] encode(VoteRequest request) throws IOException;

    VoteRequest decodeRequest(byte[] bytes) throws IOException;
}

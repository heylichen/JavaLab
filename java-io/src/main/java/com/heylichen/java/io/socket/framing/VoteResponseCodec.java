package com.heylichen.java.io.socket.framing;

import java.io.IOException;

public interface VoteResponseCodec {
    byte[] encode(VoteResponse request) throws IOException;

    VoteResponse decodeResponse(byte[] bytes) throws IOException;
}

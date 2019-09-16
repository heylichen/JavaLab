package com.heylichen.java.io.socket.framing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequest {
    public static final int QUERY = 0;
    private static final int VOTE = 1;
    private int type;
    private int candidateId;
}

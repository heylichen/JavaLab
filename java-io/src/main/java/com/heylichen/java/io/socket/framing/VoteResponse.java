package com.heylichen.java.io.socket.framing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteResponse {
    private int candidateId;
    private long voteCount;
}

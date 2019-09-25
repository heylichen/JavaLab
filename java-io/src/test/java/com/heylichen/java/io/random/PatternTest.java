package com.heylichen.java.io.random;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PatternTest {
    private Pattern simplePattern = Pattern.compile("[A-Z][a-z][0-9]");

    @Test
    public void testMatch() {
        Pattern p = Pattern.compile("\\.");
        String[] pieces = p.split("hello.world.this.is", 3);
        for (String piece : pieces) {
            System.out.println(piece);
        }

        Pattern p2 = Pattern.compile("[A-Z][a-z][0-9]");
        Matcher m2 = p2.matcher("Ab1-");
        logMatcher(m2, m2.matches());
        logMatcher(m2, m2.lookingAt());
        logMatcher(m2, m2.find());
    }

    private void logMatcher(Matcher m2, boolean success) {
        if (success) {
            log.info("match:{} {} {} {}", success, m2.start(), m2.end(), m2.group());
        } else {
            log.info("NOT Match");
        }
    }

    @Test
    public void testFind() {
        Matcher m = simplePattern.matcher("Ab1dededeAb2mjdj12jjjAb3dededAb4");
        while (m.find()) {
            log.info("found substring({},{}) {}", m.start(), m.end(), m.group());
        }
    }

    @Test
    public void testRegion() {
        Matcher m = simplePattern.matcher("Ab1dededeAb2mjdj12jjjAb3dededAb4");
        m.region(0, 24);
        log.info("region ({},{})", m.regionStart(), m.regionEnd());
        while (m.find()) {
            log.info("found substring({},{}) {}", m.start(), m.end(), m.group());
        }

        "111".getBytes(StandardCharsets.UTF_8);
    }
}

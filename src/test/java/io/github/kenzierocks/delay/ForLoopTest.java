package io.github.kenzierocks.delay;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ForLoopTest {
    private String[] forItr;

    @Before
    public void setup() {
        forItr = new String[] { "hello", "world", "!" };
    }

    @Test
    public void testForEach() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        ForLoop<String> loop =
                ForLoop.forEach(forItr,
                        (string, loopRef) -> forEachBuild.add(string));
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertArrayEquals(forItr, forEachBuild.toArray(forItr.clone()));
    }
}

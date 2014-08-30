package io.github.kenzierocks.delay;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void testForEachArray() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        ForLoop<String> loop =
                ForLoop.forEach(forItr,
                        (string, loopRef) -> forEachBuild.add(string));
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertArrayEquals(forItr, forEachBuild.toArray(forItr.clone()));
    }

    @Test
    public void testForEachIterable() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        List<String> forEachList = Arrays.asList(forItr);
        ForLoop<String> loop =
                ForLoop.forEach(forEachList,
                        (string, loopRef) -> forEachBuild.add(string));
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertEquals(forEachList, forEachBuild);
    }

    @Test
    public void testForEachIterableWithBreak() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        List<String> forEachList = Arrays.asList(forItr);
        ForLoop<String> loop =
                ForLoop.forEach(forEachList,
                        (string, loopRef) -> loopRef._break());
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertEquals(Arrays.asList(), forEachBuild);
    }

    @Test
    public void testForEachIterableWithContinue() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        List<String> forEachList = Arrays.asList(forItr);
        ForLoop<String> loop =
                ForLoop.forEach(forEachList,
                        (string, loopRef) -> loopRef._continue());
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertEquals(Arrays.asList(), forEachBuild);
    }

    @Test
    public void testForEachArrayWithBreak() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        ForLoop<String> loop =
                ForLoop.forEach(forItr, (string, loopRef) -> loopRef._break());
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertEquals(Arrays.asList(), forEachBuild);
    }

    @Test
    public void testForEachArrayWithContinue() {
        List<String> forEachBuild = new ArrayList<>(forItr.length);
        ForLoop<String> loop =
                ForLoop.forEach(forItr,
                        (string, loopRef) -> loopRef._continue());
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertEquals(Arrays.asList(), forEachBuild);
    }

    @Test
    public void testFor1To10() {
        List<Integer> _1To10 = new ArrayList<>(10);
        ForLoop<Integer> loop =
                ForLoop.normalCounterLoop(1, 11, (i, loopRef) -> _1To10.add(i));
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertArrayEquals(
                new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
                _1To10.toArray(new Integer[0]));
    }

    @Test
    public void testFor1To5Break() {
        List<Integer> _1To5 = new ArrayList<>(5);
        ForLoop<Integer> loop =
                ForLoop.normalCounterLoop(1, 11, (i, loopRef) -> {
                    if (i.intValue() > 5)
                        loopRef._break();
                    _1To5.add(i);
                });
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 },
                _1To5.toArray(new Integer[0]));
    }

    @Test
    public void testFor1And10Continue() {
        List<Integer> _1And10Continue = new ArrayList<>(2);
        ForLoop<Integer> loop =
                ForLoop.normalCounterLoop(1, 11, (i, loopRef) -> {
                    if (i.intValue() != 1 && i.intValue() != 10)
                        loopRef._continue();
                    _1And10Continue.add(i);
                });
        while (loop.hasNext()) {
            loop.next();
        }
        Assert.assertArrayEquals(new Integer[] { 1, 10 },
                _1And10Continue.toArray(new Integer[0]));
    }
}

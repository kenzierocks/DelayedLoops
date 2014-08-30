package io.github.kenzierocks.delay;

import io.github.kenzierocks.delay.exceptionalcontrol.ControlException;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForLoop<ObjectType> {
    public static final class ForBreakException extends ControlException {
        private static final long serialVersionUID = 768884027407360637L;

        public ForBreakException() {
            super();
        }

        public ForBreakException(String label) {
            super(label);
        }
    }

    public static final class ForContinueException extends ControlException {
        private static final long serialVersionUID = 768884027407360637L;

        public ForContinueException() {
            super();
        }

        public ForContinueException(String label) {
            super(label);
        }
    }

    private static final class NothingLoop<T> extends ForLoop<T> {
        public NothingLoop(String label) {
            super(loop -> {
            }, loop -> {
                return Boolean.FALSE;
            }, loop -> {
            }, loop -> {
                return null;
            }, (x, loop) -> {
            }, label);
        }

        public NothingLoop() {
            this(ControlException.DEFAULT_LABEL);
        }
    }

    public static final ForLoop<Integer> normalCounterLoop(int start, int max,
            BiConsumer<Integer, ForLoop<Integer>> body) {
        return counterLoopWithStep(start, max, 1, body);
    }

    public static final ForLoop<Integer> backwardsCounterLoop(int start,
            int min, BiConsumer<Integer, ForLoop<Integer>> body) {
        return counterLoopWithStep(start, min, -1, body);
    }

    public static final ForLoop<Integer> counterLoopWithStep(int start,
            int limit, int step, BiConsumer<Integer, ForLoop<Integer>> body) {
        return new ForLoop<>(loop -> loop.setCounter(start),
                loop -> step > 0 ? loop.getCounter() < limit : loop
                        .getCounter() >= limit,
                loop -> loop.setCounter(loop.getCounter() + step),
                loop -> loop.getCounter(), body);
    }

    public static final <T> ForLoop<T> forEach(Iterable<T> iterOver,
            BiConsumer<T, ForLoop<T>> body) {
        Iterator<T> itr = iterOver.iterator();
        if (!itr.hasNext()) {
            // special empty case
            return new NothingLoop<T>();
        }
        boolean[] hadNextWrap = { itr.hasNext() };
        return new ForLoop<>(loop -> {
        }, loop -> hadNextWrap[0], loop -> {
            hadNextWrap[0] = itr.hasNext();
        }, loop -> itr.next(), body);
    }

    public static final <T> ForLoop<T> forEach(T[] iterOver,
            BiConsumer<T, ForLoop<T>> body) {
        return new ForLoop<>(loop -> loop.setCounter(0),
                loop -> loop.getCounter() < iterOver.length,
                loop -> loop.setCounter(loop.getCounter() + 1),
                loop -> iterOver[loop.getCounter()], body);
    }

    private final Function<ForLoop<ObjectType>, Boolean> continueOkay;
    private final Function<ForLoop<ObjectType>, ObjectType> supplier;
    private final Consumer<ForLoop<ObjectType>> updateState;
    private final BiConsumer<ObjectType, ForLoop<ObjectType>> loopBody;
    private final String label;

    ForLoop(Consumer<ForLoop<ObjectType>> init,
            Function<ForLoop<ObjectType>, Boolean> expr,
            Consumer<ForLoop<ObjectType>> update,
            Function<ForLoop<ObjectType>, ObjectType> supplierOfType,
            BiConsumer<ObjectType, ForLoop<ObjectType>> body, String label) {
        continueOkay = expr;
        supplier = supplierOfType;
        updateState = update;
        loopBody = body;
        this.label = label;
        init.accept(this);
    }

    ForLoop(Consumer<ForLoop<ObjectType>> init,
            Function<ForLoop<ObjectType>, Boolean> expr,
            Consumer<ForLoop<ObjectType>> update,
            Function<ForLoop<ObjectType>, ObjectType> supplierOfType,
            BiConsumer<ObjectType, ForLoop<ObjectType>> body) {
        this(init, expr, update, supplierOfType, body,
                ControlException.DEFAULT_LABEL);
    }

    private int counter;

    public void setCounter(int newVal) {
        counter = newVal;
    }

    public int getCounter() {
        return counter;
    }

    public Function<ForLoop<ObjectType>, Boolean> getContinueOkay() {
        return continueOkay;
    }

    public Consumer<ForLoop<ObjectType>> getUpdateState() {
        return updateState;
    }

    public BiConsumer<ObjectType, ForLoop<ObjectType>> getLoopBody() {
        return loopBody;
    }

    public Function<ForLoop<ObjectType>, ObjectType> getSupplier() {
        return supplier;
    }

    private boolean broken = false;

    public boolean hasNext() {
        return continueOkay.apply(this) && !broken;
    }

    public void _continue() {
        throw new ForContinueException(label);
    }

    public void _break() {
        broken = true;
        throw new ForBreakException(label);
    }

    public void next() {
        // new state okay
        if (hasNext()) {
            ObjectType res = supplier.apply(this);
            try {
                loopBody.accept(res, this);
            } catch (ControlException e) {
                if (e instanceof ForBreakException) {
                    ForBreakException _break = (ForBreakException) e;
                    if (_break.getLabel().equals(label)) {
                        // no-op
                        // return: break doesn't need to update
                        return;
                    } else {
                        // re-throw
                        throw _break;
                    }
                } else if (e instanceof ForContinueException) {
                    ForContinueException _continue = (ForContinueException) e;
                    if (_continue.getLabel().equals(label)) {
                        // no-op
                        // no return: continue needs to update
                    } else {
                        // re-throw
                        throw _continue;
                    }
                }
            }
            updateState.accept(this);
        }
    }
}

package soda.streaming.metrics;

public abstract class AbstractMetric<T extends AbstractMetric<T>> {
    public final String name;

    protected AbstractMetric(String name) {
        this.name = name;
    }

    protected abstract T create();
}



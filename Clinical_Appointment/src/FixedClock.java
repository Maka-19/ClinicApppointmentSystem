public class FixedClock implements Clock {

    private int hour;

    public FixedClock(int hour) {
        this.hour = hour;
    }

    @Override
    public int now() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
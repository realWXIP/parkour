package lv.cmnts.Parkour;

public enum EventType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int players;
    EventType(int players) {
        this.players = players;
    }

    private int skaits() { return players; }
}

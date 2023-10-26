import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    private final Song song = new Song("Smooth Operator",
            "Sade",
            "1994",
            "2",
            "16",
            "338003491");

    @org.junit.jupiter.api.Test
    void trackName() {

        assertEquals("Smooth Operator", song.trackName());

    }

    @org.junit.jupiter.api.Test
    void artistName() {

        assertEquals("Sade", song.artistName());
    }

    @org.junit.jupiter.api.Test
    void releasedYear() {

        assertEquals("1994", song.releasedYear());
    }

    @org.junit.jupiter.api.Test
    void releasedMonth() {

        assertEquals("2", song.releasedMonth());
    }

    @org.junit.jupiter.api.Test
    void releasedDay() {

        assertEquals("16", song.releasedDay());
    }

    @org.junit.jupiter.api.Test
    void totalNumberOfStreamsOnSpotify() {

        assertEquals("338003491", song.totalNumberOfStreamsOnSpotify());
    }

    @org.junit.jupiter.api.Test
    void toStringTest() {

        assertFalse(song.toString().isBlank());
        assertEquals("track name = Smooth Operator, artist = Sade, release year = 1994, release month = 2, release day = 16, streams = 338003491", song.toString());


    }
}
/**
 * Song record that is used to create Song objects.
 *
 * @author Bennett Fife
 * @version 10/25/23
 */
public record Song(String trackName, String artistName, String releasedYear, String releasedMonth, String releasedDay,
                   String totalNumberOfStreamsOnSpotify) implements Comparable<Song> {
    /**
     * Song constructor to create a song object
     *
     * @param trackName                     String name of the song; must not be null or empty
     * @param artistName                    String name of the artist; must not be null or empty
     * @param releasedYear                  String year of song release; must not be null or empty
     * @param releasedMonth                 String month of song release; must not be null or empty
     * @param releasedDay                   String day of song release; must not be null or empty
     * @param totalNumberOfStreamsOnSpotify String number of spotify streams; must not be null or empty
     */
    public Song {
        if (trackName == null || trackName.isBlank()) {
            throw new IllegalArgumentException("artist name must not be null or empty");
        }
        if (artistName == null || artistName.isBlank()) {
            throw new IllegalArgumentException("artist name must not be null or empty");
        }
        if (releasedYear == null || releasedYear.isBlank()) {
            throw new IllegalArgumentException("release year must not be null or empty");
        }
        if (releasedMonth == null || releasedMonth.isBlank()) {
            throw new IllegalArgumentException("release month must not be null or empty");
        }
        if (releasedDay == null || releasedDay.isBlank()) {
            throw new IllegalArgumentException("release day must not be null or empty");
        }
        if (totalNumberOfStreamsOnSpotify == null || totalNumberOfStreamsOnSpotify.isBlank()) {
            throw new IllegalArgumentException("number of streams must not be null or empty");
        }

    }

    /**
     * A comepareTo Override that compares two song's trackName (used for sorting)
     *
     * @param other song to be compared.
     * @return Returns a negative integer, zero, or a positive integer as this song's trackName is less than, equal to, or greater than the specified song.
     */
    @Override
    public int compareTo(Song other) {
        return this.trackName.compareTo(other.trackName);
    }

    /**
     * Retrieves a representation of basic song information
     * @return string representation of the song object
     */
    @Override
    public String toString() {
        return String.format("track name = %s, artist = %s, release year = %s, release month = %s, release day = %s, streams = %s", trackName, artistName, releasedYear, releasedMonth, releasedDay, totalNumberOfStreamsOnSpotify);
    }
}
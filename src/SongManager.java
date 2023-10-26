import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * SongManager class that manages songs and year of release data.
 * It also provides methods that let clients access song and year data.
 *
 * @author Bennett Fife
 * @version 10/25/23
 */


public class SongManager implements SongManagerInterface {

    private final Song[][] songs;
    private final String[] releaseYears;

    /**
     * SongManager constructor pulls song/year data from files, places them in arrays, and sorts the songs.
     **/
    public SongManager() {

        String yearsCountPath = "count-by-release-year.csv";
        String spotifySongsPath = "spotify-2023.csv";

        try {
            CSVReader countByReleaseCSV = new CSVReader(new FileReader(yearsCountPath));
            CSVReader spotify = new CSVReader(new FileReader(spotifySongsPath));
            String[] firstLine = countByReleaseCSV.readNext();
            int yearCount = Integer.parseInt(firstLine[0]); //pull the number of years data from the first line
            countByReleaseCSV.readNext();
            songs = new Song[yearCount][]; //set the rows of the jagged array
            releaseYears = new String[yearCount];

            int yearIterator = 0;
            String[] yearsLine;

            //iterate through 'count-by-release-year.csv'
            while ((yearsLine = countByReleaseCSV.readNext()) != null) {
                //fill releaseYears with years (String) using csv data
                releaseYears[yearIterator] = yearsLine[0];
                //set each row length of the 2d array using csv data
                songs[yearIterator] = new Song[Integer.parseInt(yearsLine[1])];
                yearIterator++;
            }

            //iterate through 'spotify.csv'
            while ((spotify.readNext()) != null) {
                //iterate through each index of the 2d array
                for (int row = 0; row < getYearCount(); row++) {
                    for (int col = 0; col < getSongCount(row); col++) {
                        //temp String[] of specific song data
                        String[] songLine = spotify.readNext();
                        //create a new Song object at each index of the 2d array using songLine data
                        songs[row][col] = new Song(songLine[0], songLine[1], songLine[3], songLine[4], songLine[5], songLine[8]);
                    }
                }

            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        //sort songs by track name for each year
        sortSongs();
    }

    /**
     * Retrieves the count of release years
     *
     * @return count of release years
     */
    @Override
    public int getYearCount() {
        return songs.length;
    }

    /**
     * Retrieves the number of songs in the specified release year (by index)
     *
     * @param yearIndex the index of the release year
     * @return song count in that release year
     */
    @Override
    public int getSongCount(int yearIndex) {
        if (yearIndex < 0 || yearIndex > getYearCount() - 1) {
            throw new IllegalArgumentException("year must be a valid index in the range 0 to count - 1");
        }

        return songs[yearIndex].length;
    }

    /**
     * Retrieves the number of songs in all release years
     *
     * @return song count in all release years
     */
    @Override
    public int getSongCount() {
        int countTotal = 0;
        for (Song[] songsInRow : songs) {
            countTotal += songsInRow.length;
        }
        return countTotal;
    }

    /**
     * Retrieves the song number of a track name starting from the beginning of Song[][] songs
     *
     * @param trackName name of the track to search for
     * @return song number from every song within Song[][] songs, or -1 if the track isn't found
     */
    public int getSongNum(String trackName) {
        int num = 0;
        for (int row = 0; row < getYearCount(); row++) {
            for (int col = 0; col < getSongCount(row); col++) {
                num++;
                if (trackName.equals(getSong(row, col).trackName())) {
                    return num;
                }
            }
        }
        return -1;
    }

    /**
     * Retrieves the release year at the specified index
     *
     * @param yearIndex index of the desired release year
     * @return release year
     */
    @Override
    public String getYearName(int yearIndex) {
        if (yearIndex < 0 || yearIndex > getYearCount() - 1) {
            throw new IllegalArgumentException("year must be a valid index in the range 0 to count - 1");
        }
        return releaseYears[yearIndex];
    }

    /**
     * Helper method to find the index of a specific year
     *
     * @param stringArray array being used to iterate
     * @param target      target string to find in the array
     * @return the index of the array where the String is found; -1 if the target is not found.
     */
    public int findStringIndex(String[] stringArray, String target) {
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].equals(target)) {
                return i; // Return the index where the target string is found
            }
        }
        return -1; // Return -1 if the target string is not found in the array
    }

    /**
     * Retrieves the number of songs in the specified release year (by name)
     *
     * @param year the release year
     * @return song count in that release year
     */
    @Override
    public int getSongCount(String year) {
        int index = findStringIndex(releaseYears, year);
        if (index == -1) {
            System.out.print("The year doesn't exist in our data");
            return 0;
        } else {
            return getSongCount(index);
        }
    }

    /**
     * Retrieves the song at the specific release year and song index
     *
     * @param yearIndex release year index
     * @param songIndex song index
     * @return song at that array position
     */
    @Override
    public Song getSong(int yearIndex, int songIndex) {
        if (yearIndex < 0 || yearIndex > getYearCount() - 1) {
            throw new IllegalArgumentException("year must be a valid index in the range 0 to count - 1");
        }
        if (songIndex < 0 || songIndex > getSongCount(yearIndex) - 1) {
            throw new IllegalArgumentException("song must be a valid index in the range 0 to count - 1");
        }
        return songs[yearIndex][songIndex];
    }

    /**
     * Retrieves a copy of the song array for the release year at the specified
     * index
     *
     * @param yearIndex release year index
     * @return copy of song array (not a reference to the internal one)
     */
    @Override
    public Song[] getSongs(int yearIndex) {
        if (yearIndex < 0 || yearIndex > getYearCount() - 1) {
            throw new IllegalArgumentException("year must be a valid index in the range 0 to count - 1");
        }
        return Arrays.copyOf(songs[yearIndex], songs[yearIndex].length);
    }

    /**
     * Retrieves the first release year index associated with the specified song's
     * track name
     *
     * @param trackName the track name to search for
     * @return the first release year index containing the specified
     * song, or -1 if not found
     */
    @Override
    public int findSongYear(String trackName) {
        for (int row = 0; row < getYearCount(); row++) {
            for (int col = 0; col < getSongCount(row); col++) {
                if (trackName.equals(getSong(row, col).trackName())) {
                    return row;
                }
            }
        }
        return -1;
    }

    /**
     * Helper method to return a String array of song release years
     *
     * @return String array of release years that have been extracted from the file
     */
    public String[] getReleaseYears() {
        return releaseYears;
    }

    /**
     * Sorts the songs alphabetically (case-sensitive) by track name.  Implements an Insertion Sort adapted from the
     * code given in my CSC142 class.
     * rowIndex refers to the row index of the 2d jagged array
     */
    public void sortSongs() {
        for (int rowIndex = 0; rowIndex < getYearCount(); rowIndex++) {
            for (int pass = 1; pass < getSongCount(rowIndex); pass++) {
                Song temp = songs[rowIndex][pass];
                int checkPos = pass - 1;
                while (checkPos >= 0 && temp.trackName().compareTo(songs[rowIndex][checkPos].trackName()) < 0) {
                    songs[rowIndex][checkPos + 1] = songs[rowIndex][checkPos];
                    checkPos--;
                }
                songs[rowIndex][checkPos + 1] = temp;
            }
        }
    }
}

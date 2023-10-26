import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * SongViewer class that creates a GUI for SongManager data to be displayed to user.
 *
 * @author Bennett Fife
 * @version 10/25/23
 */

public class SongViewer extends JFrame {

    private int songIteration = 0;
    private int songNumByYear;
    public SongViewer(SongManager a){

        //create the GUI frame that will hold frame elements
        Frame frame = new JFrame("Popular Songs of 2023");
        frame.setSize(400, 400);

        //create, setBounds, and add buttons to the frame
        JButton loadData = new JButton("Load Data");
        JButton prevSong = new JButton("Prev");
        JButton nextSong = new JButton("Next");
        loadData.setBounds(20, 25, 100, 30);
        prevSong.setBounds(140, 25, 100, 30);
        nextSong.setBounds(260, 25, 100, 30);
        frame.add(loadData);
        frame.add(prevSong);
        frame.add(nextSong);
        prevSong.setEnabled(false);
        nextSong.setEnabled(false);

        //combo box to provide song years to the user
        JComboBox<String> yearsComboBox = new JComboBox<>();
        yearsComboBox.setBounds(20, 75, 100, 30);

        //Set a label for the current year's data above the text fields
        JLabel yearsData = new JLabel("");
        yearsData.setBounds(140,75,220,30);

        //used for padding between text fields
        Dimension dataPadding = new Dimension(0, 25);
        Dimension labelPadding = new Dimension(0, 40);

        //Set a panel for the data labels
        JPanel dataLabels = new JPanel();
        dataLabels.setBounds(20, 127, 120, 195);
        BoxLayout labelLayout = new BoxLayout(dataLabels, BoxLayout.Y_AXIS);
        dataLabels.setLayout(labelLayout);

        //Set and add data labels with padding
        JLabel trackNameLabel = new JLabel("Track Name:");
        JLabel artistLabel = new JLabel("Artist(s):");
        JLabel releaseDateLabel = new JLabel("Release Date:");
        JLabel streamsLabel = new JLabel("Total Streams:");

        dataLabels.add(trackNameLabel);
        dataLabels.add(Box.createRigidArea(labelPadding));
        dataLabels.add(artistLabel);
        dataLabels.add(Box.createRigidArea(labelPadding));
        dataLabels.add(releaseDateLabel);
        dataLabels.add(Box.createRigidArea(labelPadding));
        dataLabels.add(streamsLabel);
        
        //Set a panel for the text fields
        JPanel dataTextFields = new JPanel();
        dataTextFields.setBounds(140, 125, 220, 195);
        BoxLayout dataLayout = new BoxLayout(dataTextFields, BoxLayout.Y_AXIS);
        dataTextFields.setLayout(dataLayout);

        //Set and add text fields with padding
        JTextField trackNameData = new JTextField();
        JTextField artistData = new JTextField();
        JTextField releaseYearData = new JTextField();
        JTextField streamsData = new JTextField();

        dataTextFields.add(trackNameData);
        dataTextFields.add(Box.createRigidArea(dataPadding));
        dataTextFields.add(artistData);
        dataTextFields.add(Box.createRigidArea(dataPadding));
        dataTextFields.add(releaseYearData);
        dataTextFields.add(Box.createRigidArea(dataPadding));
        dataTextFields.add(streamsData);

        frame.add(yearsData);
        frame.add(yearsComboBox);
        frame.add(dataLabels);
        frame.add(dataTextFields);
        frame.setLayout(null);
        //show the window on run
        frame.setVisible(true);
        
        
        //load button (enables Next/Prev buttons and adds combo box choices)
        loadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prevSong.setEnabled(true);
                nextSong.setEnabled(true);
                loadData.setEnabled(false);
                ComboBoxModel<String> years = new DefaultComboBoxModel<>(a.getReleaseYears());
                yearsComboBox.setModel(years);
            }
        });

        //combo box choice logic
        yearsComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    //always reset to 0 when a new year is chosen
                    songIteration = 0;
                    //always reset to 1 for first track in specific year (used for frame title)
                    songNumByYear = 1;
                    //year selected from the combo box
                    String selectedYear = (String) yearsComboBox.getSelectedItem();
                    //grab the index of the current year selected
                    int yearIndex = a.findStringIndex(a.getReleaseYears(), selectedYear);
                    //grab an array of song objects from that specific index
                    Song[] songsInCurrentYear = a.getSongs(yearIndex);
                    //set the text fields using the specified get methods
                    trackNameData.setText(songsInCurrentYear[songIteration].trackName());
                    artistData.setText(songsInCurrentYear[songIteration].artistName());
                    releaseYearData.setText(songsInCurrentYear[songIteration].releasedYear());
                    streamsData.setText(numberCommas(songsInCurrentYear[songIteration].totalNumberOfStreamsOnSpotify()));
                    trackNameData.setCaretPosition(0);
                    artistData.setCaretPosition(0);
                    releaseYearData.setCaretPosition(0);
                    streamsData.setCaretPosition(0);
                    //songNum of all songs
                    double songNum = a.getSongNum(songsInCurrentYear[songIteration].trackName());
                    //total number of songs in all years
                    double totalSongs = a.getSongCount();
                    double songNumPercent = (songNum / totalSongs) * 100.0;
                    //set yearsData text label and the frame title
                    yearsData.setText(String.format("%.2f", songNumPercent) + "% | " + (int) songNum + " of " + a.getSongCount() + " total songs.");
                    frame.setTitle("Songs | " + songNumByYear + " of " + songsInCurrentYear.length + " songs");
                }
            }
        });

        prevSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearsComboBox.getSelectedItem();
                int yearIndex = a.findStringIndex(a.getReleaseYears(), selectedYear);
                Song[] songsInCurrentYear = a.getSongs(yearIndex);
                if (songIteration == 0){
                    JOptionPane.showMessageDialog(frame, "This is the first song for this year.");
                } else {
                    songIteration--;
                    songNumByYear--;
                    trackNameData.setText(songsInCurrentYear[songIteration].trackName());
                    artistData.setText(songsInCurrentYear[songIteration].artistName());
                    releaseYearData.setText(songsInCurrentYear[songIteration].releasedYear());
                    streamsData.setText(numberCommas(songsInCurrentYear[songIteration].totalNumberOfStreamsOnSpotify()));
                    trackNameData.setCaretPosition(0);
                    artistData.setCaretPosition(0);
                    releaseYearData.setCaretPosition(0);
                    streamsData.setCaretPosition(0);
                    double songNum = a.getSongNum(songsInCurrentYear[songIteration].trackName());
                    double totalSongs = a.getSongCount();
                    double songNumPercent = (songNum / totalSongs) * 100.0;
                    yearsData.setText(String.format("%.2f", songNumPercent) + "% | " + (int) songNum + " of " + a.getSongCount() + " total songs");
                    frame.setTitle("Songs | " + songNumByYear + " of " + songsInCurrentYear.length + " songs");
                }
            }
        });

        nextSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearsComboBox.getSelectedItem();
                int yearIndex = a.findStringIndex(a.getReleaseYears(), selectedYear);
                Song[] songsInCurrentYear = a.getSongs(yearIndex);
                if (songIteration == songsInCurrentYear.length - 1){
                    JOptionPane.showMessageDialog(frame, "This is the last song for this year.");
                } else {
                    songIteration++;
                    songNumByYear++;
                    trackNameData.setText(songsInCurrentYear[songIteration].trackName());
                    artistData.setText(songsInCurrentYear[songIteration].artistName());
                    releaseYearData.setText(songsInCurrentYear[songIteration].releasedYear());
                    streamsData.setText(numberCommas(songsInCurrentYear[songIteration].totalNumberOfStreamsOnSpotify()));
                    trackNameData.setCaretPosition(0);
                    artistData.setCaretPosition(0);
                    releaseYearData.setCaretPosition(0);
                    streamsData.setCaretPosition(0);
                    int songNum = a.getSongNum(songsInCurrentYear[songIteration].trackName());
                    double totalSongs = a.getSongCount();
                    double songNumPercent = (songNum / totalSongs) * 100.0;
                    yearsData.setText(String.format("%.2f", songNumPercent) + "% | " + songNum + " of " + a.getSongCount() + " total songs");
                    frame.setTitle("Songs | " + songNumByYear + " of " + songsInCurrentYear.length + " songs");

                }
            }
        });
    }

    /**
     * Helper method to format the number of streams for a song
     * @param streams String number of streams pulled from song data
     * @return String of the streams but formatted to have a comma every 3rd number from the right
     */
    public String numberCommas(String streams){
        for (char c : streams.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new NumberFormatException("Input string contains non-numeric characters.");
            }
        }

        return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(streams));
    }
}

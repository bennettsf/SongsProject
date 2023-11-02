/**
 * Main class used for running the program.
 *
 * @author Bennett Fife
 * @version 10/25/23
 */
class Main {
    /**
     * The application's entry point
     * @param args an array of command-line arguments for the application
     */
    public static void main(String[] args){

        SongManager a = new SongManager();
        SongViewer b = new SongViewer(a);

    }
}

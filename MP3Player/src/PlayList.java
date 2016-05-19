/**
 * A PlayList comprises a list of <code>AudioSource</code>s,
 * built from strings that name file with an MP3 audio stream.
 * Once the list is created, the <code>AudioSources</audio> in the files
 * can be played.
 */

/*
 * Standard Java classes
 */
import java.util.Observer ;
import java.util.Observable ;
import java.util.ArrayList ;

/*
 * SWEN383 classes for managing and playing audio files.
 */
import edu.rit.se.swen383.audio.AudioPlayer ;
import edu.rit.se.swen383.audio.AudioSource ;
import edu.rit.se.swen383.audio.AudioSourceException ;

public class PlayList implements Observer {
    /**
     * The <code>AudioSource</code>s (created from MP3 files) in the play list.
     */
    private ArrayList<AudioSource> sources = new ArrayList<AudioSource>() ;

    /**
     * The index in the <code>sources</code> list of the
     * <code>AudioSource</code> being played, or -1 if the player is idle.
     */
    private volatile int currentIndex = -1 ;

    /**
     * A reference to the (one and only) <code>AudioPlayer</code>
     * in the system.
     */
    private final AudioPlayer player ;

    /**
     * Create a new play list from an array of <code>String</code>s naming
     * MP3 files.
     * The list itself contains <code>AudioSource</code> objects created
     * from the filenames.
     *
     * @param fileNames The array of audio filenames for this play list.
     */
    public PlayList(String fileNames[]) {
        for( int i = 0 ; i < fileNames.length ; ++i ) {
            AudioSource source ;
            try {
                source = new AudioSource(fileNames[i]) ;
                sources.add(source) ;
            } catch (AudioSourceException ex) {
                System.out.print("File <" + fileNames[i] + "> excluded ") ;
                System.out.println(ex) ;
            }
        }
        player = AudioPlayer.getPlayer() ;
        player.addObserver(this) ;
    }

    /**
     * Return the size of the play list (the number of
     * <code>AudioSource</code>s in it).
     * @return size of the playlist
     */
    public int size() {
        return sources.size() ;
    }

    /**
     * Play a specific <code>AudioSource</code> in the list.
     * When done playing, the try to move on to the next
     * <code>AudioSource</code> in the play list.
     *
     * @param index Index of the entry to play.
     */
    public void play(int index) {
        playAudioSource(index) ;
    }

    /**
     * Index of the <code>AudioSource</code> being played.
     * @return Index of the current source or (-1) if the player is idle.
     */
    public int getSourceIndex() {
        return currentIndex ;
    }

    /**
     * Return the i'th <code>AudioSource</code>.
     * @param i index of the AudioSource to retrieve
     * @return ith <code>AudioSource</code> if it exists, else null.
     */
    public AudioSource getSource(int i) {
        AudioSource as = null ;
        if( i >= 0 && i < sources.size() ) {
            as = sources.get(i) ;
        }
        return as ;
    }

    /**
     * Return the position in <code>AudioSource</code> being played.
     * @return millisecond position in current stream or 0 if no
     *         stream is being played
     */
    public int getPosition() {
        return player.getPosition() ;
    }

    /**
     * Pause playback.
     * Only has effect if the AudioPlayer is playing.
     */
    public void pause() {
        player.pause() ;
    }

    /**
     * Resume playback.
     * Only has an effect if the AudioPlayer is paused.
     */
    public void resume() {
        player.resume() ;
    }

    /**
     * Update called when an audio stream finishes playing.
     * This is only called when the stream being played completes
     * without interruption; the assumption is that the client
     * will decide to stop or will select a new file to play.
     *
     * @param o The observable object sending the update
     * @param junk Ignored secondary information.
     */
    public void update(Observable o, Object junk) {
        /*
         * Make sure this came from the AudioPlayer object.
         */
        if( o != player ) {
            return ;
        }

        /*
         * Advance to the next AudioSource unless we
         * just played the last one.
         */
        int nextIndex = currentIndex + 1 ;
        if( nextIndex < size() ) {
            playAudioSource(nextIndex) ;
        } else {
            currentIndex = (-1) ;
        }
    }

    /**
     * The internal method that initiates playback of the AudioSource
     * selected by <code>index</code>.
     *
     * Synchcronized because this is the only method that can be called
     * from both the main thread <i>and </i>the audio player thread.
     *
     * @param index Index of entry to play
     */
    private synchronized void playAudioSource(int index) {
        int origIndex = currentIndex ;

        if( index < 0 || index >= sources.size() ) {
            return ;
        }
        currentIndex = index ;

        try {
            AudioSource source = sources.get(index) ;
            /*
             * If play request is denied, unwind to the previous index.
             */
            if( ! player.playMP3(source.getFileName()) ) {
                currentIndex = origIndex ;
            }
        } catch(Exception ex) {
            /*
             * On exception, set currentIndex to -1 (not playing).
             */
            currentIndex = (-1) ;
            System.out.println("PlayList.playAudioSource: " + ex) ;
        } ;
    }
}

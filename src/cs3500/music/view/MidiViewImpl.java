package cs3500.music.view;

import java.util.Iterator;
import java.util.Set;

import javax.sound.midi.*;

import cs3500.music.model.Note;
import cs3500.music.model.NoteList;

/**
 * A skeleton for MIDI playback
 */
public class MidiViewImpl /*implements YourViewInterfaceHere*/ {
  private Synthesizer synth;
  private Receiver receiver;

  public MidiViewImpl() {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.synth.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }
  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   *  <li>{@link MidiSystem#getSynthesizer()}</li>
   *  <li>{@link Synthesizer}
   *    <ul>
   *      <li>{@link Synthesizer#open()}</li>
   *      <li>{@link Synthesizer#getReceiver()}</li>
   *      <li>{@link Synthesizer#getChannels()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link Receiver}
   *    <ul>
   *      <li>{@link Receiver#send(MidiMessage, long)}</li>
   *      <li>{@link Receiver#close()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link MidiMessage}</li>
   *  <li>{@link ShortMessage}</li>
   *  <li>{@link MidiChannel}
   *    <ul>
   *      <li>{@link MidiChannel#getProgram()}</li>
   *      <li>{@link MidiChannel#programChange(int)}</li>
   *    </ul>
   *  </li>
   * </ul>
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI">
   *   https://en.wikipedia.org/wiki/General_MIDI
   *   </a>
   */

  public void playNote() throws InvalidMidiDataException {
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 64);
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 64);
    this.receiver.send(start, -1);
    this.receiver.send(stop, this.synth.getMicrosecondPosition() + 200000);
    this.receiver.close(); // Only call this once you're done playing *all* notes
  }

  public void playBeat(NoteList noteList, int BeatNumber) throws InvalidMidiDataException {

    Set<Note> Notes = noteList.getAllAtTime(BeatNumber);

    Iterator<Note> i = Notes.iterator();

    while (i.hasNext()){
      //Get the Note from the set
      Note n = (Note) i.next();

      //Find notes to Start
      if(n.getStart() == BeatNumber) {

        /**
         * void setMessage(int command, int channel, int data1, int data2)
         * Data1 is the pitch/octave represented 60=Middle C = C4
         */
        //int MidiPitch = Integer.parseInt(n.getOctave().toString()) * 15 + n.getPitchInt();
        //this.receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 0, MidiPitch, 40), -1);
        //this.receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 0, MidiPitch, 40), this.synth.getMicrosecondPosition() + 200000);
      }

      //Find Notes to Continue
      else if(n.getStart() < BeatNumber && n.getStart()+n.getEnd() >= BeatNumber){
        //this.receiver.send(new ShortMessage(ShortMessage.CONTINUE, 0, 60, 40), -1);
      }
    }


    //End all notes from before
    //this.receiver.send(new ShortMessage(0xFC), -1);
    //this.receiver.send(stop, this.synth.getMicrosecondPosition() + 200000);
    this.receiver.close(); // Only call this once you're done playing *all* notes
  }
}

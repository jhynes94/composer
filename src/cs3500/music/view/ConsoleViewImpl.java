package cs3500.music.view;

import cs3500.music.model.Note;
import cs3500.music.model.SoundUnit;
import cs3500.music.model.SoundUnitList;

import java.util.*;

/**
 * Returns in the console a view of how all the notes are played
 */
public class ConsoleViewImpl implements View {
  SoundUnitList soundUnitList;

  public ConsoleViewImpl(SoundUnitList soundUnitList) {
    this.soundUnitList = soundUnitList;
  }

  /**
   * Returns a list containing the range of notes to be played, including gaps
   * where there is no note in the song itself.
   *
   * @return a list representing the range of notes to be played.
   */
  public ArrayList<SoundUnit> makeTopRow() {

    int rangeOfSong = soundUnitList.getHighestNote().getMIDIPitch()
            - soundUnitList.getLowestNote().getMIDIPitch();
    ArrayList<SoundUnit> finalList = new ArrayList<>();
    //Iterate Through the Range to create Header
    for (int i = rangeOfSong; i >= 0; i--) {
      SoundUnit rangeNote = new Note(SoundUnit.Pitch.C, SoundUnit.Octave.FOUR, 0, 1);
      rangeNote.setPitchAndOctaveFromMIDI(soundUnitList.getHighestNote().getMIDIPitch() - i);
      finalList.add(rangeNote);
    }
    return finalList;
  }

  /**
   * Renders the top row of the console output so that it lists
   * the entire range of pitches.
   *
   * @param list ArrayList
   * @return a string representing the range of pitches to be played
   */
  public String renderTopRow(ArrayList<SoundUnit> list) {
    String header = "        ";
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).toString().length() == 2) {
        header = header + list.get(i).toString() + "   ";
      } else {
        header = header + list.get(i).toString() + "  ";
      }
    }
    return header;
  }

  /**
   * Renders the console view of the given list of notes
   *
   * @throws IllegalArgumentException if the NoteList is empty
   */
  public void consoleRender() {
    if (soundUnitList.size() == 0) {
      throw new IllegalArgumentException("Nothing to render");
    }
    ArrayList<SoundUnit> pitchRow = this.makeTopRow();
    HashMap<SoundUnit.Octave, HashSet<SoundUnit.Pitch>> onRightNow = new HashMap<>();
    String finalConsoleRender = this.renderTopRow(pitchRow) + "\n";
    for (int i = 0; i <= soundUnitList.songLength(); i++) {
      String finalRow;
      if (i < 10) {
        finalRow = "   " + Integer.toString(i) + "  ";
      } else if (i < 100) {
        finalRow = "  " + Integer.toString(i) + "  ";
      } else if (i < 1000) {
        finalRow = " " + Integer.toString(i) + "  ";
      } else {
        finalRow = Integer.toString(i) + "  ";
      }
      if (soundUnitList.hasNotesAtTime(i)) {
        for (int j = 0; j < pitchRow.size(); j++) {
          Iterator iterator = soundUnitList.getAllAtTime(i).iterator();
          boolean wasAnythingAdded = false;
          while (iterator.hasNext()) {
            SoundUnit n = (Note) iterator.next();
            SoundUnit.Pitch nPitch = pitchRow.get(j).getPitch();
            SoundUnit.Octave nOctave = pitchRow.get(j).getOctave();
            if (nPitch.equals(n.getPitch()) && nOctave.equals(n.getOctave())) {
              if (!(onRightNow.containsKey(nOctave)) && n.getStart() == n.getEnd() - 1 ||
                      onRightNow.containsKey(nOctave) && n.getStart() == n.getEnd() - 1) {
                finalRow = finalRow + "  X  ";
                wasAnythingAdded = true;
              }else if (!(onRightNow.containsKey(nOctave))) {
                HashSet<SoundUnit.Pitch> p = new HashSet<>();
                p.add(nPitch);
                onRightNow.put(nOctave, p);
                finalRow = finalRow + "  X  ";
                wasAnythingAdded = true;
              } else if (!(onRightNow.get(nOctave).contains(nPitch))) {
                onRightNow.get(nOctave).add(nPitch);
                finalRow = finalRow + "  X  ";
                wasAnythingAdded = true;
              } else if (onRightNow.get(nOctave).contains(nPitch)) {
                finalRow = finalRow + "  |  ";
                wasAnythingAdded = true;
                if (n.getEnd() == i + 1) {
                  onRightNow.get(nOctave).remove(nPitch);
                }
              }
            }
          }
          if (wasAnythingAdded == false) {
            finalRow = finalRow + "     ";
          }
        }
        finalConsoleRender = finalConsoleRender + finalRow + "\n";
      } else {
        finalConsoleRender = finalConsoleRender + finalRow + "\n";
      }
    }
    System.out.println(finalConsoleRender);
  }

  @Override
  public void render() {
    consoleRender();
  }
}

package cs3500.music.model;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.ArrayList;

/**
 * To sort individual notes by time.
 */
public class NoteList implements SoundUnitList<Note> {
  private TreeMap<Integer, HashSet<Note>> map;
  private TreeSet<Note> set;

  public NoteList() {
    this.map = new TreeMap<>();
    this.set = new TreeSet<>();
  }

  @Override
  public void add(Note note) {
    for (int i = note.getStart(); i <= note.getEnd(); i++) {
      if (map.containsKey(i)) {
        Iterator iterator = map.get(i).iterator();
        while (iterator.hasNext()) {
          Note n = (Note) iterator.next();
          if (n.getPitch().toString() == note.getPitch().toString() &&
                  n.getOctave().toString() == note.getOctave().toString()) {
            throw new IllegalArgumentException("Note with same pitch and octave " +
                    "already exists at this time interval.");
          }
        }
      }
    }
    for (int i = note.getStart(); i <= note.getEnd(); i++) {
      if (!map.containsKey(i)) {
        map.put(i, new HashSet());
        map.get(i).add(note);
        set.add(note); // adds if not already present
      } else {
        map.get(i).add(note);
        set.add(note);
      }
    }
  }

  @Override
  public void delete(Note note) {
    for (int i = note.getStart(); i <= note.getEnd(); i++) {
      if (map.containsKey(i) && !map.get(i).contains(note) && !set.contains(note)
              || !map.containsKey(i) && !set.contains(note)) {
        throw new IllegalArgumentException("The given note is not in this list");
      } else {
        map.get(i).remove(note);
        set.remove(note);
      }
    }
  }

  @Override
  public boolean contains(Note note) {
    for (int i = note.getStart(); i <= note.getEnd(); i++) {
      if (map.containsKey(i) && !map.get(i).contains(note) && !set.contains(note)
              || !map.containsKey(i) && !set.contains(note)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Set<Note> getAllAtTime(int time) {
    if (time < 0) {
      throw new IllegalArgumentException("Time cannot be negative.");
    }
    if (!map.containsKey(time)) {
      throw new IllegalArgumentException("No notes at this time" +
              "stamp.");
    } else {
      return map.get(time);
    }
  }

  @Override
  public int size() {
    HashSet<Note> finalSet = new HashSet<>();
    for (Map.Entry<Integer, HashSet<Note>> entry : map.entrySet()) {
      HashSet value = entry.getValue();
      Iterator iterator = value.iterator();
      for (int i = 0; i < value.size(); i++) {
        Note n = (Note) iterator.next();
        finalSet.add(n); // works because HashSet does not add duplicates
      }
    }
    return finalSet.size();
  }

  @Override
  public void consoleRender() {
    Note highestNote = set.last();
    Note lowestNote = set.first();
    Note.Pitch currentPitch = lowestNote.getPitch();
    Note.Octave currentOctave = lowestNote.getOctave();
    Note.Pitch highestPitch = highestNote.getPitch();
    Note.Octave highestOctave = highestNote.getOctave();

    ArrayList<Note.Octave> allOctaves = new ArrayList<>();
    for(Note.Octave octave : Note.Octave.values()) {
      if(octave.compareTo(lowestNote.getOctave()) >= 0)
        allOctaves.add(octave);
    }

    ArrayList<Note.Pitch> allPitches = new ArrayList<>();
    for(Note.Pitch pitch : Note.Pitch.values()) {
      allPitches.add(pitch);
    }

    ArrayList<Note> pitchRow = new ArrayList<>();
    while(currentOctave.compareTo(highestOctave) < 0 &&
            currentPitch.compareTo(highestPitch) < 0) {
      Note n = new Note(currentPitch, currentOctave, 0, 1);
      pitchRow.add(n);
      // advance current octave and set pitch back to C
      if(currentPitch.equals((Note.Pitch.B))) {
        currentOctave = allOctaves.get(allOctaves.indexOf(currentOctave) + 1);
        currentPitch = Note.Pitch.C;
      }
      else {
        currentPitch = allPitches.get(allPitches.indexOf(currentPitch) + 1);
      }
    }

    SoundUnitList on = new NoteList(); // stores all notes currently playing
    String finalConsoleRender = "";    // final return value
    for(int i = 0; i <= map.lastKey(); i++) {
      Iterator iterator = map.get(i).iterator();
      for(int j = 0; j < pitchRow.size(); j++) {
        pitchRow.get(j).getPitch()
                pitchRow.get(j).getOctave()
      }
    }
  }
}

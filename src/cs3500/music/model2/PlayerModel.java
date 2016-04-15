package cs3500.music.model2;

import java.util.List;
import java.util.ArrayList;
import java.util.*;



/**
 * Key things to Know:
 * Notes come in as a list of notes and since you utilize a Map of notes, you must call that
 * function whenever you wish to output the Notes in a Map Fashion
 */

/**
 * Class, that implements a IPlayerModel and represents a music player model
 */

public class PlayerModel implements IPlayerModel {


  private List<Note> theModel;
  private String songname;
  private int currentBeat;
  private int tempo;


  /**
   * Creates a PlayerModel with all default fields except for song Name
   */

  public PlayerModel(String inputSongName) {
    theModel = new ArrayList<Note>();
    songname = inputSongName;
    currentBeat = 0;
    tempo = 0;
  }


  /**
   * Creates a PlayerModel with songName and inputNotes passed in
   */

  public PlayerModel(String inputSongName, List<Note> inputNotes){
    songname = inputSongName;
    theModel = inputNotes;
    currentBeat = 0;
    tempo = 0;
  }


  /**
   * Creates a PlayerModel with all fields passed in except CurrentBeat
   */

  public PlayerModel(String inputSongName, List<Note> inputNotes, int tempoi){
    songname = inputSongName;
    theModel = inputNotes;
    currentBeat = 0;
    tempo = tempoi;
  }


  /**
   * Outputs the song name
   *
   * @return songname
   */

  public String getSongName() {
    return this.songname;
  }


  /**
   * sets the song name
   *
   * @param name = new name of Song
   */

  public void setSongName(String name) {
    songname = name;
  }


  /**
   * sets the Tempo
   *
   * @param inputTempo -> the new Tempo
   */

  public void setTempo(int inputTempo) {
    if (inputTempo <= 0){
      throw new IllegalArgumentException("You must input a number larger than 0");
    }
    this.tempo = inputTempo;
  }


  /**
   * Outputs the tempo
   *
   * @return tempo
   */

  public int getTempo() {
    return this.tempo;
  }


  /**
   * Outputs the currentBeat
   *
   * @return currentBeat
   */

  public int getCurrentBeat() {
    return this.currentBeat;
  }


  /**
   * Sets the current Beat
   *
   * @param inputBeat
   */

  public void setCurrentBeat(int inputBeat) throws IllegalArgumentException {
    if (inputBeat < 0){
      throw new IllegalArgumentException("Can not enter below 0 beat ");
    }
    // double make sure this is true - could lead to out of bounds
    if (inputBeat > this.finalBeat()){
      throw new IllegalArgumentException("Must be within acceptable range");
    }
    else {
      this.currentBeat = inputBeat;
    }
  }


  /**
   * gets the List of Notes
   *
   * @return list of Notes
   */

  public List<Note> outputModelAsList() {
    return this.theModel;
  }


  /**
   * method which adds a Note to the Model
   *
   * @param inputNote indicates the note that you want to add
   */


  public void addNote(Note inputNote) throws IllegalArgumentException {
    NoteComparatorTextView nc1 = new NoteComparatorTextView();
    for (Note x : this.theModel) {
      if (nc1.compare(inputNote, x) == 0) {
        throw new IllegalArgumentException("You cannot do that");
      }
    }
    this.theModel.add(inputNote);
  }


  /**
   * Converts the inputted list of notes into a Map
   *
   * @return Map of Notes with Key being a beat
   */

  public Map<Integer, List<Note>> outputModelAsMap() {

    if (this.outputModelAsList().size() <= 0) {
      return Collections.emptyMap();
    }

    List<Note> theNotes = this.outputModelAsList();
    Collections.sort(theNotes, new NoteComparatorTextView());
    int endPoint = this.finalBeat();
    Map<Integer, List<Note>> dataMap = new HashMap<Integer, List<Note>>();

    for (int i = 0; i <= endPoint; i++) {
      dataMap.put(i, new ArrayList<Note>());
    }

    for (Note aNote : theNotes) {
      int curr = aNote.getStart();
      int end = aNote.getEnd();
      while (curr < end) {
        dataMap.get(curr).add(aNote);
        curr += 1;
      }
    }

    return dataMap;
  }


  /**
   * removes a Note from the Model
   *
   * @param inputNote indicates the note that you want to have removed
   */

  public void removeNote(INote inputNote) throws IllegalArgumentException {
    if (theModel.size() <= 0) {
      throw new IllegalArgumentException("You have no notes");
    }

    int index = -1;
    for (int i = 0; i < theModel.size(); i++) {
      if (inputNote.getOctave() == theModel.get(i).getOctave() &&
              inputNote.getPitch() == theModel.get(i).getPitch() &&
              inputNote.getDuration() == theModel.get(i).getDuration() &&
              inputNote.getStart() == theModel.get(i).getStart() &&
              inputNote.getEnd() == theModel.get(i).getEnd() &&
              inputNote.getInstrument() == theModel.get(i).getInstrument() &&
              inputNote.getVolume() == theModel.get(i).getVolume()) {
        index = i;
        break;
      }
    }

    if (index == -1) {
      throw new IllegalArgumentException("That note is not in the list");
    } else {
      theModel.remove(index);
    }
  }


  /**
   * outputs the Notes as a matrix so it easy to visualize for the console output
   *
   * return String[][] matrix of all the Notes based on all available pitches and how many beats
   * in the song
   */

  public String[][] outputAsMatrix() {

    int max = this.finalBeat();
    List<Note> loNotes = this.outputModelAsList();
    List<String> pitchesPlayed = outputPitchesOctaves();
    String[][] matrixRep = new String[max+1][pitchesPlayed.size()];

    for (int x = 0; x <= max; x++) {
      for (int y = 0; y < pitchesPlayed.size(); y++) {
        matrixRep[x][y] = "James";
      }
    }


    for (Note x : loNotes) {

      int indexOfNote = pitchesPlayed.indexOf(x.getNoteAsString()); // problem is with index of
      // note C0
      int counterStart = x.getStart();
      int counter = 0;
      while (counterStart < x.getEnd()) {
        if (counterStart == x.getStart()) {
          matrixRep[counterStart][indexOfNote] = "X";
        } else {
          matrixRep[counterStart][indexOfNote] = "|";
        }
        counterStart += 1;
        counter += 1;
      }
    }


    return matrixRep;
  }


  /**
   * Outputs the model as a String
   *
   * @String the model as a String
   */

  public String outputModel() {

    List<Note> yourModel = this.outputModelAsList();
    Collections.sort(yourModel, new NoteComparatorStartTime());
    String outputString = "";
    //List<String> notesPlayed = new ArrayList<>();
    if (yourModel.size() == 0) {
      return outputString;
    } else {
//      return outputString;
//    }

      int max = this.finalBeat();
      List<String> notesPlayed = outputPitchesOctaves();

//    String[][] matrixRep = new String[max + 1][notesPlayed.size()];
      String[][] matrixRep = this.outputAsMatrix();
//    for (int x = 0; x <= max; x++) {
//      for (int y = 0; y < notesPlayed.size(); y++) {
//        matrixRep[x][y] = "James";
//      }
//    }

      int intColumn = String.valueOf(max).length();
      String toAdd = "";
      int i = 0;
      while (i < intColumn) {
        outputString = outputString + " ";
        i = i + 1;
      }

      for (String x : notesPlayed) {
        if (x.length() == 1) {
          outputString = outputString + "  " + x + "  ";
        } else if (x.length() == 2) {
          outputString = outputString + "  " + x + " ";
        } else if (x.length() == 3) {
          outputString = outputString + " " + x + " ";
        } else if (x.length() == 4) {
          outputString = outputString + " " + x;
        } else if (x.length() == 5) {
          outputString = outputString + x;
        } else {
          outputString = outputString + x.substring(0, 5);
        }
      }


//    for (Note x : this.theModel) {
//      int indexOfNote = notesPlayed.indexOf(x.getNoteAsString());
//      int counterStart = x.getStart();
//      while (counterStart < x.getEnd()) {
//        if (counterStart == x.getStart()) {
//          matrixRep[counterStart][indexOfNote] = "X";
//        } else {
//          matrixRep[counterStart][indexOfNote] = "|";
//        }
//        counterStart += 1;
//      }
//    }

      i = 0;

      while (i < max) {
        outputString = outputString + "\n";
        toAdd = "";
        int curSize = String.valueOf(i).length();
        int whiteSpaces = intColumn - curSize;
        while (whiteSpaces > 0) {
          toAdd = toAdd + " ";
          whiteSpaces = whiteSpaces - 1;
        }

        toAdd = toAdd + Integer.toString(i);
        outputString = outputString + toAdd;
        for (String x : matrixRep[i]) {


          if (x.compareTo("James") == 0) {
            outputString = outputString + "     ";
          } else if (x.length() == 1) {
            outputString = outputString + "  " + x + "  ";
          } else if (x.length() == 2) {
            outputString = outputString + "  " + x + " ";
          } else if (x.length() == 3) {
            outputString = outputString + " " + x + " ";
          } else if (x.length() == 4) {
            outputString = outputString + " " + x;
          } else if (x.length() >= 5) {
            outputString = outputString + x;
          } else {
            outputString = outputString + x.substring(0, 5);
          }
        }
        i = i + 1;
      }

      return outputString;
    }
  }

  /**
   * Returns the last beat of the song
   *
   * @return integer of the final beat of the song
   */

  public int finalBeat() {
    int endPoint = 0;
    for (Note x : outputModelAsList()) {
      if (x.getEnd() > endPoint) {
        endPoint = x.getEnd();
      }
    }
    return endPoint;
  }


  /**
   * Outputs all pitches as a list of Strings
   * outputs pitches based on textview sort
   *
   * @return List of all the Pitches in the song as Strings
   */

  public List<String> outputPitchesOctaves() {
    List<String> notesPlayed = new ArrayList<>();
    List<Note> noteList = this.outputModelAsList();
    Collections.sort(noteList, new NoteComparatorTextView());
    if (noteList.size() == 0) {
      return notesPlayed;
    } else {
      Note noteBegin = noteList.get(0);
      Note noteEnd = noteList.get(noteList.size() - 1);
      Note traversalNote = new Note(noteBegin.getOctave(), noteBegin.getPitch(),
              noteBegin.getDuration(), noteBegin.getStart(), noteBegin.getInstrument(),
              noteBegin.getVolume());

      while (traversalNote.pitchOctaveComparator(noteBegin) >= 0 &&
              traversalNote.pitchOctaveComparator(noteEnd) <= 0) {
        notesPlayed.add(traversalNote.getNoteAsString());

        Note.Pitch newP = traversalNote.getPitch().getNext();
        if (newP.compareTo(Note.Pitch.C) == 0) {
          traversalNote.setOctave(traversalNote.getOctave() + 1);
        }
        traversalNote.setPitch(newP);

      }
      //System.out.println(notesPlayed);

      return notesPlayed;

    }
  }
}
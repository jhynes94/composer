package cs3500.music.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import cs3500.music.controller.KeyboardHandler;
import cs3500.music.model.Note;
import cs3500.music.model.SoundUnit;
import cs3500.music.model.SoundUnitList;


public class ControllerCompositeAdapter implements cs3500.music.view.ICompositeView {

  cs3500.music.view2.IGuiView GuiView;
  cs3500.music.view2.IMidiImpl MidiView;

  public ControllerCompositeAdapter(cs3500.music.view2.IGuiView newGuiView, cs3500.music.view2.IMidiImpl newMidiView) {
    this.GuiView = newGuiView;
    this.MidiView = newMidiView;
  }

  @Override
  public Note NotePressed(Point mousePoint, SoundUnitList model) {
    return new Note(SoundUnit.Pitch.C, SoundUnit.Octave.FOUR, 999, 1000);
  }


  @Override
  public Note SpacePressed(Point mousePoint, SoundUnitList model) {
    return new Note(SoundUnit.Pitch.C, SoundUnit.Octave.FOUR, 999, 1000);
  }

  @Override
  public void setGuiView(View guiView) {

  }

  @Override
  public void setMidiView(View midiView) {

  }

  @Override
  public void addNewMouseListener(MouseListener listener) {

  }

  @Override
  public void addActionListener(ActionListener listener) {

  }

  @Override
  public void refreshGuiViewFromModel(SoundUnitList refreshedModel) {

  }

  @Override
  public void initialize() {

  }

  @Override
  public void resetFocus() {

  }

  @Override
  public void addKeyListener(KeyboardHandler keyHandler) {

  }

  @Override
  public void setVisible(boolean state) {

  }

  @Override
  public void playBeat(Integer BeatNumber) {

  }

  public void render(){

  }

}
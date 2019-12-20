package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;

public class ModelImpl implements Model{

  private final PropertyChangeSupport support;
  
  private ModifiableGameState state;
  private GameMode mode;
  //private AI ai;
  
  // HotSeat and Single
  public ModelImpl(GameMode mode) {}
  
  //Multiplayer
  public ModelImpl(Player thisClient, PlayerManagement manager) {}
  
  @Override
  public boolean placeDisk(Disk disk, Cell cell) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set<Cell> getPossibleMovesForPlayer(Player player) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addListener(PropertyChangeListener pcl) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void removeListener(PropertyChangeListener pcl) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public GameState getState() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private boolean checkIfLegalMove(Disk disk, Cell cell) {
    return false;
  }
  
  private void notifyListeners() {
  }

}

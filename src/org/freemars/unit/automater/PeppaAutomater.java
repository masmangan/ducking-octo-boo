package org.freemars.unit.automater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.freemars.controller.FreeMarsController;
import org.freemars.tile.SpaceshipDebrisCollectable;
import org.freerealm.executor.CommandResult;
import org.freerealm.executor.command.AttackUnitCommand;
import org.freerealm.executor.command.UnitAdvanceToCoordinateCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.map.Path;
import org.freerealm.tile.Tile;
import org.freerealm.unit.AbstractUnitAutomater;
import org.freerealm.unit.Unit;
import java.math.*;
public class PeppaAutomater extends AbstractUnitAutomater {

	private static final String NAME = "freeMarsScoutAutomater";
	private FreeMarsController freeMarsController;
	private Unit target = null; // unidade a ser atacada
	private Tile targetTile; // tile onde esta a unidade sendo atacada
	private boolean attackMode = false;
	private boolean moveMode = false;
	private int step = 0;
	private Path pathToTarget = null;

	public FreeMarsController getFreeMarsController() {
		return freeMarsController;
	}

	public void setFreeMarsController(FreeMarsController freeMarsController) {
		this.freeMarsController = freeMarsController;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	private double coordinateDiff(Coordinate a, Coordinate b){
		int Xa, Xb, Ya, Yb; 
		Xa = a.getAbscissa();
		Xb = b.getAbscissa();
		Ya = a.getOrdinate();
		Yb = b.getOrdinate();
		return Math.sqrt(Math.pow(Xa - Xb, 2) + Math.pow(Ya - Yb, 2));
	}

	@Override
	public void automate() {
		if (moveMode) {
			Logger.getLogger(ScoutAutomater.class).info(
					"indo para " + target.getCoordinate().toString());
			if (pathToTarget != null) {
				step++;
				if(step < pathToTarget.getLength()){
					
					freeMarsController.execute(new UnitAdvanceToCoordinateCommand(
							freeMarsController.getFreeMarsModel().getRealm(),
							getUnit(), pathToTarget.getStep(step)));
					//calcula distancia do alvo
					//se for menor que 1 estamos prontos para atacar
					double diff = (coordinateDiff(getUnit().getCoordinate(), target.getCoordinate()));
					if(diff <= 2){
						attackMode = true; // vai para modo de ataque
						moveMode = false; // sai do modo de movimentação
						return;
						
					}
					
					return;
					
				}
				else{
					attackMode = true; // vai para modo de ataque
					moveMode = false; // sai do modo de movimentação
					return;
				}
			}

		}
		// procura algo para atacar
		if (attackMode) {
			Logger.getLogger(ScoutAutomater.class).info(
					"indo atacar em " + target.getCoordinate().toString());
			
			if(target.getStatus() != Unit.UNIT_ACTIVE){

				attackMode = false;
				moveMode = false;
				step = 0;
				pathToTarget = null;
				target = null;
				
			}
			else{
				Logger.getLogger(ScoutAutomater.class).info(
						"unidade não mais esta ativa " + getUnit().getName());
			}
			
			freeMarsController.execute(new
					 AttackUnitCommand(freeMarsController.getFreeMarsModel().getRealm(),
					 getUnit(), target));
			

			return; 
		}

		Unit unit = findAttackableUnits(getUnit());
		if (unit != null) {

			// freeMarsController.execute(new
			// AttackUnitCommand(freeMarsController.getFreeMarsModel().getRealm(),
			// getUnit(), unit));
			/*
			 * CommandResult result = freeMarsController.execute(new
			 * UnitAdvanceToCoordinateCommand(
			 * freeMarsController.getFreeMarsModel().getRealm(), getUnit(),
			 * unit.getCoordinate()));
			 */
			
			
			Logger.getLogger(ScoutAutomater.class).info(
					"indo atacar " + unit.getName());  
			
			
			
			pathToTarget = freeMarsController.getFreeMarsModel().getRealm()
					.getPathFinder()
					.findPath(getUnit(), unit.getCoordinate(), true);
			Coordinate c = pathToTarget.getStep(0);
			freeMarsController.execute(new UnitAdvanceToCoordinateCommand(
					freeMarsController.getFreeMarsModel().getRealm(),
					getUnit(), c));

			step = 1;
			moveMode = true;
			target = unit;

			Logger.getLogger(ScoutAutomater.class).info(
					"movendo " + getUnit().getName());

		}

		else if (isScoutingNeeded(getUnit())) {

			Coordinate scoutingSite = findScoutingSite(getUnit());

			if (scoutingSite != null) {
				freeMarsController.execute(new UnitAdvanceToCoordinateCommand(
						freeMarsController.getFreeMarsModel().getRealm(),
						getUnit(), scoutingSite));
			} else {
				String log = "Peppa automater for \""
						+ getUnit().getName()
						+ "\" could not find any tiles to explore in turn "
						+ freeMarsController.getFreeMarsModel()
								.getNumberOfTurns()
						+ ". Removing automater for \"" + getUnit().getName()
						+ "\".";
				Logger.getLogger(ScoutAutomater.class).info(log);
				getUnit().setAutomater(null);
			}

		}

	}

	private boolean isScoutingNeeded(Unit peppa) {
		// esta função procura em um raio crescente de 1 a 10 unidades de medida
        // todos os
        // tiles não explorados.
        // Caso tenha algum tile não explorado em um raio de 10 unidades de
        // medida
        // retorna true se não, não tem nada a ser explorado
        // nossa unidade é capaz de ir a distancias mais afastadas, diferente do
        // scout que só procura tiles com até 10 unidades de medida de distancia

        for (int i = 1; i < 20; i++) {
                List<Coordinate> circleCoordinates = freeMarsController
                                .getFreeMarsModel().getRealm()
                                .getCircleCoordinates(peppa.getCoordinate(), i);
                for (Coordinate coordinate : circleCoordinates) {
                        if (coordinate != null
                                        && !peppa.getPlayer().isCoordinateExplored(coordinate)) {
                                return true;
                        }
                }
        }
		return false;
	}

	private Coordinate findScoutingSite(Unit scout) {
		// código de scouting....
        for (int i = 1; i < 10; i++) {
                List<Coordinate> circleCoordinates = freeMarsController
                                .getFreeMarsModel().getRealm()
                                .getCircleCoordinates(scout.getCoordinate(), i);
                List<Coordinate> candidateCoordinates = new ArrayList<Coordinate>();
                for (Coordinate coordinate : circleCoordinates) {
                        if (isCoordinateOkForScouting(scout, coordinate)) {
                                candidateCoordinates.add(coordinate);
                        }
                }
                Collections.shuffle(candidateCoordinates);
                for (Coordinate candidateCoordinate : candidateCoordinates) {
                        Tile tile = freeMarsController.getFreeMarsModel().getTile(
                                        candidateCoordinate);
                        if (scout.getPlayer().getSettlementCount() > 0
                                        && tile.getCollectable() != null
                                        && (tile.getCollectable() instanceof SpaceshipDebrisCollectable)) {
                                return candidateCoordinate;
                        }
                }
        }
        for (int i = 1; i < 10; i++) {
                List<Coordinate> circleCoordinates = freeMarsController
                                .getFreeMarsModel().getRealm()
                                .getCircleCoordinates(scout.getCoordinate(), i);
                List<Coordinate> candidateCoordinates = new ArrayList<Coordinate>();
                for (Coordinate coordinate : circleCoordinates) {
                        if (isCoordinateOkForScouting(scout, coordinate)) {
                                candidateCoordinates.add(coordinate);
                        }
                }
                Collections.shuffle(candidateCoordinates);
                for (int j = 4; j > 0; j--) {
                        for (Coordinate candidateCoordinate : candidateCoordinates) {
                                if (getUnexploredNeighborCount(candidateCoordinate) >= j) {
                                        return candidateCoordinate;
                                }
                        }
                        for (Coordinate candidateCoordinate : candidateCoordinates) {
                                if (getUnexploredNeighborCount(candidateCoordinate) >= j) {
                                        return candidateCoordinate;
                                }
                        }
                }
        }

		return null;
	}

	private Unit findAttackableUnits(Unit peppa) {
		for (int i = 1; i < 10; i++) {
			// pega as coordenas a i de distancia
			List<Coordinate> circleCoordinates = freeMarsController
					.getFreeMarsModel().getRealm()
					.getCircleCoordinates(peppa.getCoordinate(), i);
			Unit bestAttack = null;
			// procura pela unidade mais fraca para atacar
			// de maneira que a unidade mais proxima e mais fraca é escolhida
			// para atacar
			for (Coordinate c : circleCoordinates) {
				if (c != null) {
					Tile tile = freeMarsController.getFreeMarsModel()
							.getTile(c);
					if (tile.getNumberOfUnits() > 0) {
						// tile tem o metodo getUnitsIterator, que não pode ser
						// usado
						// pois vai gerar a exceção
						// java.util.ConcurrentModificationException
						// devido as modificações feitas no iterator durante o
						// processamento do jogo
						// de maneira que etrySet representa o estado em que os
						// dados estão(mesmo que ele mude).
						TreeMap<Integer, Unit> map = tile.getUnits();
						for (Map.Entry<Integer, Unit> entry : map.entrySet()) {
							Unit unit = entry.getValue();

							if (unit.canAttack()
									&& unit.getPlayer() != peppa.getPlayer()) {
								if (bestAttack == null) {
									bestAttack = unit;
								} else if (unit.getDefencePoints() < bestAttack
										.getDefencePoints()) {
									bestAttack = unit;
								}

							}
						}
					}
				}
			}
			if (bestAttack != null && isCoordinateOkForAttacking(bestAttack.getCoordinate())) {
				return bestAttack;
			}

		}

		return null;

	}
	private boolean isCoordinateOkForAttacking(Coordinate coordinate){
		Path path = freeMarsController.getFreeMarsModel().getRealm()
				.getPathFinder().findPath(getUnit(), coordinate, true);
		if (path == null) {
			return false;
		}
		else
			return true;
		
	}
	
	private boolean isCoordinateOkForScouting(Unit scout, Coordinate coordinate) {
		if (coordinate == null) {
			return false;
		}
		if (!getUnit().getPlayer().isCoordinateExplored(coordinate)) {
			return false;
		}
		Tile tile = freeMarsController.getFreeMarsModel().getTile(coordinate);

		if (tile == null) {
			return false;
		}
		Path path = freeMarsController.getFreeMarsModel().getRealm()
				.getPathFinder().findPath(scout, coordinate, false);
		if (path == null) {
			return false;
		}
		return true;
	}

	private int getUnexploredNeighborCount(Coordinate coordinate) {
		int unexploredNeighborCount = 0;
		List<Coordinate> neighbors = freeMarsController.getFreeMarsModel()
				.getRealm().getCircleCoordinates(coordinate, 1);
		for (Coordinate neighbor : neighbors) {
			if (!getUnit().getPlayer().isCoordinateExplored(neighbor)) {
				unexploredNeighborCount = unexploredNeighborCount + 1;
			}
		}
		return unexploredNeighborCount;
	}

}
